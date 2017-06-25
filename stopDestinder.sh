#!/bin/bash

cd `dirname $0`

pid=`ps -ef | grep spring-boot | grep destinder | awk '{print $2}'`

if [ ! -z $pid ];
then
	kill $pid
	echo "Destinder Sopped"
else
	echo "Destinder is Not running"
fi

exit 0






