package com.github.wmlynar.rosjava.o.nodes.csvplayer.internal;

import org.ros.internal.message.DefaultMessageFactory;
import org.ros.internal.message.definition.MessageDefinitionReflectionProvider;
import org.ros.message.MessageDefinitionProvider;
import org.ros.message.MessageFactory;
import org.ros.message.Time;

import geometry_msgs.Quaternion;
import geometry_msgs.Vector3Stamped;
import nav_msgs.Odometry;
import sensor_msgs.Imu;
import sensor_msgs.LaserScan;

public class RosMessageFactory {

    private static MessageDefinitionProvider messageDefinitionProvider = new MessageDefinitionReflectionProvider();
    private static MessageFactory messageFactory = new DefaultMessageFactory(messageDefinitionProvider);

    public static Quaternion newQuaternionFromYaw(double yaw) {
        Quaternion m = messageFactory.newFromType(Quaternion._TYPE);
        m.setZ(Math.sin(yaw / 2.));
        m.setW(Math.cos(yaw / 2.));
        return m;
    }

    public static float[] newScansFromStrings(String[] strings) {
        float floats[] = new float[strings.length - 2];
        for (int i = 0; i < floats.length; i++) {
            floats[i] = Float.parseFloat(strings[i + 2]);
        }
        return floats;
    }

    public static Odometry newOdomMessage(String[] line) {
        Odometry m = messageFactory.newFromType(Odometry._TYPE);
        m.getHeader().setStamp(Time.fromNano(Long.parseLong(line[1])));
        m.getPose().getPose().getPosition().setX(Double.parseDouble(line[2]));
        m.getPose().getPose().getPosition().setY(Double.parseDouble(line[3]));
        m.getPose().getPose().setOrientation(newQuaternionFromYaw(Double.parseDouble(line[4])));
        m.getTwist().getTwist().getLinear().setX(Double.parseDouble(line[5]));
        m.getTwist().getTwist().getAngular().setZ(Double.parseDouble(line[6]));
        return m;
    }

    public static LaserScan newScanMessage(String[] line) {
        LaserScan m = messageFactory.newFromType(LaserScan._TYPE);
        m.getHeader().setStamp(Time.fromNano(Long.parseLong(line[1])));
        m.setRanges(newScansFromStrings(line));
        return m;
    }

    public static Vector3Stamped newDistMessage(String[] line) {
        Vector3Stamped m = messageFactory.newFromType(Vector3Stamped._TYPE);
        m.getHeader().setStamp(Time.fromNano(Long.parseLong(line[1])));
        m.getVector().setX(Double.parseDouble(line[2]));
        m.getVector().setY(Double.parseDouble(line[3]));
        return m;
    }

	public static Imu newImuMessage(String[] line) {
		Imu m = messageFactory.newFromType(Imu._TYPE);
        m.getHeader().setStamp(Time.fromNano(Long.parseLong(line[1])));
        m.getAngularVelocity().setZ(Double.parseDouble(line[2]));
        return m;
	}
}
