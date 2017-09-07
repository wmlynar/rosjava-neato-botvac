package com.github.wmlynar.rosjava.logging;

import org.ros.internal.message.Message;
import org.ros.message.MessageListener;
import org.ros.message.Time;

import nav_msgs.Odometry;

public class MessageWithTime<T> {
    public Time time;
    public Message message;
    public MessageListener<T> messageListener;
	public int position;

    public MessageWithTime(Time time, Message message, MessageListener<T> messageListener) {
        this.time = time;
        this.position = 0;
        this.message = message;
        this.messageListener = messageListener;
    }

	public MessageWithTime(Time time, int position, Message message, MessageListener<T> messageListener) {
        this.time = time;
        this.position = position;
        this.message = message;
        this.messageListener = messageListener;
	}
}
