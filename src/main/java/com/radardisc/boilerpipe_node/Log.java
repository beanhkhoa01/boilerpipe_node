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
		initLog4j();
		_logger = LoggerFactory.getLogger(Log.class);
		
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
	
	public static void initLog4j(){
			ConsoleAppender console = new ConsoleAppender(); //create appender
		  //configure the appender
		  String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		  console.setLayout(new PatternLayout(PATTERN)); 
		  console.setThreshold(Level.DEBUG);
		  console.activateOptions();
		  //add appender to any Logger (here is root)
		  org.apache.log4j.Logger.getRootLogger().addAppender(console);

		  FileAppender fa = new FileAppender();
		  fa.setName("FileLogger");
		  fa.setFile("mylog.log");
		  fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		  fa.setThreshold(Level.DEBUG);
		  fa.setAppend(true);
		  fa.activateOptions();

		  //add appender to any Logger (here is root)
		  org.apache.log4j.Logger.getRootLogger().addAppender(fa);
	}

	
	
}
