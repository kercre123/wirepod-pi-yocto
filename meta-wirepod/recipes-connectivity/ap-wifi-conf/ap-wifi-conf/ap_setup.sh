#!/bin/bash

MAC_ADDRESS=$(/usr/bin/generate_mac.sh)

/sbin/iw phy phy0 interface add uap0 type __ap
/sbin/ip link set dev uap0 address $MAC_ADDRESS
/sbin/ifconfig uap0 up

/bin/systemctl restart dhcpcd
/bin/systemctl restart dnsmasq
/bin/systemctl restart hostapd
