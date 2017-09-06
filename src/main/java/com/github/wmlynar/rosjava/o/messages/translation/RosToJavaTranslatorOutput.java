package com.github.wmlynar.rosjava.o.messages.translation;

public interface RosToJavaTranslatorOutput {

    void processScan(long n, float[] ranges);

    void processOdom(long n, double valueX, double valueY, double yaw, double linear, double angular);

    void processDist(long n, double valueX, double valueY);

	void processImu(long n, double angularYaw);

}