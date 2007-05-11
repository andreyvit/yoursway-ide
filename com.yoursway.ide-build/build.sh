#!/bin/sh

rm -rf src

JAVA=java
ECLIPSE_DIR=/home/builder/eclipse
PDEBUILD_DIR=${ECLIPSE_DIR}/plugins/org.eclipse.pde.build_*
STAMP=`date +%Y%m%d.%H%M`

CURDIR=$PWD

$JAVA -jar ${ECLIPSE_DIR}/plugins/org.eclipse.equinox.launcher_*.jar \
    -noSplash \
    -console \
    -consoleLog \
    -application org.eclipse.ant.core.antRunner \
    -buildfile ${PDEBUILD_DIR}/scripts/productBuild/productBuild.xml \
    -Dbuilder=${CURDIR} \
    -DbuildDirectory=${CURDIR}/src \
    -DbaseLocation=${ECLIPSE_DIR} \
    -DbuildId=${STAMP} && \
  cp -R ${CURDIR}/src/I.* /home/ftp/yoursway
