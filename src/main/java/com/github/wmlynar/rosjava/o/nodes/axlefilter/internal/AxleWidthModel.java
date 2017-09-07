package com.github.wmlynar.rosjava.o.nodes.axlefilter.internal;

import com.github.wmlynar.rosjava.kf.Model;

public class AxleWidthModel extends Model {

    public static int X = 0;
    public static int Y = 1;
    public static int S = 2;
    public static int A = 3;
    public static int ROT = 4;
    public static int WIDTH = 5;
    
	@Override
	public int stateDimension() {
		return 6;
	}

	@Override
	public void stateFunction(double[][] x, double[][] f) {
        f[AxleWidthModel.X][0] = x[AxleWidthModel.S][0] * Math.cos(x[AxleWidthModel.A][0]);
        f[AxleWidthModel.Y][0] = x[AxleWidthModel.S][0] * Math.sin(x[AxleWidthModel.A][0]);
        f[AxleWidthModel.A][0] = x[AxleWidthModel.ROT][0];
	}

	@Override
	public void stateFunctionJacobian(double[][] x, double[][] j) {
        j[AxleWidthModel.X][AxleWidthModel.S] = Math.cos(x[AxleWidthModel.A][0]);
        j[AxleWidthModel.X][AxleWidthModel.A] = -x[AxleWidthModel.S][0] * Math.sin(x[AxleWidthModel.A][0]);
        j[AxleWidthModel.Y][AxleWidthModel.S] = Math.sin(x[AxleWidthModel.A][0]);
        j[AxleWidthModel.Y][AxleWidthModel.A] = x[AxleWidthModel.S][0] * Math.cos(x[AxleWidthModel.A][0]);
        j[AxleWidthModel.A][AxleWidthModel.ROT] = 1;
	}

    public double getRotation() {
        return getState()[AxleWidthModel.ROT][0];
    }
    
    public double getSpeed() {
        return getState()[AxleWidthModel.S][0];
    }

    public double getAngle() {
        return getState()[AxleWidthModel.A][0];
    }

	public double getWidth() {
        return getState()[AxleWidthModel.WIDTH][0];
	}

}
