package com.balancedbytes.game.roborally.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 
 * @author TecBeast
 */
public final class DateUtil {
	
	private static SimpleDateFormat _TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String toString(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		return _TIMESTAMP_FORMAT.format(timestamp);
	}
	
	public static Timestamp toTimestamp(String source) {
		if (!StringUtil.isProvided(source)) {
			return null;
		}
		try {
			return new Timestamp(_TIMESTAMP_FORMAT.parse(source).getTime());
		} catch (ParseException pE) {
			return null;
		}
	}
	
}
