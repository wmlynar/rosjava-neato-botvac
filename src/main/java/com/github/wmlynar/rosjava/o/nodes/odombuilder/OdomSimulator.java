package com.github.wmlynar.rosjava.o.nodes.odombuilder;

import java.util.Random;

public class OdomSimulator {
    public double time = 0;
    public double maximalTimeStep = .05;
    public double x = 0;
    public double y = 0;
    public double speed = 0;
    public double angle = 0;
    public double rotation = 0;
    
    private boolean isInitialized = false;

    Random random = new Random(0);

    public void setTimeStep(double d) {
        maximalTimeStep = d;
    }

    public void simulate(double t) {
    	if(!isInitialized) {
    		time = t;
    		isInitialized = true;
    	}
        do {
            double dt = t - time;
            if (dt > maximalTimeStep) {
                dt = maximalTimeStep;
            }
            simulateInterval(dt);
            time += dt;
        } while (time < t);
    }

    public void simulateInterval(double dt) {
        angle += rotation * dt;
        x += speed * Math.cos(angle) * dt;
        y += speed * Math.sin(angle) * dt;
    }
}
