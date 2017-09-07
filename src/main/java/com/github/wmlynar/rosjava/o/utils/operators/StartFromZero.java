package com.github.wmlynar.rosjava.o.utils.operators;

public class StartFromZero {
	
	private double firstValue = 0;
	private boolean isInitialized = false;

	public double process( double value) {
		if(!isInitialized) {
			firstValue = value;
			isInitialized = true;
			return 0;
		}
		return value - firstValue;
	}
	
	public boolean isOk() {
		return isInitialized;
	}

	public void zero() {
		isInitialized = false;
	}

}
