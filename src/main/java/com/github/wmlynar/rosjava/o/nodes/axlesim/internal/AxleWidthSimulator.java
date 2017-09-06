package com.github.wmlynar.rosjava.o.nodes.axlesim.internal;

import java.util.Random;

public class AxleWidthSimulator {
    double time = 0;
    double maximalTimeStep = 1;
    double x = 0;
    double y = 0;
    double speed = 0;
    double angle = 0;
    double rotation = 0;
    double rotationNoise = 0;
    double accelerationNoise = 0;
    double distanceLeft = 0;
    double distanceRight = 0;
    double width = 0.2;

    Random random = new Random(0);

	public void setAxleWidth(double d) {
		width = d;
	}
    
    public void setRotationNoise(double n) {
        rotationNoise = n;
    }

    public void setSpeed(double s) {
        speed = s;
    }

    public void setAccelerationNoise(double n) {
        accelerationNoise = n;
    }

    public void setTimeStep(double d) {
        maximalTimeStep = d;
    }

    public void simulate(double t) {
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
        double acceleration = accelerationNoise * (random.nextDouble() - 0.5);
        double drotation = rotationNoise * (random.nextDouble() - 0.5);
        speed += acceleration * dt;
        angle += rotation * dt;
        rotation += drotation * dt;
        x += speed * Math.cos(angle) * dt;
        y += speed * Math.sin(angle) * dt;
        distanceLeft += speed * dt + width * rotation * dt;
        distanceRight += speed * dt - width * rotation * dt;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDistanceLeft() {
        return distanceLeft;
    }

    public double getDistanceRight() {
        return distanceRight;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAngle() {
        return angle;
    }

    public double getRotation() {
        return rotation;
    }

}
