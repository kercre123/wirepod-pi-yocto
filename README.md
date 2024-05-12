# wirepod-pi-yocto

Yocto experiments, for now.

The end goal is to create an image which:

-	Runs on a Raspberry Pi Pi (any 64-bit capable Pi)
-	Hosts a network (Vector will connect to this)
-	Can connect to a network (for weather and kg)
-	Includes WirePod
-	Includes web-based Wi-Fi connection interface
-	Has network configured so escapepod.local directs to WirePod without multicast needed

So far, it compiles and does run on a Pi. It hosts a network and can connect to a network with NetworkManager.

Hosted AP credentials:

-	SSID: WirePod
-	Password: WirePod123

Linux user credentials:

-	user: root
-	password: wirepod

(it also creates a `wirepod` user with the same password, unused for now)
