#!/usr/bin/env bash

set -e

ANDROID_SDK_ARTIFACT=android-sdk_r21.1-macosx.zip

PROJECT_ROOT=$(pwd)
echo $PROJECT_ROOT
VENDOR_DIRECTORY=$PROJECT_ROOT/vendor
if [ ! -d $VENDOR_DIRECTORY ]; then
  mkdir $VENDOR_DIRECTORY
fi
ANDROID_SDK_DIRECTORY=$VENDOR_DIRECTORY/android-sdk-macosx

export ANDROID_HOME=$ANDROID_SDK_DIRECTORY
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

[ -f ./setup-env ] && . ./setup-env
if [ ! -d $ANDROID_SDK_DIRECTORY ]; then
  wget http://dl.google.com/android/$ANDROID_SDK_ARTIFACT
  unzip -d $VENDOR_DIRECTORY $ANDROID_SDK_ARTIFACT
  rm -rf $ANDROID_SDK_ARTIFACT
fi

android update sdk --no-ui --force --filter platform-tools,tools
echo no | android create avd -n emulator -t android-17 --skin WVGA800 --force --abi armeabi-v7a

cd $PROJECT_ROOT

gem install calabash-android  
