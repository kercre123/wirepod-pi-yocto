#!/bin/bash


if [[ ! -d build ]]; then
	mkdir -p build
	cp -r conf build/
fi

if [[ ! -d meta-raspberrypi ]]; then
	git clone https://github.com/agherzan/meta-raspberrypi.git
fi

if [[ ! -d meta-openembedded ]]; then
	git clone https://github.com/openembedded/meta-openembedded.git
fi

if [[ ! -d poky ]]; then
	git clone https://github.com/yoctoproject/poky.git
fi

source poky/oe-init-build-env
