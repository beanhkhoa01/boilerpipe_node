package com.radardisc.boilerpipe_node;

import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;

public class Node {
	
	private static OtpNode _node;
	private static String _nodeName;
	private static String _cookie;
	public static void  start() {
		try {
			_nodeName=Config.getNodeName();
			_cookie=Config.getCookie();
			_node = new OtpNode(Config.getNodeName(), _cookie);
			Log.info("Node started: sname %s cookie %s", _nodeName, _cookie);
		}
		catch( Exception e){
			Log.exception(e,"Failed to start otp_node");
		}
	}

	public static OtpMbox createMBox(String name) {
		return _node.createMbox(name);
	}

	public static OtpMbox createMBox() {
		
		return _node.createMbox();
	}
}
