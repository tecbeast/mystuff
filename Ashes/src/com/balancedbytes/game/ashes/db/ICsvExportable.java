package com.balancedbytes.game.ashes.db;

public interface ICsvExportable {
	
	public static final String CSV_SEPARATOR = ";";

	public String toCsv();
	
}
