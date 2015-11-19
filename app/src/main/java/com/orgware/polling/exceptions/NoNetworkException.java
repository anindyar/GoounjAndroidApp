package com.orgware.polling.exceptions;

/*
 * Author: Rajeshwaran Sethuraman
 */


public class NoNetworkException extends Exception{

	private static final long serialVersionUID = 1L;

	public NoNetworkException() {
		super("Check your network settings");
	}
	
}
