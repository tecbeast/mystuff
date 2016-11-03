package com.balancedbytes.mystuff;

import java.util.SortedMap;

import javax.ws.rs.core.UriBuilder;

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
	
	public static UriBuilder setQueryParams(UriBuilder uriBuilder, RestDataFilter filter) {
		if (uriBuilder == null) {
			return null;
		}
		uriBuilder.replaceQuery(null);
		if (filter != null) {
			SortedMap<String, Object> params = filter.toSortedMap();
			for (String key : params.keySet()) {
				uriBuilder.queryParam(key, params.get(key));
			}
		}
		return uriBuilder;
	}

}
