DESCRIPTION = "A minimal image with AP configuration for Raspberry Pi"
LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL += "\
    networkmanager \
    rng-tools \
    hostapd \
    dnsmasq \
    dhcpcd \
    bash \
    openssh \
    rsync \
    sudo \
    dpkg \
    avahi-daemon \
    avahi-autoipd \
    vosk \
    libopus \
    libsodium \
    nano \
    iw \
    htop \
    openssl-bin \
    wire-pod \
    ap-wifi-conf \
    coreutils \
    wifi-web-app \
"

IMAGE_FEATURES += "ssh-server-openssh"

IMAGE_LINGUAS = " "

inherit extrausers
# password: wirepod
PASSWD="\$5\$BQkNxoSKGuem6Huy\$kYnah7TN1LRsu2WxeC4TeCg5hYa3bHJ11SG4zv.bGA1"
EXTRA_USERS_PARAMS = "\
    useradd -p '${PASSWD}' -G sudo -s /bin/bash -d /home/wirepod -m wirepod; \
    usermod -p '${PASSWD}' root; \
    "

LICENSE = "MIT"
