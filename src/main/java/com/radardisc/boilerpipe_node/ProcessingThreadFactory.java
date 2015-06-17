package com.radardisc.boilerpipe_node;

import java.util.concurrent.ThreadFactory;

public class ProcessingThreadFactory implements ThreadFactory {

	public Thread newThread(Runnable r) {
		
		return new ProcessingThread(r);
	}

}
