#! /bin/sh

export JAVA_HOME=/usr/lib/jvm/java-7-oracle/
export MAVEN_HOME=/opt/apache-maven-3.0.5/
export PATH=${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${PATH}

mvn install:install-file	-Dfile=swt-4.2.2-cocoa-macosx/swt.jar \
							-Dsources=swt-4.2.2-cocoa-macosx/src.zip \
							-DgroupId=org.eclipse.swt \
							-DartifactId=swt-cocoa-macosx-x86 \
							-Dversion=4.2.2 \
							-Dpackaging=jar \
							-DgeneratePom=true \
							-DgenerateChecksum=true

mvn install:install-file	-Dfile=swt-4.2.2-cocoa-macosx-x86_64/swt.jar \
							-Dsources=swt-4.2.2-cocoa-macosx-x86_64/src.zip \
							-DgroupId=org.eclipse.swt \
							-DartifactId=swt-cocoa-macosx-x86_64 \
							-Dversion=4.2.2 \
							-Dpackaging=jar \
							-DgeneratePom=true \
							-DgenerateChecksum=true

mvn install:install-file	-Dfile=swt-4.2.2-gtk-linux-x86/swt.jar \
							-Dsources=swt-4.2.2-gtk-linux-x86/src.zip \
							-DgroupId=org.eclipse.swt \
							-DartifactId=swt-gtk-linux-x86 \
							-Dversion=4.2.2 \
							-Dpackaging=jar \
							-DgeneratePom=true \
							-DgenerateChecksum=true

mvn install:install-file	-Dfile=swt-4.2.2-gtk-linux-x86_64/swt.jar \
							-Dsources=swt-4.2.2-gtk-linux-x86_64/src.zip \
							-DgroupId=org.eclipse.swt \
							-DartifactId=swt-gtk-linux-x86_64 \
							-Dversion=4.2.2 \
							-Dpackaging=jar \
							-DgeneratePom=true \
							-DgenerateChecksum=true

mvn install:install-file	-Dfile=swt-4.2.2-win32-win32-x86/swt.jar \
							-Dsources=swt-4.2.2-win32-win32-x86/src.zip \
							-DgroupId=org.eclipse.swt \
							-DartifactId=swt-win32-x86 \
							-Dversion=4.2.2 \
							-Dpackaging=jar \
							-DgeneratePom=true \
							-DgenerateChecksum=true

mvn install:install-file	-Dfile=swt-4.2.2-win32-win32-x86_64/swt.jar \
							-Dsources=swt-4.2.2-win32-win32-x86_64/src.zip \
							-DgroupId=org.eclipse.swt \
							-DartifactId=swt-win32-x86_64 \
							-Dversion=4.2.2 \
							-Dpackaging=jar \
							-DgeneratePom=true \
							-DgenerateChecksum=true