#!/usr/bin/env bash
#REM ************************************************************************************
#REM Description: run 
#REM Author: Rui S. Moreira
#REM Date: 10/04/2018
#REM ************************************************************************************
#REM Script usage: runclient <role> (where role should be: producer / consumer)
source ./setenv.sh client

echo ${ABSPATH2CLASSES}
cd ${ABSPATH2CLASSES}
#clear
#pwd
java -cp ${CLASSPATH} \
     ${JAVAPACKAGEROLE}.${CONSUMER_CLASS_PREFIX} ${BROKER_HOST} ${BROKER_PORT} ${BROKER_EXCHANGE} 1 SmallVs 1 create

echo ${ABSPATH2SRC}/${JAVASCRIPTSPATH}
cd ${ABSPATH2SRC}/${JAVASCRIPTSPATH}
#pwd