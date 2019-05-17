package com.balancedbytes.game.ashes;

public class AshesException extends RuntimeException {

	private static final long serialVersionUID = -4071598781301410262L;

	public AshesException() {
		super();
	}

	public AshesException(String message) {
		super(message);
	}

	public AshesException(Throwable cause) {
		super(cause);
	}

	public AshesException(String message, Throwable cause) {
		super(message, cause);
	}

}
