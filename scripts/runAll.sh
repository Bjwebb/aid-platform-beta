#!/bin/bash
BUILD_ID=dontKillMe11

export DFID_DATA_PATH=/var/lib/jenkins/jobs/aid-platform-beta/workspace/build/data/iati-xml

whoami
mkdir -p ./running

echo "Deploying and running cms application..."
unzip -o ./build/artefacts/cms/*.zip -d ./running/
chmod a+x ./running/cms*/start
./scripts/playctrl.sh running/cms-1.0-SNAPSHOT/ start 9000

echo "Deploying and running api application..."
unzip -o ./build/artefacts/api/*.zip -d ./running/
chmod a+x ./running/api*/start
./scripts/playctrl.sh running/api-1.0-SNAPSHOT/ start 9001

echo "Deploying and running search application..."
unzip -o ./build/artefacts/search/*.zip -d ./running/
chmod a+x ./running/search*/start
./scripts/playctrl.sh running/search-1.0-SNAPSHOT/ start 9002


