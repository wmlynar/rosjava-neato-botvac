package com.github.wmlynar.rosjava.utils;

public class ScanMessageInterpreter {

    public double processScanToAngle(float[] ranges) {
    	double a = (359.0 - Utils.getIndexOfNearest(ranges, 0.3f, 10, -1)) * Math.PI / 180.0;
    	if(a>Math.PI) {
    		a-=2*Math.PI;
    	}
        return a;
    }

    public double processScanToDistance(float[] ranges) {
        return ranges[180];
    }

}
