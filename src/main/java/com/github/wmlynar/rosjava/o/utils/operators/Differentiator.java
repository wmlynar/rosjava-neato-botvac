package com.github.wmlynar.rosjava.o.utils.operators;

public class Differentiator {
	
	private double prevValue = 0;
	private double prevTime = 0;
	private double minTimeDifference = 0;
	private boolean isInitialized = false;
	private boolean isTimeOk = false;

	public Differentiator(double minTimeDifference) {
		this.minTimeDifference = minTimeDifference;
	}

	public double differentiate(double time, double value) {
		if(!isInitialized) {
			prevValue = value;
			prevTime = time;
			isInitialized = true;
			return 0;
		}
		double dt = time - prevTime;
		if(dt<minTimeDifference) {
			isTimeOk = false;
			return 0;
		} else {
			isTimeOk = true;
		}
		double diff = (value-prevValue)/dt;
		prevValue = value;
		prevTime = time;
		return diff;
	}
	
	public boolean isOk() {
		return isInitialized && isTimeOk;
	}

}
