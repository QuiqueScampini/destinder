#!/bin/bash

cd `dirname $0`

pid=`ps -ef | grep spring-boot | grep destinder | awk '{print $2}'`

if [ ! -z $pid ];
then
	echo "Destinder is Running with Process id $pid".
	exit 1
fi

echo "Starting Destinder App"
mvn spring-boot:run > destinder.log &

while [ -z $pid ]
do
	pid=`ps -ef | grep spring-boot | grep destinder | awk '{print $2}'`
	sleep 0.5
done
	
echo "Destinder started with Process id $pid".


