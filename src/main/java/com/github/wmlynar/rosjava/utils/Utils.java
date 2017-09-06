package com.github.wmlynar.rosjava.utils;

import geometry_msgs.Quaternion;

public class Utils {

    public static int getIndexOfNearest(float[] ranges, float minRange, float maxRange, int otherwise) {
        int angle = otherwise;
        float distance = Float.MAX_VALUE;
        for (int i = 0; i < ranges.length; i++) {
            if (ranges[i] < distance && ranges[i] > minRange && ranges[i] < maxRange) {
                distance = ranges[i];
                angle = i;
            }
        }
        return angle;
    }

    public static double fromQuaternionToYaw(Quaternion q) {
        double siny = +2.0 * (q.getW() * q.getZ() + q.getX() * q.getY());
        double cosy = +1.0 - 2.0 * (q.getY() * q.getY() + q.getZ() * q.getZ());
        return Math.atan2(siny, cosy);
    }
}
