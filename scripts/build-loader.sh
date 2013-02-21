#!/bin/bash

# Update current repo
git pull origin master
git submodule init
git submodule update --merge

cd build
git checkout master
git pull origin master
cd ..

# Remove curent Loader aretfacts
rm -rf ./build/artefacts/loader

# Build new artefacts
cd ./src/loader/
sbt clean "one-jar"
cd ../..

# Move new artefacts
mkdir -p ./build/artefacts/loader
mv ./src/loader/target/scala-2.10/*-dist.jar ./build/artefacts/loader

# Commit new artefacts
cd build
git add -A
git commit -m "Automated Commit of Loader app"
git push origin master
cd ..