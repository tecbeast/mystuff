package com.balancedbytes.mystuff;

import java.util.SortedMap;

public abstract class RestDataFilter {
	
	public abstract boolean isEmpty();

	public abstract SortedMap<String, Object> toSortedMap();
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		SortedMap<String, Object> valueMap = toSortedMap();
		for (String key : valueMap.keySet()) {
			addSeparator(builder);
			builder.append(key).append("=").append(valueMap.get(key));
		}
		return builder.toString();
	}
	
	private void addSeparator(StringBuilder builder) {
		if ((builder != null) && (builder.length() > 0)) {
			builder.append(",");
		}
	}

}
