package com.github.wmlynar.rosjava.o.nodes.axlefilter.internal;

import com.github.wmlynar.rosjava.kf.KalmanFilter;
import com.github.wmlynar.rosjava.o.utils.operators.Differentiator;
import com.github.wmlynar.rosjava.plots.Plots;

public class Main {
	
	public static void main(String[] args) {
		
		Differentiator diff = new Differentiator(1e-6);
		
		AxleWidthSimulator simulator = new AxleWidthSimulator();
		simulator.setAxleWidth(0.25);
        simulator.setRotationNoise(.1);
        simulator.setSpeed(1);
        simulator.setAccelerationNoise(1);
        simulator.setTimeStep(0.1);
        
		AxleWidthModel model = new AxleWidthModel();
		AxleWidthRotationObservation rotationObservation = new AxleWidthRotationObservation();
		AxleWidthVelocityLeftObservation leftObservation = new AxleWidthVelocityLeftObservation();
		KalmanFilter filter = new KalmanFilter(model);
		
        double[][] x = model.getState();
        x[AxleWidthModel.X][0] = 0;
        x[AxleWidthModel.Y][0] = 0;
        x[AxleWidthModel.S][0] = 1;
        x[AxleWidthModel.A][0] = 0;
        x[AxleWidthModel.ROT][0] = 0;
        x[AxleWidthModel.WIDTH][0] = 0.1235; // initial value with error

        double[][] cov = model.getCovariance();
        cov[AxleWidthModel.X][AxleWidthModel.X] = 1e-10;
        cov[AxleWidthModel.Y][AxleWidthModel.Y] = 1e-10;
        cov[AxleWidthModel.S][AxleWidthModel.S] = 1e-4;
        cov[AxleWidthModel.A][AxleWidthModel.A] = 1e-4;
        cov[AxleWidthModel.ROT][AxleWidthModel.ROT] = 1e-4;
        cov[AxleWidthModel.WIDTH][AxleWidthModel.WIDTH] = 1e-2;

        cov = model.getProcessNoiseCovariance();
        cov[AxleWidthModel.X][AxleWidthModel.X] = 1e-10;
        cov[AxleWidthModel.Y][AxleWidthModel.Y] = 1e-10;
        cov[AxleWidthModel.S][AxleWidthModel.S] = 1e-10;
        cov[AxleWidthModel.A][AxleWidthModel.A] = 1e-2;
        cov[AxleWidthModel.ROT][AxleWidthModel.ROT] = 1e-2;
        cov[AxleWidthModel.WIDTH][AxleWidthModel.WIDTH] = 1e-10;
        
        cov = rotationObservation.getObservationNoiseCovariance();
        cov[0][0] = 1e-6;

        cov = leftObservation.getObservationNoiseCovariance();
        cov[0][0] = 1e-6;

        for (double d = 0; d < 100; d += 1) {
            simulator.simulate(d);
            rotationObservation.rotation = simulator.getRotation();
            filter.update(d, rotationObservation);
            leftObservation.velocityLeft = diff.differentiate(d, (simulator.getDistanceRight() - simulator.getDistanceLeft())/2);
            filter.update(d, leftObservation);

            Plots.plotXy("position", "sim", simulator.getX(), simulator.getY());
            Plots.plotXTime("distances", "sim left", d, simulator.getDistanceLeft());
            Plots.plotXTime("distances", "sim right", d, simulator.getDistanceRight());
            Plots.plotXTime("speed", "sim", d, simulator.getSpeed());
            Plots.plotXTime("speed", "filter", d, model.getSpeed());
            Plots.plotXTime("angle", "sim", d, simulator.getAngle());
            Plots.plotXTime("angle", "filter", d, model.getAngle());
            Plots.plotXTime("rotation", "sim", d, simulator.getRotation());
            Plots.plotXTime("rotation", "filter", d, model.getRotation());
            
            Plots.plotXTime("width", "filter", d, model.getWidth());
        }
		
	}

}
