package com.github.wmlynar.rosjava.o.messages.translation;

import com.github.wmlynar.rosjava.o.messages.Dist;
import com.github.wmlynar.rosjava.o.messages.Inertial;
import com.github.wmlynar.rosjava.o.messages.Odom;
import com.github.wmlynar.rosjava.o.messages.Scan;
import com.github.wmlynar.rosjava.o.utils.QuaternionUtils;

import geometry_msgs.Vector3Stamped;
import nav_msgs.Odometry;
import sensor_msgs.Imu;
import sensor_msgs.LaserScan;

public class RosToJava {

    public static Scan fromScan(LaserScan scan) {
    	Scan o = new Scan();
        o.time = scan.getHeader().getStamp();
        o.ranges = scan.getRanges();
        return o;
    }

    public static Odom fromOdom(Odometry odom) {
    	Odom o = new Odom();
        o.time = odom.getHeader().getStamp();
        o.x = odom.getPose().getPose().getPosition().getX();
        o.y = odom.getPose().getPose().getPosition().getY();
        o.yaw = QuaternionUtils.fromQuaternionToYaw(odom.getPose().getPose().getOrientation());
        o.linear = odom.getTwist().getTwist().getLinear().getX();
        o.angular = odom.getTwist().getTwist().getAngular().getZ();
        return o;
    }

    public static Dist fromDist(Vector3Stamped dist) {
    	Dist d = new Dist();
        d.time = dist.getHeader().getStamp();
        d.left = dist.getVector().getX();
        d.right = dist.getVector().getY();
        return d;
    }

	public static Inertial fromImu(Imu imu) {
		Inertial i = new Inertial();
        i.time = imu.getHeader().getStamp();
        i.angularYaw = imu.getAngularVelocity().getZ();
        return i;
	}
	
}
