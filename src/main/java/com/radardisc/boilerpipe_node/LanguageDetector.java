package com.radardisc.boilerpipe_node;

import java.util.ArrayList;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;

public class LanguageDetector {
	
	private static boolean _enabled=false;
	public static boolean isEnabled(){  return _enabled; }
	
	public static void init(String dir){
		try {
			DetectorFactory.loadProfile(dir);
			Log.info("Detector factory loaded with %d profiles", DetectorFactory.getLangList().size() );
			_enabled=true;
		} catch (LangDetectException e) {
			Log.exception(e, "Exception thrown loading language profiles from dir: %s", dir);
		}
	}
	public static ArrayList<Language> detectLanguages(String text){
		try {
			Detector detector = DetectorFactory.create();
			detector.append(text);
			return detector.getProbabilities();
		} catch (LangDetectException e) {
			Log.exception(e, "Exception thrown detecting language for string: %s", text);
			
		}
		return null;
		
	}

	public static void printLanguages(String string) {
		
		ArrayList<Language> langs = detectLanguages(string);
		
		if( langs == null){
			Log.info("No languge features in text: %s", string);
			return;
		}
		StringBuilder langStr = new StringBuilder();
		langStr.append(string);
		langStr.append(": ");
		for( int i=0; i < langs.size(); ++i){
			langStr.append(langs.get(i));
		}
		
		Log.info(langStr.toString());
		
	}
	public static boolean isLanguage(String string, String language, double threshold) {
		
		ArrayList<Language> langs = detectLanguages(string);
		
		if( langs ==null || langs.isEmpty()){
			return false;
		}
		for( int i=0; i < langs.size(); ++i){
			Language test=langs.get(i);
			if(test.lang.equals(language) ){
				return test.prob >= threshold; 
			}
		}
		return false;
	}
}
