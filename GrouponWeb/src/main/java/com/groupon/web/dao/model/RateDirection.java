package com.groupon.web.dao.model;

public enum RateDirection {
	UP(1), DOWN(-1);

	private int incrementValue;
	
	private RateDirection(int value) {
		incrementValue = value;
	}

	public int getIncrementValue() {
		return incrementValue;
	}

	public void setIncrementValue(int incrementValue) {
		this.incrementValue = incrementValue;
	}
}
