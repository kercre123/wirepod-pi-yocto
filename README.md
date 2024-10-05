# wirepod-pi-yocto

Yocto experiments, for now.

The end goal is to create an image which:

-	Runs on a Raspberry Pi Pi (any 64-bit capable Pi)
-	Hosts a network (Vector will connect to this)
-	Can connect to a network (for weather and kg)
-	Includes WirePod
-	Includes web-based Wi-Fi connection interface
-	Has network configured so escapepod.local directs to WirePod without multicast needed

So far, it compiles and does run on a Pi. It hosts a network and can connect to a network with NetworkManager. It includes wire-pod, the hostname is wirepod (escapepod.local on the WirePod network also directs to 192.168.4.1), and wire-pod starts at bootup.

Hostname: wirepod

Hosted AP credentials:

-	SSID: WirePod
-	Password: WirePod123
-	The Pi's IP is 192.168.4.1. It is accessible at escapepod.local


Linux user credentials:

-	user: root
-	password: wirepod

(it also creates a `wirepod` user with the same password, unused for now)

## Compilation

```
sudo apt install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 python3-subunit zstd liblz4-tool file locales libacl1
sudo locale-gen en_US.UTF-8
source env.sh raspberrypi
bitbake wirepod-image
```
