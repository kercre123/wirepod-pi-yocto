SUMMARY = "The actual wire-pod program"
DESCRIPTION = "Wire-Pod and system configuration"
HOMEPAGE = "https://github.com/kercre123/wire-pod"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/kercre123/WirePod.git;branch=main;protocol=https \
	   file://config.ini \
	   file://wire-pod.service"

SRCREV = "847d02e02a5536dc1e27cdaed9c06dc091a9eac2"

S = "${WORKDIR}/git"

WD = "${WORKDIR}/git/src/github.com/kercre123/WirePod"

inherit go

# enable BLE on Pi
export GO_BUILD_TAGS = "inbuiltble,nolibopusfile"

DEPENDS += "vosk libopus libsodium pkgconfig-native"

GO_IMPORT = "github.com/kercre123/WirePod"

# we need to get github and go stuff
do_configure[network] = "1"
do_compile[network] = "1"

# probably do wire-pod webroot in seperate recipe
do_configure:prepend() {
  cd ${WD}
  git submodule update --init --recursive
  if [[ ! -d vosk-api ]]; then
    git clone https://github.com/alphacep/vosk-api
  fi
  cd -
}

do_compile() {
    export CGO_ENABLED=1
    export CGO_CFLAGS="-I${STAGING_INCDIR} -I${WD}/vosk-api/src"
    export CGO_LDFLAGS="-L${STAGING_LIBDIR}"
    cd ${WD}/debian
    go build -tags ${GO_BUILD_TAGS} -modcacherw -o ${B}/wire-pod pod/main.go pod/web.go pod/server.go
}

CH = "${WD}/wire-pod/chipper"

do_install() {
    # wire-pod binary
    install -d ${D}/usr/bin
    install -m 0755 ${B}/wire-pod ${D}/usr/bin/wire-pod

    # /etc/wire-pod stuff
    install -d ${D}${sysconfdir}/wire-pod
    install -m 0755 ${WORKDIR}/config.ini ${D}/etc/wire-pod/config.ini
    cp -r ${CH}/webroot ${D}/etc/wire-pod/
    cp -r ${CH}/intent-data ${D}/etc/wire-pod/
    cp -r ${CH}/epod ${D}/etc/wire-pod/
    cp -r ${CH}/weather-map.json ${D}/etc/wire-pod/
    cp -r ${CH}/../vector-cloud/pod-bot-install.sh ${D}/etc/wire-pod/
    cp -r ${CH}/stttest.pcm ${D}/etc/wire-pod/

    # systemd service
    install -d ${D}/usr/lib/systemd/system
    install -d ${D}/etc/systemd/system/multi-user.target.wants
    install -m 0755 ${WORKDIR}/wire-pod.service ${D}/usr/lib/systemd/system/wire-pod.service
    ln -sf /usr/lib/systemd/system/wire-pod.service ${D}/etc/systemd/system/multi-user.target.wants/wire-pod.service
}

FILES:${PN} += "${bindir}/wire-pod \
		/etc/wire-pod \
		/usr/lib/systemd/system \
		/etc/systemd/system/multi-user.target.wants"

INSANE_SKIP:${PN} = "file-rdeps textrel buildpaths ldflags"
