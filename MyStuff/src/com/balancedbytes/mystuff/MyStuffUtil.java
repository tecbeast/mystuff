package com.balancedbytes.mystuff;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class MyStuffUtil {
	
	private static SimpleDateFormat _TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

	public static int parseInt(String text) {
		if ((text == null) || (text.length() == 0)) {
			return 0;
		}
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException nfE) {
			return 0;
		}
	}

	public static boolean isProvided(String text) {
		return ((text != null) && (text.length() > 0));
	}
	
	public static String toString(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		return _TIMESTAMP_FORMAT.format(timestamp);
	}
	
	public static Timestamp toTimestamp(String source) {
		if (!isProvided(source)) {
			return null;
		}
		try {
			return new Timestamp(_TIMESTAMP_FORMAT.parse(source).getTime());
		} catch (ParseException pE) {
			return null;
		}
	}
	
}
