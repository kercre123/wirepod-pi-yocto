FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://dhcpcd.conf"

do_install:append() {
    install -m 0644 ${WORKDIR}/dhcpcd.conf ${D}${sysconfdir}/dhcpcd.conf
}
