package com.balancedbytes.mystuff;

public final class MyStuffUtil {

	public static long parseLong(String text) {
		if ((text == null) || (text.length() == 0)) {
			return 0;
		}
		try {
			return Long.parseLong(text);
		} catch (NumberFormatException nfE) {
			return 0;
		}
	}
	
	public static boolean isProvided(String text) {
		return ((text != null) && (text.length() > 0));
	}

}
