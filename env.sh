#!/bin/bash


if [[ ! -d build ]]; then
	mkdir -p build
	cp -r conf build/
fi

function fetch_poky() {
	BRANCH=$1
	DEVURL=$2
	DEVLAYERNAME=$3
	if [[ ! -d $DEVLAYERNAME ]]; then
        	git clone ${DEVURL} -b $BRANCH
	else
		cd $DEVLAYERNAME
		git checkout $BRANCH
		cd ..
	fi

	if [[ ! -d meta-openembedded ]]; then
        	git clone https://github.com/openembedded/meta-openembedded.git -b $BRANCH
	else
		cd meta-openembedded
		git checkout $BRANCH
		cd ..
	fi

	if [[ ! -d poky ]]; then
        	git clone https://github.com/yoctoproject/poky.git -b $BRANCH
	else
		cd poky
		git checkout $BRANCH
		cd ..
	fi
}

if [[ $1 == "raspberrypi" ]]; then
	fetch_poky scarthgap https://github.com/agherzan/meta-raspberrypi.git meta-raspberrypi
elif [[ $1 == "rockpis" ]]; then
	fetch_poky nanbield https://github.com/radxa/meta-rockchip.git meta-rockchip
else
	echo "valid target choices: raspberrypi, rockpis"
	exit 1
fi

source poky/oe-init-build-env
