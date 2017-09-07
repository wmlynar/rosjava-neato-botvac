package com.github.wmlynar.rosjava.o.utils.operators;

public class Integrator {
	
	private double prevValue = 0;
	private double prevTime = 0;
	private boolean isInitialized = false;
	private double sum = 0;

	public double integrate(double time, double value) {
		if(!isInitialized) {
			prevValue = value;
			prevTime = time;
			isInitialized = true;
			return 0;
		}
		double dt = time - prevTime;
		double increment = (value+prevValue)/2*dt;
		sum += increment;
		prevValue = value;
		prevTime = time;
		return sum;
	}
	
	public boolean isOk() {
		return isInitialized;
	}

}
