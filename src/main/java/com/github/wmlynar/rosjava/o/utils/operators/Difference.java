package com.github.wmlynar.rosjava.o.utils.operators;

public class Difference {
	
	private double prevValue = 0;
	private boolean isInitialized = false;

	public double difference( double value) {
		if(!isInitialized) {
			prevValue = value;
			isInitialized = true;
			return 0;
		}
		double diff = value-prevValue;
		prevValue = value;
		return diff;
	}
	
	public boolean isOk() {
		return isInitialized;
	}

}
