package com.balancedbytes.game.ashes.json;

public class JsonSerializationException extends RuntimeException {

	private static final long serialVersionUID = 3881578931271528300L;

	public JsonSerializationException() {
		super();
	}

	public JsonSerializationException(String message) {
		super(message);
	}

	public JsonSerializationException(Throwable cause) {
		super(cause);
	}

	public JsonSerializationException(String message, Throwable cause) {
		super(message, cause);
	}

}
