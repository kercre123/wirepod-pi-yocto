package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"time"
	"os/exec"
	"regexp"
	"strings"
)

type Network struct {
	SSID string `json:"ssid"`
}

type ConnectRequest struct {
	SSID     string `json:"ssid"`
	Password string `json:"password"`
}

func scanNetworks() ([]Network, error) {
	cmd := exec.Command("sudo", "iw", "dev", "wlan0", "scan")
	output, err := cmd.Output()
	if err != nil {
		return nil, err
	}

	lines := strings.Split(string(output), "\n")
	networks := []Network{}
	ssidPattern := regexp.MustCompile(`SSID: (.+)`)

	for _, line := range lines {
		matches := ssidPattern.FindStringSubmatch(line)
		if matches != nil && len(matches) > 1 {
			ssid := matches[1]
			if ssid != "" {
				networks = append(networks, Network{SSID: ssid})
			}
		}
	}

	return networks, nil
}

func connectToNetwork(ssid, password string) error {
	cmd := exec.Command("sudo", "nmcli", "dev", "wifi", "connect", ssid, "password", password, "ifname", "wlan0")
	return cmd.Run()
}

func scanHandler(w http.ResponseWriter, r *http.Request) {
	networks, err := scanNetworks()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	json.NewEncoder(w).Encode(networks)
}

func connectHandler(w http.ResponseWriter, r *http.Request) {
	var req ConnectRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	if err := connectToNetwork(req.SSID, req.Password); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusOK)

	go func() {
		os.WriteFile("/etc/wifi-web-app/done", []byte("true"), 0755)
		time.Sleep(time.Second*1)
		DisableSelfStartWirePod()
	}()
}

func DisableSelfStartWirePod() {
//	fmt.Println("Disabling self, enabling wire-pod, then exiting")
//	exec.Command("/bin/bash", "-c", "systemctl disable wifi-web-app").Run()
//	exec.Command("/bin/bash", "-c", "systemctl enable wire-pod").Run()
//	exec.Command("/bin/bash", "-c", "sync").Run()
	os.Exit(0)
}

func main() {
	if _, err := os.ReadFile("/etc/wifi-web-app/done"); err == nil {
		fmt.Println("wifi-web-app done already")
	}
	os.Chdir("/etc/wifi-web-app")
	http.HandleFunc("/scan", scanHandler)
	http.HandleFunc("/connect", connectHandler)

	fs := http.FileServer(http.Dir("./static"))
	http.Handle("/", fs)

	fmt.Println("Server starting on port 8080")
	log.Fatal(http.ListenAndServe(":8080", nil))

}
