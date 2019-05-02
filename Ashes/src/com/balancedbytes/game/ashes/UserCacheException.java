package com.balancedbytes.game.ashes;

public class UserCacheException extends RuntimeException {

	private static final long serialVersionUID = -4071598781301410262L;

	public UserCacheException() {
		super();
	}

	public UserCacheException(String message) {
		super(message);
	}

	public UserCacheException(Throwable cause) {
		super(cause);
	}

	public UserCacheException(String message, Throwable cause) {
		super(message, cause);
	}

}
