SUMMARY = "The actual wire-pod program"
DESCRIPTION = "Wire-Pod and system configuration"
HOMEPAGE = "https://github.com/kercre123/wire-pod"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/kercre123/WirePod.git;branch=main;protocol=https"
SRCREV = "b2fa8495ea6e5a4cdda1618ef0ae0bf408765e48"

S = "${WORKDIR}/git"

WD = "${WORKDIR}/git/src/github.com/kercre123/WirePod"

inherit go

# enable BLE on Pi
export GO_BUILD_TAGS = "inbuiltble,nolibopusfile"

DEPENDS += "vosk libopus libsodium pkgconfig-native"

GO_IMPORT = "github.com/kercre123/WirePod"

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

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/wire-pod ${D}${bindir}/wire-pod
}

FILES:${PN} += "${bindir}/wire-pod"
