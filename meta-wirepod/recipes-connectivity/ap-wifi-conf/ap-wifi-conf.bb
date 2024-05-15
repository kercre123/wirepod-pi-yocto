SUMMARY = "Pi WirePod Wi-Fi configuration"
DESCRIPTION = "Configure Pi to act as an AP while also able to connect to a network"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://uap0.service \
           file://generate_mac.sh \
           file://ap_setup.sh \
           file://unmanaged.conf \
	   file://disable-5ghz.conf"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "uap0.service"

# "script requires /bin/bash ????"
INSANE_SKIP:${PN} += "file-rdeps"
do_package_qa[noexec] = "1"

do_install() {
    install -d ${D}${sysconfdir}/systemd/system
    install -m 0644 ${WORKDIR}/uap0.service ${D}${sysconfdir}/systemd/system/

    install -d ${D}${sysconfdir}/NetworkManager/conf.d
    install -m 0644 ${WORKDIR}/unmanaged.conf ${D}${sysconfdir}/NetworkManager/conf.d/
    install -m 0644 ${WORKDIR}/disable-5ghz.conf ${D}${sysconfdir}/NetworkManager/conf.d/

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/generate_mac.sh ${D}${bindir}/
    install -m 0755 ${WORKDIR}/ap_setup.sh ${D}${bindir}/

    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants
    ln -sf /etc/systemd/system/uap0.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/uap0.service
}

