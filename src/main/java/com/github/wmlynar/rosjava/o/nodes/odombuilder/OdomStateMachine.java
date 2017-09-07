package com.github.wmlynar.rosjava.o.nodes.odombuilder;

import java.util.Random;

import org.ros.message.Time;

import com.github.wmlynar.rosjava.kf.KalmanFilter;
import com.github.wmlynar.rosjava.o.messages.Dist;
import com.github.wmlynar.rosjava.o.messages.Odom;
import com.github.wmlynar.rosjava.o.nodes.axlefilter.internal.AxleWidthModel;
import com.github.wmlynar.rosjava.o.nodes.axlefilter.internal.AxleWidthRotationObservation;
import com.github.wmlynar.rosjava.o.nodes.axlefilter.internal.AxleWidthVelocityLeftObservation;
import com.github.wmlynar.rosjava.o.utils.operators.Difference;
import com.github.wmlynar.rosjava.o.utils.operators.Differentiator;
import com.github.wmlynar.rosjava.plots.Plots;

public class OdomStateMachine {
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

	public OdomStateMachine() {
	}

	public void processDist(Dist dist) {
		double forward = (dist.left + dist.right) / 2;
	}
}
