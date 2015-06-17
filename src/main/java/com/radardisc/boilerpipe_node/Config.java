package com.radardisc.boilerpipe_node;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {

	private static File propertiesFile = new File("config.properties");
	private static FileConfiguration _configuration;
		
	public static void load(){
		
		try {
			_configuration = new PropertiesConfiguration(propertiesFile);
			_configuration.load();
		} catch (ConfigurationException e) {
			
			e.printStackTrace();
		}	
	}
	
	private final static  String KEY_SNAME="sname";
	private final static  String KEY_SETCOOKIE="setcookie";
	private final static  String KEY_NUM_PROCESSORS="num_processors";
	private final static  String KEY_PROCESSING_SERVER_NAME="processing_server_name";
	private final static  String KEY_LANGUAGE_PROFILE_DIR="language_detector_profile_dir";
	private final static  String KEY_FILTER_NUMERICAL_DENSITY="filter_numerical_density";
	private final static  String KEY_LANGUAGE="language";
	private final static  String KEY_LANGUAGE_CERTAINTY="language_certainity";
	private final static  String KEY_STRIP_UNICODE="strip_unicode";

	public static String getNodeName() {
		return _configuration.getString(KEY_SNAME,"boilerpipe");
	}

	public static String getCookie() {
		return _configuration.getString(KEY_SETCOOKIE,"boilerpipe");
	}

	public static int getNumberOfProcessingThreads() {
		
		return _configuration.getInt(KEY_NUM_PROCESSORS, 20);
	}

	public static String getProcessingServerName() {
		
		return _configuration.getString(KEY_PROCESSING_SERVER_NAME,"boilerpipe");
	}

	public static String getLanguageDetectorProfileDir() {
		
		return _configuration.getString(KEY_LANGUAGE_PROFILE_DIR,".");
	}

	public static double getNumericalDensity() {
		
		//The numerical density figure is uses when calculating if a string has too many numbers E.g. a date 
		//A value of 1.01 means that a string of all number is acceptable
		return _configuration.getDouble(KEY_FILTER_NUMERICAL_DENSITY, 1.01); 
	}

	public static String getLanguage() {
		return _configuration.getString(KEY_LANGUAGE,"en");
	}

	public static double getLanguageCertainty() {
		
		return _configuration.getDouble(KEY_LANGUAGE_CERTAINTY,0.95);
	}

	public static boolean stripUnicode() {
		
		return _configuration.getBoolean(KEY_STRIP_UNICODE,false);
	}
	
	
	
	
	
	
}
