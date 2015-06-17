package com.radardisc.boilerpipe_node;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.OtpMbox;

public class ProcessingMailbox {
	private OtpMbox _mbox;
	private ExecutorService _executorService=Executors.newFixedThreadPool(Config.getNumberOfProcessingThreads(), new ProcessingThreadFactory());
	
	public ProcessingMailbox(String name){
		_mbox= Node.createMBox(name);
		
	}
	
	public void listen(){
		Log.info("Mailbox listening: %s", _mbox.getName());
		while(true){
			try {
				Log.info("Polling message box");
				OtpErlangObject message=_mbox.receive();

				Log.info("Received message: %s", message);
				if( message instanceof OtpErlangTuple ){
					OtpErlangTuple msg = (OtpErlangTuple)message;
				
					OtpErlangPid from = (OtpErlangPid) msg.elementAt(0);
					OtpErlangString filePath = (OtpErlangString) msg.elementAt(1);
				
					_executorService.execute(new ProcessingRunnable(filePath.stringValue(), from));

					
				}
			} catch (OtpErlangExit e) {
				Log.exception(e, "Mailbox exit exception" );
			} catch (OtpErlangDecodeException e) {
				Log.exception(e, "Mailbox decode exception" );
			}
			
		}
	}

	public void shutdown() {
		
		_executorService.shutdown();
		
	}
	
	
}
