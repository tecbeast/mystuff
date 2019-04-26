package com.balancedbytes.game.roborally.model;

public class Coordinate {
	
	public static int MAX_SIZE = 1000;
	
	private int fX;
	private int fY;
	
	public Coordinate() {
		this(0, 0);
	}
	
	public Coordinate(int x, int y) {
		fX = x;
		fY = y;
	}
	
	public Coordinate setX(int x) {
		fX = x;
		return this;
	}
	
	public int getX() {
		return fX;
	}
	
	public Coordinate setY(int y) {
		fY = y;
		return this;
	}
	
	public int getY() {
		return fY;
	}
	
	@Override
	public int hashCode() {
		return fY * MAX_SIZE + fX;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Coordinate other = (Coordinate) obj;
		if (fX != other.fX) {
			return false;
		}
		if (fY != other.fY) {
			return false;
		}
		return true;
	}

}
