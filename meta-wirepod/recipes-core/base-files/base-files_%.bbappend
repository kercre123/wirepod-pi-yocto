FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://profile \
	    file://motd"

do_install:append() {
    install -m 0777 ${WORKDIR}/profile ${D}/etc/profile
    install -m 0777 ${WORKDIR}/motd ${D}/etc/motd
    echo "192.168.4.1 escapepod" >> ${D}/etc/hosts
    echo "192.168.4.1 escapepod.local" >> ${D}/etc/hosts
}
