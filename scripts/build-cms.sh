#!/bin/bash

# Update current repo
git pull origin master

# Update the Build Submodule
cd ./build
git pull origin master
cd ..

# Remove curent CMS aretfacts
rm -rf ./build/artefacts/cms

# Build new artefacts
cd ./src/cms/
play clean dist
cd ../..

# Move new artefacts
mkdir -p ./build/artefacts/cms
mv ./src/cms/dist/*.zip ./build/artefacts/cms

# Commit new artefacts
cd build
git add -A
git commit -m "Automated Commit of CMS app"
git push origin master
cd ..