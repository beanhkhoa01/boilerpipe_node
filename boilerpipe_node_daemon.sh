#! /bin/sh
#  /etc/init.d/mydaemon

### BEGIN INIT INFO
# Provides:          mydaemon
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Short-Description: Starts the MyDaemon service
# Description:       This file is used to start the daemon
#                    and should be placed in /etc/init.d
### END INIT INFO

# Author:   Sheldon Neilson <sheldon[AT]neilson.co.za>
# Url:      www.neilson.co.za
# Date:     25/04/2013

#Copied by Radardisc from http://www.neilson.co.za/creating-a-java-daemon-system-service-for-debian-using-apache-commons-jsvc/

NAME="boilerpipe_node"
DESC="boilerpipe_node html parsing service"

# The path to Jsvc
EXEC="/usr/bin/jsvc"

# The path to the folder containing MyDaemon.jar
FILE_PATH="."

# The path to the folder containing the java runtime
JAVA_HOME="/usr/lib/jvm/default-java"

# Our classpath including our jar file and the Apache Commons Daemon library
CLASS_PATH="$FILE_PATH/boilerpipe_node.jar"

# The fully qualified name of the class to execute
CLASS="com.radardisc.boilerpipe_node.JSvc"

# Any command line arguments to be passed to the our Java Daemon implementations init() method
ARGS=""

#The user to run the daemon as
USER="ror"

# The file that will contain our process identification number (pid) for other scripts/programs that need to access it.
if [ "$USER" = "root" ] ; then
    PID="/var/run/$NAME.jsvc.pid"
else
    PID="$FILE_PATH/$NAME.jsvc.pid"
fi



# System.out writes to this file...
LOG_OUT="$FILE_PATH/log/$NAME.out"

# System.err writes to this file...
LOG_ERR="$FILE_PATH/log/$NAME.err"

jsvc_exec()
{  
    cd $FILE_PATH
    $EXEC -home $JAVA_HOME -cp $CLASS_PATH -user $USER -outfile $LOG_OUT -errfile $LOG_ERR -Dlog4j.configuration=file:$FILE_PATH/log4j.properties -pidfile $PID $1 $CLASS $ARGS
}

case "$1" in
    start) 
        echo "Starting the $DESC..."       
       
        # Start the service
        jsvc_exec
       
        echo "The $DESC has started."
    ;;
    stop)
        echo "Stopping the $DESC..."
       
        # Stop the service
        jsvc_exec "-stop"      
       
        echo "The $DESC has stopped."
    ;;
    restart)
        if [ -f "$PID" ]; then
           
            echo "Restarting the $DESC..."
           
            # Stop the service
            jsvc_exec "-stop"
           
            # Start the service
            jsvc_exec
           
            echo "The $DESC has restarted."
        else
            echo "Daemon not running, no action taken"
            exit 1
        fi
            ;;
    *)
    echo "Usage: /etc/init.d/$NAME {start|stop|restart}" >&2
    exit 3
    ;;
esac