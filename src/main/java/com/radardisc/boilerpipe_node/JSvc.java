package com.radardisc.boilerpipe_node;


public class JSvc  {
	private  ProcessingMailbox _mailBox;
	
	public void init(String[] args){
		
		Log.init();
		
		Config.load();
		
		LanguageDetector.init(Config.getLanguageDetectorProfileDir());
		Log.info("Initialized via Jsvc");
	}
	
	public void start(){
		Node.start();

		_mailBox = new ProcessingMailbox(Config.getProcessingServerName());
		_mailBox.listen();
		
		Log.info("Started via Jsvc");
	}
	
	public void stop(){
		_mailBox.shutdown();
		Log.info("Stopped via Jsvc");
	}
	
	public void destroy(){
		Log.info("Destroyed via Jsvc");
	}
}
