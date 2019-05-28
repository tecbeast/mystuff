package com.balancedbytes.game.ashes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public final class AshesUtil {
	
	private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String print(Object obj) {
		return (obj != null) ? obj.toString() : "";
	}
	
	public static boolean provided(String str) {
		return (str != null) && (str.length() > 0);
	}

	public static boolean provided(Object[] array) {
		return (array != null) && (array.length > 0);
	}
	
	public static boolean provided(Collection<?> collection) {
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
		if (!provided(numberString)) {
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
	
	public static boolean isNumeric(String str) {
		if (!provided(str)) {
			return false;
		}
	    for (char c : str.toCharArray()) {
	        if (!Character.isDigit(c)) {
	        	return false;
	        }
	    }
	    return true;
	}

	public static String asTimestamp(Date date) {
		if (date == null) {
			return "";
		}
		return TIMESTAMP_FORMAT.format(date);
	}
	
	public static String join(Collection<?> collection, String separator) {
		if ((collection == null) || (separator == null)) {
			return null;
		}
		boolean first = true;
		StringBuilder result = new StringBuilder();
		for (Object element : collection) {
			if (first) {
				first = false;
			} else {
				result.append(separator);
			}
			result.append(print(element));
		}
		return result.toString();
	}
	
	public static List<Integer> splitIntegerList(String listString, String separator) {
		List<Integer> result = new ArrayList<Integer>();
		if ((listString == null) || (separator == null)) {
			return result;
		}
		String[] elements = listString.split(separator);
		for (String element : elements) {
			if (isNumeric(element)) {
				result.add(Integer.parseInt(element));
			} else {
				result.add(0);
			}
		}
		return result;
	}
	
}
