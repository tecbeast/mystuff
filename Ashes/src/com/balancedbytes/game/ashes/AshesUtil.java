package com.balancedbytes.game.ashes;

import java.util.Collection;

public final class AshesUtil {

	public static String toString(Object obj) {
		return (obj != null) ? obj.toString() : "";
	}
	
	public static boolean isProvided(String str) {
		return (str != null) && (str.length() > 0);
	}

	public static boolean isProvided(Object[] array) {
		return (array != null) && (array.length > 0);
	}
	
	public static boolean isProvided(Collection<?> collection) {
		return (collection != null) && (collection.size() > 0);
	}
	
	public static String leftpad(String text, int length) {
	    return String.format("%" + length + "." + length + "s", text);
	}
	
	public static String rightpad(String text, int length) {
	    return String.format("%-" + length + "." + length + "s", text);
	}
	
	public static String toStringWithLeadingZeroes(int number, int length) {
		String numberString = Integer.toString(number);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i >= numberString.length()) {
				result.append("0");
			}
		}
		result.append(numberString);
		return result.toString();
	}
	
	public static String stripLeadingZeroes(String numberString) {
		if (!isProvided(numberString)) {
			return numberString;
		}
		int start = 0;
		for (int i = 0; i < numberString.length(); i++) {
			if (numberString.charAt(i) != '0') {
				start = i;
				break;
			}
		}
		return numberString.substring(start);
	}
	
}
