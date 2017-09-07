package com.github.wmlynar.rosjava.o.nodes.axlefilter;

import org.ros.message.Duration;
import org.ros.message.Time;

import com.github.wmlynar.rosjava.kf.KalmanFilter;
import com.github.wmlynar.rosjava.o.messages.Dist;
import com.github.wmlynar.rosjava.o.messages.Odom;
import com.github.wmlynar.rosjava.o.nodes.axlefilter.internal.AxleWidthModel;
import com.github.wmlynar.rosjava.o.nodes.axlefilter.internal.AxleWidthRotationObservation;
import com.github.wmlynar.rosjava.o.nodes.axlefilter.internal.AxleWidthVelocityLeftObservation;
import com.github.wmlynar.rosjava.o.nodes.axlefilter.internal.Differentiator;
import com.github.wmlynar.rosjava.plots.Plots;

public class AxleFilterStateMachine {
	
	private KalmanFilter filter;
	private AxleWidthModel model;
	private AxleWidthRotationObservation rotationObservation;
	private AxleWidthVelocityLeftObservation leftObservation;
	private Time time;
	private Differentiator diff;
	private double prevT = -1;
	private double prevDiff;

	public AxleFilterStateMachine() {
		model = new AxleWidthModel();
		rotationObservation = new AxleWidthRotationObservation();
		leftObservation = new AxleWidthVelocityLeftObservation();
		filter = new KalmanFilter(model);
		
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
        cov[AxleWidthModel.S][AxleWidthModel.S] = 1e-2;
        cov[AxleWidthModel.A][AxleWidthModel.A] = 1e-10;
        cov[AxleWidthModel.ROT][AxleWidthModel.ROT] = 1e-2;
        cov[AxleWidthModel.WIDTH][AxleWidthModel.WIDTH] = 1e-2;

        cov = model.getProcessNoiseCovariance();
        cov[AxleWidthModel.X][AxleWidthModel.X] = 1e-8;
        cov[AxleWidthModel.Y][AxleWidthModel.Y] = 1e-8;
        cov[AxleWidthModel.S][AxleWidthModel.S] = 1e-8;
        cov[AxleWidthModel.A][AxleWidthModel.A] = 1e-2;
        cov[AxleWidthModel.ROT][AxleWidthModel.ROT] = 1e-2;
        cov[AxleWidthModel.WIDTH][AxleWidthModel.WIDTH] = 1e-8;
        
        cov = rotationObservation.getObservationNoiseCovariance();
        cov[0][0] = 1e-6;

        cov = leftObservation.getObservationNoiseCovariance();
        cov[0][0] = 1e-6;
        
		diff = new Differentiator();
		
	}
	
	public void processOdom(Odom odom) {
		double t = getFilterTime(odom.time);
		
        rotationObservation.rotation = odom.angular;
        filter.update(t, rotationObservation);
    	
        Plots.plotXTime("angle", "odom", t, odom.yaw);
        Plots.plotXTime("rotation", "odom", t, odom.angular);
        
        Plots.plotXTime("angle", "filter", t, model.getAngle());
        Plots.plotXTime("rotation", "filter", t, model.getRotation());
        
        Plots.plotXTime("width", "filter", t, model.getWidth());
	}

	public void processDist(Dist dist) {
		double t = getFilterTime(dist.time);
		double diff = (dist.right - dist.left) / 2;
		if(prevT == -1) {
			prevT = t;
			prevDiff = diff;
			return;
		}
		double ddiff =  diff - prevDiff;
		double dt = t - prevT;

		if(dt<1e-6) {
			return;
		}
		
		prevDiff = diff;
		prevT = t;

        leftObservation.velocityLeft = ddiff/dt;
        filter.update(t, leftObservation);
        Plots.plotXTime("left", "dist", t, leftObservation.velocityLeft);
	}
	
	private double getFilterTime(Time time) {
		if (this.time==null) {
			this.time=time;
		}
		return time.toSeconds()-this.time.toSeconds(); 
	}


}
