package com.radardisc.boilerpipe_node;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;


import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.OtpMbox;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class ProcessingRunnable implements Runnable {

	String _file;
	OtpErlangPid _replyTo;

	public ProcessingRunnable( String filePath, OtpErlangPid replyTo ){
		_file=filePath;
		_replyTo=replyTo;
	}
	
	public void run() {
		
		OtpMbox box = getMbox();
		
		readAndReply(_file,_replyTo,box);
			
		
		
	}
	
	public static String bpExtract(String file) throws FileNotFoundException, BoilerpipeProcessingException, URISyntaxException, MalformedURLException{
		
		URI u = new URI(file);
		
		if( u.getScheme() != null  ){
			
				return ArticleExtractor.INSTANCE.getText(u.toURL());
			
		} else {
			FileReader reader = new FileReader(file);
		
			return ArticleExtractor.INSTANCE.getText(reader);
		}
	}

	public static void readAndReply(String file, OtpErlangPid replyTo, OtpMbox mbox )  {

		try {
			
			
			String result = bpExtract(file);
			
			result = filterBPResult(result);
			OtpErlangAtom ok = new OtpErlangAtom("ok");
			OtpErlangString eString = new OtpErlangString(result);
			
			OtpErlangTuple response = new OtpErlangTuple(new OtpErlangObject[]{ ok, eString});
			mbox.send(replyTo, response);
		
		} catch (FileNotFoundException e) {
			Log.exception(e, "File not found: %s", file);
			sendNotFound(file, mbox, replyTo);
		} catch (BoilerpipeProcessingException e) {
			
			Log.exception(e, "Processing exception: %s", file);
			sendBoilerPipeException(file,e.getMessage(), mbox, replyTo);
		} catch (MalformedURLException e) {
			Log.exception(e, "Malformed URL exception: %s", file);
			sendInvalidUrl(file, e.getMessage(),mbox,replyTo);
		} catch (URISyntaxException e) {
			Log.exception(e, "Malformed URL exception: %s", file);
			sendInvalidUrl(file, e.getMessage(),mbox,replyTo);
			
		}
	}
	
	private static String filterBPResult(String result) {
		
		String[] lines = result.split("\n");
		
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i < lines.length; ++i ) {
			 String line = filterLine(lines[i]);
			 
			 
			 if( line == null || line.isEmpty() ){
				 
			 } else {
				 Log.info(line);
				 builder.append(line);
				 builder.append("\n");
			 }
		}
		
		return builder.toString();
	}

	private static String filterLine(String string) {
		
		if( numericalDensity(string) > Config.getNumericalDensity() ) {
			return null;
		}
		
		String language = Config.getLanguage(); 
		
		if(language == null ||  language.isEmpty() || !LanguageDetector.isLanguage(string, language , Config.getLanguageCertainty())){
			return null;
		}
		
		
		if( Config.stripUnicode() ){
			string = string.replaceAll("[^\\x00-\\x7F]", "");
			//Normalizer.normalize(string, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			return string;
			
		}
		
		return string;
	}

	private static double numericalDensity(String normalized) {
		
		if( normalized == null || normalized.isEmpty() ){
			return 0;
		}
		
		
		int nums=0;
		int whiteSpace=0;
		int len=normalized.length();
		
		for(int i = 0; i< len; ++i ){
			char c = normalized.charAt(i);
			if( Character.isDigit(c ) ){
				nums=nums+1;
			}
			if( Character.isWhitespace(c)){
				whiteSpace=whiteSpace+1;
			}
		}
		
		double density = (double)((double)nums/(len-whiteSpace));
		Log.info("Nums: %d Whitespace %d: Density: %f", nums, whiteSpace,density);
		return density;
	}

	private static void sendBoilerPipeException(String file, String message,
			OtpMbox mbox, OtpErlangPid replyTo) {
		sendExceptionResult("boilerpipe_exception",file, message, mbox,replyTo);
		
	}

	private static void sendInvalidUrl(String file, String message,
			OtpMbox mbox, OtpErlangPid replyTo) {
		sendExceptionResult("invalid_url",file, message, mbox,replyTo);
		
	}

	private static void sendExceptionResult(String descriptor, String file, String message, OtpMbox mbox,
			OtpErlangPid replyTo) {
		
		OtpErlangAtom bpException = new OtpErlangAtom(descriptor);
		OtpErlangString eString = new OtpErlangString(file);
		OtpErlangString eMessage = new OtpErlangString(message);

		OtpErlangTuple messageTuple = new OtpErlangTuple( new OtpErlangObject[]{
				eString,eMessage
		});
		sendError(mbox, replyTo, new OtpErlangObject[]{
				bpException,messageTuple
		} );
	}

	public static void sendError(OtpMbox mbox, OtpErlangPid replyTo, OtpErlangObject[] errorTuple) {

		OtpErlangAtom error = new OtpErlangAtom("error");
		
		OtpErlangTuple responseInfo = new OtpErlangTuple(errorTuple);
		OtpErlangTuple response = new OtpErlangTuple(new OtpErlangObject[]{ error, responseInfo });
		mbox.send(replyTo, response);
	}
	
	public static void sendNotFound(String path, OtpMbox mbox, OtpErlangPid replyTo) {

		Log.error("No file at path %s", path);
		
		OtpErlangAtom notFound = new OtpErlangAtom("not_found");
		OtpErlangString eString = new OtpErlangString(path);
		OtpErlangObject[] inner = new OtpErlangObject[]{
			notFound,eString
		};
		
		sendError(mbox,replyTo,inner);
	}

	private OtpMbox getMbox() {
		
		ProcessingThread thread= (ProcessingThread)Thread.currentThread();
		return thread.getMbox();
	}
	
}
