#!/bin/sh
set -e
BASE_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
WRAPPER_JAR="$BASE_DIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPS="$BASE_DIR/.mvn/wrapper/maven-wrapper.properties"
JAVA_EXEC="${JAVA_HOME:+$JAVA_HOME/bin/}java"
if [ ! -x "${JAVA_EXEC}" ]; then
  JAVA_EXEC=java
fi
exec "$JAVA_EXEC" -classpath "$WRAPPER_JAR" -Dmaven.multiModuleProjectDirectory="$BASE_DIR" org.apache.maven.wrapper.MavenWrapperMain "$@"
