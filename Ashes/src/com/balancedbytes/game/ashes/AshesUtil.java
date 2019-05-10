package com.balancedbytes.game.ashes;

public final class AshesUtil {

	public static String toString(Object obj) {
		return (obj != null) ? obj.toString() : "";
	}
	
	public static boolean isProvided(String str) {
		return (str != null) && (str.length() > 0);
	}
	
	public static String leftpad(String text, int length) {
	    return String.format("%" + length + "." + length + "s", text);
	}
	
	public static String rightpad(String text, int length) {
	    return String.format("%-" + length + "." + length + "s", text);
	}
	
}
