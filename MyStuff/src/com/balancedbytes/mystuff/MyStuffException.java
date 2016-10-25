package com.balancedbytes.mystuff;

@SuppressWarnings("serial")
public class MyStuffException extends RuntimeException {

	public MyStuffException() {
		super();
	}

	public MyStuffException(String message) {
		super(message);
	}

	public MyStuffException(Throwable cause) {
		super(cause);
	}

	public MyStuffException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyStuffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
