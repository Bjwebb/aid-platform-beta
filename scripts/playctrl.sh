#!/bin/bash

APPLICATION=$1
COMMAND=$2
PORT=9000

if [ ! -z "$3" ]; then
	PORT=$3;
fi

echo app $APPLICATION
echo com $COMMAND
echo port $PORT

function debug_port {
	DEBUG_PORT=$(($PORT+1000))
	export SBT_EXTRA_PARAMS="-Xdebug -Xrunjdwp:transport=dt_socket,address=$DEBUG_PORT,server=y,suspend=n"
}

function start_app {
	debug_port
	mkdir -p $APPLICATION/logs

	if [ -f "$APPLICATION/logs/application.log"  ]; then
		kill_app
	fi
	echo " Starting application "
	nohup ./$APPLICATION/start -Dhttp.port=$PORT > "$APPLICATION/logs/application.log" 2>&1 &
}

function kill_app {
	if [ -f $APPLICATION/RUNNING_PID ]; then
		echo " Killing running application "
		kill `cat $APPLICATION/RUNNING_PID`
		`rm -f "$APPLICATION/logs/application.log"` 
		`rm -f "$APPLICATION/RUNNING_PID"`
	fi
	sleep 2
}

case "$COMMAND" in 
	'start')
		start_app
		;;
	'stop')
		kill_app
		;;
	*)
		echo "Usage $0 {app_ROOT_DIR app_name | [start|stop] [port] }"
		;;
esac
exit 0 

