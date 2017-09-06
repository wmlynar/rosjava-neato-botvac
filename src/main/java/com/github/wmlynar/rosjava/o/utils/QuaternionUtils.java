package com.github.wmlynar.rosjava.o.utils;

import geometry_msgs.Quaternion;

public class QuaternionUtils {

    public static double fromQuaternionToYaw(Quaternion q) {
        double siny = +2.0 * (q.getW() * q.getZ() + q.getX() * q.getY());
        double cosy = +1.0 - 2.0 * (q.getY() * q.getY() + q.getZ() * q.getZ());
        return Math.atan2(siny, cosy);
    }
    
}
