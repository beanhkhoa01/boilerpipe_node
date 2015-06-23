package com.radardisc.boilerpipe_node;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Log {

	public static Logger _logger;
	public static void init(){
		//initLog4j();
		_logger = LoggerFactory.getLogger("R");
		
	}
	
	public static void exception(Exception e, String string, Object... args) {
		_logger.error(e.getMessage());
		_logger.error(String.format(string,args) );
	}
	
	
	public static void info(String string, Object... args) {
		_logger.info(String.format(string,args));	
	}
	
	public static void error(String string, Object... args) {
		_logger.error(String.format(string,args));
	}
	
	public static void debug(String string, Object... args) {
		_logger.debug(String.format(string,args));
	}
	


	

	
	
}
