package com.balancedbytes.game.ashes.parser;

public class ParserException extends RuntimeException {

	private static final long serialVersionUID = 3881578931271528300L;

	public ParserException() {
		super();
	}

	public ParserException(String message) {
		super(message);
	}

	public ParserException(Throwable cause) {
		super(cause);
	}

	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
