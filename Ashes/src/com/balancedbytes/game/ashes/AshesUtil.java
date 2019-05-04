package com.balancedbytes.game.ashes;

public final class AshesUtil {

	public static String toString(Object obj) {
		return (obj != null) ? obj.toString() : null;
	}
	
	public static boolean isProvided(String str) {
		return (str != null) && (str.length() > 0);
	}
	
}
