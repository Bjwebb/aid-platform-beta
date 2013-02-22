#!/bin/bash

cd src/site
rm -rf build
bundle install
bundle exec middleman build --clean
cp -fR build/* /mnt/www
cd ../..
