#! /bin/sh

# Clone repositories
git clone https://code.google.com/p/hott-transmitter-config/
git clone git@git.assembla.com:hottdecoder.git
git clone https://code.google.com/p/hott-transmitter-config.freemarker freemarker --branch 2.3-android

# Build and install Freemarker into local Maven repository
cd freemarker
ant update-deps
ant
mvn install:install-file -Dfile=build/freemarker.jar -DartifactId=freemarker -DgroupId=org.freemarker -Dpackaging=jar -Dversion=2.3.20 -Dclassifier=android
cd ..

# Install android.jar into local Maven repository
mvn install:install-file -Dfile=${HOME}/android-sdk-linux/platforms/android-19/android.jar -DartifactId=android -DgroupId=com.google.android -Dpackaging=jar -Dversion=19

# Build and install application
cd hott-transmitter-config
mvn install