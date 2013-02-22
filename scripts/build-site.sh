#!/bin/bash

cd ../src/site
bundle install
bundle exec middleman build --clean
cp -R build/* /mnt/www
cd ../..
