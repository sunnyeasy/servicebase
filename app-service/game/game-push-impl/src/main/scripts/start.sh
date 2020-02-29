#!/bin/bash
cd `dirname $0`
cd ../
BASE_DIR=`pwd`
echo "BASE_DIR:$BASE_DIR"

CONF_DIR=$BASE_DIR/conf
echo "CONF_DIR:$CONF_DIR"

LIB_DIR=$BASE_DIR/lib
echo "LIB_DIR:$LIB_DIR"

SERVER_NAME='com.easy.game.gateway.GamePushMain'
PIDS=`ps -ef | grep java | grep "$LIB_DIR" |grep $SERVER_NAME|awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "start fail! The $SERVER_NAME already started!"
    exit 1
fi

cd $LIB_DIR
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
echo "LIB_JARS:$LIB_JARS"

cd ..
nohup java -server -Xms1g -Xmx1g -XX:PermSize=128m  -classpath $CONF_DIR:$LIB_JARS $SERVER_NAME 2>&1 &
echo "start "$SERVER_NAME" success!"
