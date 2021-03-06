package com.github.wmlynar.rosjava.o.nodes.axlefilter.internal;

import com.github.wmlynar.rosjava.kf.Observation;

public class AxleWidthVelocityLeftObservation extends Observation {
	
	public double velocityLeft;

	@Override
	public int observationDimension() {
		return 1;
	}

	@Override
	public int stateDimension() {
		// TODO Auto-generated method stub
		return 6;
	}

	@Override
	public void observationMeasurement(double[][] y) {
		y[0][0] = velocityLeft;
	}

	@Override
	public void observationModel(double[][] x, double[][] h) {
		h[0][0] = x[AxleWidthModel.ROT][0] * x[AxleWidthModel.WIDTH][0];
	}

	@Override
	public void observationModelJacobian(double[][] x, double[][] j) {
        j[0][AxleWidthModel.ROT] = x[AxleWidthModel.WIDTH][0];
        j[0][AxleWidthModel.WIDTH] = x[AxleWidthModel.ROT][0];
	}
}
