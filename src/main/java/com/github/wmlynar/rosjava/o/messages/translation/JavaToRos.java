package com.github.wmlynar.rosjava.o.messages.translation;

import org.ros.internal.message.DefaultMessageFactory;
import org.ros.internal.message.definition.MessageDefinitionReflectionProvider;
import org.ros.message.MessageDefinitionProvider;
import org.ros.message.MessageFactory;
import org.ros.message.Time;

import com.github.wmlynar.rosjava.o.messages.Dist;
import com.github.wmlynar.rosjava.o.messages.Odom;

import geometry_msgs.Quaternion;
import geometry_msgs.Vector3Stamped;
import nav_msgs.Odometry;

public class JavaToRos {

    private static MessageDefinitionProvider messageDefinitionProvider = new MessageDefinitionReflectionProvider();
    private static MessageFactory messageFactory = new DefaultMessageFactory(messageDefinitionProvider);

    public static Quaternion newQuaternionFromYaw(double yaw) {
        Quaternion m = messageFactory.newFromType(Quaternion._TYPE);
        m.setZ(Math.sin(yaw / 2.));
        m.setW(Math.cos(yaw / 2.));
        return m;
    }

    public static Odometry newOdomMessage(Odom o) {
        Odometry m = messageFactory.newFromType(Odometry._TYPE);
        m.getHeader().setStamp(o.time);
        m.getPose().getPose().getPosition().setX(o.x);
        m.getPose().getPose().getPosition().setY(o.y);
        m.getPose().getPose().setOrientation(newQuaternionFromYaw(o.yaw));
        m.getTwist().getTwist().getLinear().setX(o.linear);
        m.getTwist().getTwist().getAngular().setZ(o.angular);
        return m;
    }

    public static Odometry newOdomMessage(Time time, double x, double y, double speed, double angle, double rotation) {
        Odometry m = messageFactory.newFromType(Odometry._TYPE);
        m.getHeader().setStamp(time);
        m.getPose().getPose().getPosition().setX(x);
        m.getPose().getPose().getPosition().setY(y);
        m.getPose().getPose().setOrientation(newQuaternionFromYaw(angle));
        m.getTwist().getTwist().getLinear().setX(speed);
        m.getTwist().getTwist().getAngular().setZ(rotation);
        return m;
    }

    public static Vector3Stamped newDistMessage(Time time, double left, double right) {
        Vector3Stamped m = messageFactory.newFromType(Vector3Stamped._TYPE);
        m.getHeader().setStamp(time);
        m.getVector().setX(left);
        m.getVector().setY(right);
        return m;
    }
    
    public static Vector3Stamped newDistMessage(Dist dist) {
        Vector3Stamped m = messageFactory.newFromType(Vector3Stamped._TYPE);
        m.getHeader().setStamp(dist.time);
        m.getVector().setX(dist.left);
        m.getVector().setY(dist.right);
        return m;
    }
}
