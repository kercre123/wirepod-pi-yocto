DESCRIPTION = "OpenBLAS is an optimized BLAS library based on GotoBLAS2 1.13 BSD version."
SUMMARY = "OpenBLAS : An optimized BLAS library"
HOMEPAGE = "http://www.openblas.net/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5adf4792c949a00013ce25d476a2abc0"

SRC_URI = "\
    git://github.com/xianyi/OpenBLAS;protocol=https;branch=develop \
"

# tag 0.3.20
SRCREV = "0b678b19dc03f2a999d6e038814c4c50b9640a4e"
S = "${WORKDIR}/git"

DEPENDS += "libgfortran"

def map_arch(d):
    import re
    arch = d.getVar('TARGET_ARCH', True)
    if   re.match('i.86$', arch):    return 'ATOM'
    elif re.match('x86_64$', arch):  return 'ATOM'
    elif re.match('aarch32$', arch): return 'CORTEXA9'
    elif re.match('aarch64$', arch): return 'ARMV8'
    return 'CORTEXA15'

def map_bits(d):
    import re
    arch = d.getVar('TARGET_ARCH', True)
    if   re.match('i.86$', arch):    return 32
    elif re.match('x86_64$', arch):  return 64
    elif re.match('aarch32$', arch): return 32
    elif re.match('aarch64$', arch): return 64
    return 32

EXTRA_OEMAKE = "\
    BUILD_WITHOUT_LAPACK=OFF \
    HOSTCC=${BUILD_CC} \
    CROSS=1 \
    CROSS_SUFFIX=${TARGET_PREFIX} \
    BINARY=${@map_bits(d)} \
    TARGET=${@map_arch(d)} \
    OPENBLAS_LIBRARY_DIR=${D}${libdir} \
    DYNAMIC_ARCH=ON \
    BUILD_STATIC_LIBS=ON \
    USE_LOCKING=1 \
    USE_THREAD=0 \
"

do_install() {
    oe_runmake PREFIX=${D}${prefix} install
    rm -rf ${D}${bindir} ${D}${libdir}/cmake

    # fixup pkgconfig file
    sed -i -e "s#libdir=/.*#libdir=${libdir}#" ${D}${libdir}/pkgconfig/openblas.pc
    sed -i -e "s#includedir=/.*#includedir=${includedir}#" ${D}${libdir}/pkgconfig/openblas.pc

    cat  ${D}${libdir}/pkgconfig/openblas.pc

    # Create symlink from libblas.so to libopenblas.so.0, required by scipy
    ln -s libopenblas.so.0 ${D}${libdir}/libblas.so
}

FILES:${PN}     = "${libdir}/*"
FILES:${PN}-dev = "${includedir} ${libdir}/libopenblas.so ${libdir}/libblas.so"
