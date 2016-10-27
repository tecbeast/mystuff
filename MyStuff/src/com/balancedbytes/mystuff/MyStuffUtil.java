package com.balancedbytes.mystuff;

import java.util.Map;

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
	
	public static UriBuilder setQueryParams(UriBuilder uriBuilder, Map<String, String> queryParams) {
		if (uriBuilder == null) {
			return null;
		}
		uriBuilder.replaceQuery(null);
		if ((queryParams != null) && (queryParams.size() > 0)) {
			for (String key : queryParams.keySet()) {
				uriBuilder.queryParam(key, queryParams.get(key));
			}
		}
		return uriBuilder;
	}

}
