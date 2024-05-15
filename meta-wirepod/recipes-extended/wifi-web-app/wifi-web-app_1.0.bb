SUMMARY = "Wi-Fi config program"
DESCRIPTION = "Hosts web page for configuring Wi-Fi"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://main.go \
	   file://static \
	   file://go.mod"

#	   file://start-wire-pod.sh \
#	   file://wifi-web-app.service"

S = "${WORKDIR}"

inherit go

GO_IMPORT = "wifi-web-app"

# we need to get go stuff
do_configure[network] = "1"
do_compile[network] = "1"

do_compile() {
    go build -modcacherw -o ${B}/wifi-web-app ${S}/main.go
}

do_install() {
    # wifi-web-app binary
    install -d ${D}/usr/bin
    install -m 0755 ${B}/wifi-web-app ${D}/usr/bin/wifi-web-app

    #install -m 0755 ${WORKDIR}/start-wire-pod.sh ${D}/usr/bin/start-wire-pod.sh

    install -d ${D}/etc/wifi-web-app
    cp -r ${WORKDIR}/static ${D}/etc/wifi-web-app/

    # systemd service
    #install -d ${D}/etc/systemd/system/multi-user.target.wants
    #install -m 0755 ${WORKDIR}/wifi-web-app.service ${D}/etc/systemd/system/wifi-web-app.service
    #ln -sf /etc/systemd/system/wifi-web-app.service ${D}/etc/systemd/system/multi-user.target.wants/wifi-web-app.service
}

FILES:${PN} += "${bindir}/wifi-web-app \
		${bindir}/start-wire-pod.sh"

#		/etc/systemd/system/multi-user.target.wants"

INSANE_SKIP:${PN} = "file-rdeps textrel buildpaths ldflags"
