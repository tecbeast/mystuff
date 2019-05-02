package com.balancedbytes.game.ashes;

import java.security.SecureRandom;

public final class TurnTokenGenerator {
	
	private static final char[] SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final int LENGTH = 32;

	/**
	 * Generate the next secure random token in the series.
	 */
	public static String generateToken() {
		char[] buf = new char[LENGTH];
		for (int i = 0; i < buf.length; i++) {
	        buf[i] = SYMBOLS[RANDOM.nextInt(SYMBOLS.length)];
		}
	    return new String(buf);
	}

	public static void main(String[] args) {
		System.out.println(generateToken());
	}	
	
}
