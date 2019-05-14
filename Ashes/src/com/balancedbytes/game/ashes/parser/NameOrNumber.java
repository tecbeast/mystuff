package com.balancedbytes.game.ashes.parser;

public class NameOrNumber {
	
	private int fNumber;
	private String fName;
	
	public NameOrNumber(int number) {
		fNumber = number;
	}
	
	public NameOrNumber(String name) {
		fName = name;
	}
	
	public int getNumber() {
		return fNumber;
	}
	
	public String getName() {
		return fName;
	}
	
	@Override
	public String toString() {
		return (fName != null) ? fName : Integer.toString(fNumber);
	}

}
