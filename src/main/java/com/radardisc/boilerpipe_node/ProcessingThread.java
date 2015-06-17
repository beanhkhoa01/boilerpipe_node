package com.radardisc.boilerpipe_node;

import com.ericsson.otp.erlang.OtpMbox;

public class ProcessingThread extends Thread {
	
	OtpMbox mbox = Node.createMBox();
	public ProcessingThread(Runnable r){
		super(r);
	}
	public OtpMbox getMbox() {
		
		return mbox;
	}
	
	
}
