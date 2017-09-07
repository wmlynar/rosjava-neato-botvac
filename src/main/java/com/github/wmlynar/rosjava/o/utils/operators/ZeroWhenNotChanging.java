package com.github.wmlynar.rosjava.o.utils.operators;

public class ZeroWhenNotChanging {
	
	private double baseValue = 0;
	private double newValue = 0;
	private int newValueCount = 0;
	private boolean isInitialized = false;
	private int samples;
	private double epsilon;

	public ZeroWhenNotChanging(int samples, double epsilon) {
		this.samples = samples;
		this.epsilon = epsilon;
	}
	
	public double process(double value) {
		if(!isInitialized) {
			baseValue = value;
			newValue = value;
			newValueCount = 0;
			isInitialized = true;
			return 0;
		}
		if(Math.abs(value- newValue) < epsilon) {
			newValueCount ++;
		} else {
			newValue = value;
			newValueCount = 0;
		}
		if(newValueCount>samples) {
			baseValue = newValue;
		}
		
		return value - baseValue;
	}
	
	public boolean isOk() {
		return isInitialized;
	}

	public void zero() {
		isInitialized = false;
	}

}
