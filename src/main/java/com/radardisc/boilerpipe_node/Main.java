package com.radardisc.boilerpipe_node;




public class Main {

		private static ProcessingMailbox _mailBox;
	public static class ShutdownRunnable implements Runnable {

		public void run() {
			_mailBox.shutdown();
		}

	}

public static void main(String[] args){
		
		Log.init();
		
		Config.load();
		
		LanguageDetector.init(Config.getLanguageDetectorProfileDir());
		Node.start();
		
		
		Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownRunnable()));
		
		_mailBox = new ProcessingMailbox(Config.getProcessingServerName());
		
		_mailBox.listen();
		
	}
}
