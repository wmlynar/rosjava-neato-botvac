package com.github.wmlynar.rosjava.o.utils.operators;

public class AngleDifferentiator {
	
	private double prevValue = 0;
	private double prevTime = 0;
	private double minTimeDifference = 0;
	private boolean isInitialized = false;
	private boolean isTimeOk = false;

	public AngleDifferentiator(double minTimeDifference) {
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
		double delta = value-prevValue;
		while(delta>Math.PI) {
			delta-=Math.PI*2;
		}
		while(delta<-Math.PI) {
			delta+=Math.PI*2;
		}
		if(delta< -Math.PI || delta > Math.PI) {
			int i=0;
		}
		double diff = delta/dt;
		prevValue = value;
		prevTime = time;
		return diff;
	}
	
	public boolean isOk() {
		return isInitialized && isTimeOk;
	}

}
