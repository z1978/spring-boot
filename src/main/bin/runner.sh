#!/bin/bash
#
# chkconfig: 345 99 05 
# description: Java deamon script
#
# A non-SUSE Linux start/stop script for Java daemons.
#
# Derived from -
# Home page: http://www.source-code.biz
# License:    GNU/LGPL (http://www.gnu.org/licenses/lgpl.html)
# Copyright 2006 Christian d'Heureuse, Inventec Informatik AG, Switzerland.
#
# History:
# 2017-02-07 Zac: Removed 'su' and adapt for the zac enviroment
# 2010-09-21 Josh Davis: Changed 'sudo' to 'su', fix some typos, removed unused variables
# 2009-03-04 Josh Davis: Ubuntu/Redhat version.
# 2006-06-27 Christian d'Heureuse: Script created.
# 2006-07-02 chdh: Minor improvements.
# 2006-07-10 chdh: Changes for SUSE 10.0.

# Set this to your Java installation
JAVA_HOME="/usr/java/jdk1.7.0_79"
LANG=ja_JP.UTF-8
JAVA_OPTION="-Xmx512m -Xms256m -Duser.timezone=UTC -Dspring.profiles.active=${app.env} -Djava.security.egd=/dev/urandom"
# arguments for Java launcher
export JAVA_HOME LANG

# Set this to your application config
serviceName="${project.name}"                                   		  # service name
serviceLocation="zac"				                      		     	  # service name with the first letter in lowercase
mainClass="com.example.zac.Application"         		        	 	  # main class to launch the service
applDir="/usr/local/var/jobs/$serviceLocation"                            # home directory of the service application
serviceLogFile="/usr/local/var/log/jobs/$serviceLocation/runner.log"  # log file for StdOut/StdErr


# Relatively stable parameters 
serviceUser="tomcat"                                                # OS user name for the service
serviceGroup="zac"                                                  # OS group name for the service
maxShutdownTime=15                                                  # maximum number of seconds to wait for the daemon to terminate normally
pidFile="$applDir/$serviceName.pid"                                 # name of PID file (PID = process ID number)
javaCommand="java"                                                  # name of the Java launcher without the path
javaExe="$JAVA_HOME/bin/$javaCommand"                               # file name of the Java application launcher executable
executeJar="$serviceName.jar"                            
#classPath=${applDir}/conf/:${applDir}/${executeJar}:${applDir}/lib/*:${applDir}
classPath=${applDir}/conf/:${applDir}/lib/*
javaCommandLine="$javaExe -cp '$classPath' $JAVA_OPTION $mainClass" # command line to start the Java service application
javaCommandLineKeyword="$executeJar"                                # a keyword that occurs on the commandline, used to detect an already running service process and to distinguish it from others

# Try to use setsid instead of nohup if available.
# Because nohup is a poor way to isolate a child process. Specifically, it doesn't put the process into a new process group,
# so it's vulnerable to any signal sent to the entire process group (of which Ctrl+C is one.) 
# setsid is a better way of doing this. This puts the process into a new session (hence also new process group.)
# So no group-wide signal will get to the child process.
if [command -v setsid >/dev/null 2>&1]; then
    daemonCommand="setsid"
else
    daemonCommand="nohup"
fi


# # Auto detect and set the APP DIRs
# if [ -z "$APP_HOME" ]; then
#         APP_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." > /dev/null && pwd )"
# fi
# if [ -z "$APP_NAME" ]; then
#         APP_NAME="$(basename $APP_NAME)"
# fi

# Makes the file $1 writable by the group $serviceGroup.
function makeFileWritable {
    local filename="$1"
    touch $filename || return 1
    #chgrp $serviceGroup $filename || return 1
    chmod g+w $filename || return 1
    return 0; }

# Returns 0 if the process with PID $1 is running.  
function checkProcessIsRunning {
    local pid="$1"
    if [ -z "$pid" -o "$pid" == " " ]; then return 1; fi
    if [ ! -e /proc/$pid ]; then return 1; fi
    return 0; }

# Returns 0 if the process with PID $1 is our Java service process.
function checkProcessIsOurService {
    local pid="$1"
    if [ "$(ps -p $pid --no-headers -o comm)" != "$javaCommand" ]; then return 1; fi
    grep -q --binary -F "$javaCommandLineKeyword" /proc/$pid/cmdline
    if [ $? -ne 0 ]; then return 1; fi
    return 0; }

# Returns 0 when the service is running and sets the variable $pid to the PID.
function getServicePID {
    if [ ! -f $pidFile ]; then return 1; fi
    pid="$(<$pidFile)"
    checkProcessIsRunning $pid || return 1
    checkProcessIsOurService $pid || return 1
    return 0; }

function startOneJob {
    cd $applDir || return 1
    makeFileWritable $serviceLogFile || return 1
    cmd="$javaCommandLine $CLI_PARMS"
    log "$cmd"
    eval "$cmd" || return 1
    return 0; }

function startServiceProcess {
    cd $applDir || return 1
    rm -f $pidFile
    makeFileWritable $pidFile || return 1
    makeFileWritable $serviceLogFile || return 1
    log "Starting $serviceName ..." "-q"

    #cmd="$daemonCommand $javaCommandLine service start >>$serviceLogFile 2>&1 & echo \$! >$pidFile"
    cmd="$daemonCommand $javaCommandLine service start >>/dev/null 2>&1 & echo \$! >$pidFile"
    log "$cmd" "-q"

    # We do not have root, and the $serviceUser is thus not used.
    #su -m $serviceUser -s $SHELL -c "$cmd" || return 1
    eval "$cmd" || return 1

    sleep 0.1
    pid="$(<$pidFile)"
    if checkProcessIsRunning $pid; then :; else
        log "\n$serviceName start failed, see logfile." "-ne"
        return 1
    fi
    log "$serviceName is started with pid:$pid." "-q"
    return 0; }

function stopServiceProcess {
    log "Stopping $serviceName ..." "-q"
    kill $pid || return 1
    for ((i=0; i<maxShutdownTime*10; i++)); do
        checkProcessIsRunning $pid
        if [ $? -ne 0 ]; then
            rm -f $pidFile
            log "$serviceName stopped normally." "-q"
            return 0
            fi
        sleep 0.1
        done
    log "\n$serviceName did not terminate within $maxShutdownTime seconds, sending SIGKILL..." "-e"
    kill -s KILL $pid || return 1
    local killWaitTime=15
    for ((i=0; i<killWaitTime*10; i++)); do
        checkProcessIsRunning $pid
        if [ $? -ne 0 ]; then
            rm -f $pidFile
            log "$serviceName is FORCED to stop!" "-q"
            return 0
            fi
        sleep 0.1
        done
    log "Error: $serviceName could not be stopped within $maxShutdownTime+$killWaitTime seconds!"
    return 1; }

function startService {
    getServicePID
    if [ $? -eq 0 ]; then echo -n "$serviceName is already running"; RETVAL=0; return 0; fi
    echo -n "Starting $serviceName ...  "
    startServiceProcess
    if [ $? -ne 0 ]; then RETVAL=1; echo "failed"; return 1; fi
    echo "started PID=$pid"
    RETVAL=0
    return 0; }

function stopService {
    getServicePID
    if [ $? -ne 0 ]; then echo -n "$serviceName is not running"; RETVAL=0; echo ""; return 0; fi
    echo -n "Stopping $serviceName ...   "
    stopServiceProcess
    if [ $? -ne 0 ]; then RETVAL=1; echo "failed"; return 1; fi
    echo "stopped PID=$pid"
    RETVAL=0
    return 0; }

function checkServiceStatus {
    echo -n "Checking for $serviceName: ...   "
    if getServicePID; then
        echo "running PID=$pid"
        RETVAL=0
    else
        echo "stopped"
        RETVAL=3
    fi
    return 0; }

function keepServiceAlive {
    log "Checking for $serviceName: ...   " "-n"
    if getServicePID; then
        log "Alive. PID=$pid"
        RETVAL=0
    else
        log "Dead! Try to restart ..." 
        startService
    fi
    return 0; }

function log {
    if [ "$2" == "-q" ]; then
        :
        #echo "$1"
    else
        eval 'echo $2 "$1"'
    fi
    makeFileWritable $serviceLogFile || return 1
    echo "$(date +"%Y-%m-%d %H:%M:%S%z") $1" >> $serviceLogFile

    return 0; }

function main {
    RETVAL=0
    
    case "$1" in
        start)                                # starts the Java program as a Linux service
            startService
            ;;
        stop)                                 # stops the Java program service
            stopService
            ;;
        restart)                              # stops and restarts the service
            stopService && startService
            ;;
        status)                               # displays the service status
            checkServiceStatus
            ;;
        job)                                  # starts a specific job
            startOneJob
            ;;
        watchdog)                             # check service status, if dead, restart the service
            keepServiceAlive
            ;;
        *)
            echo "Usage: $0 {start|stop|restart|status|watchdog|job jobName}"
            exit 1
            ;;
        esac
    exit $RETVAL
}
##############################################################
#     main
##############################################################
source "$applDir/bin/env.sh" || fail "You must first prepare env.sh  in `pwd`/bin/." || exit 1

CLI_PARMS=$@
if [ `whoami` != $serviceUser ]; then
     fatal "Fatal Error! The batch should be executed by $serviceUser only!!!"
     exit 1
fi
main $1

