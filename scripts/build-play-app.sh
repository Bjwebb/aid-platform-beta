#!/bin/bash

# Update current repo
git pull origin master
git submodule init
git submodule update --merge

# force master branch due to jenkins? detaching the head
cd build
git checkout master
git pull origin master
cd ..

# Remove curent aretfacts
rm -rf "./build/artefacts/"$1
mkdir -p "./build/artefacts/"$1

# Build new artefacts
cd "./src/"$1
play clean dist
cd ../..

# Move new artefacts
mv "./src/"$1"/dist/"*.zip "./build/artefacts/"$1

# Commit new artefacts
cd ./build
git add -A
git commit -m "Automated Commit: "$1
git push origin master
cd ..