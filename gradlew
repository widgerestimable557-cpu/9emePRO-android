#!/usr/bin/env sh
##############################################################################
# Gradle start up script for UN*X
##############################################################################
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`
DEFAULT_JVM_OPTS=""
MAX_FD="maximum"
GRADLE_OPTS="$GRADLE_OPTS \"-Xmx64m\" \"-Xms64m\""

warn () {
    echo "$*"
}
die () {
    echo
    echo "$*"
    echo
    exit 1
}

OS="`uname`"
case "$OS" in
  Cygwin* )       cygwin=true  ;;
  Darwin* )       darwin=true  ;;
  MINGW* )        msys=true    ;;
esac

APP_HOME="`pwd -P`"
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

JAVACMD="java"

exec "$JAVACMD" \
  $DEFAULT_JVM_OPTS \
  $JAVA_OPTS \
  $GRADLE_OPTS \
  "-Dorg.gradle.appname=$APP_BASE_NAME" \
  -classpath "$CLASSPATH" \
  org.gradle.wrapper.GradleWrapperMain \
  "$@"
