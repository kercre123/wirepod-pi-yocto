#!/bin/bash

MAC_FILE="/etc/uap0_mac"

if [ -f "$MAC_FILE" ]; then
    cat "$MAC_FILE"
else
    MAC_ADDRESS=$(cat /sys/class/net/wlan0/address | cut -d: -f1-3)$(openssl rand -hex 3 | sed 's/\(..\)/:\1/g')
    echo "$MAC_ADDRESS" > "$MAC_FILE"
    echo "$MAC_ADDRESS"
fi
