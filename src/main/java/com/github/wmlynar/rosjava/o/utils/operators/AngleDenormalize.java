package com.github.wmlynar.rosjava.o.utils.operators;

public class AngleDenormalize {
	
	private double prevValue = 0;
	private boolean isInitialized = false;
	private double sum = 0;

	public double denormalize( double value) {
		
		if(!isInitialized) {
			prevValue = value;
			isInitialized = true;
			return 0;
		}
		
		double diff = value-prevValue;
		prevValue = value;
		while(diff>Math.PI) {
			diff -= 2*Math.PI;
		}
		while(diff<-Math.PI) {
			diff += 2*Math.PI;
		}
		sum+=diff;
		return sum;
	}
	
	public boolean isOk() {
		return isInitialized;
	}
	
}
