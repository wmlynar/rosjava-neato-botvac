package com.github.wmlynar.rosjava.o.messages;

import org.ros.message.Time;

public class Scan {

	public Time time;
	public float[] ranges;

	public Scan() {
	}
	
	public Scan(Time stamp, float[] ranges) {
		this.time = stamp;
		this.ranges = ranges;
	}

}
