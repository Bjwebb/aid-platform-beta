#!/bin/bash

cd src/site
rm -rf directory
bundle install
bundle exec middleman build --clean
cp -fR build/* /mnt/www
cd ../..
