package com.github.wmlynar.rosjava.o.utils.operators;

public class Summator {
	
	private double sum = 0;

	public double sum( double value) {
		sum+=value;
		return sum;
	}
}
