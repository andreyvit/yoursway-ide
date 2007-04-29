#!/bin/sh

JAVA=java
ECLIPSE_DIR=/home/builder/eclipse
BASEBUILD_DIR=/home/builder/org.eclipse.releng.basebuilder

(cd $BASEBUILD_DIR/plugins/org.eclipse.pde.build/scripts; \
 $JAVA -jar $ECLIPSE_DIR/plugins/org.eclipse.platform_*/startup.jar \
        -application org.eclipse.ant.core.antRunner \
        -console \
        -consoleLog \
        -install $ECLIPSE_DIR \
        -Dbuilder=$PWD \
        -DbuildDirectory=$PWD/work \
        -DbaseLocation=$ECLIPSE_DIR \
)
