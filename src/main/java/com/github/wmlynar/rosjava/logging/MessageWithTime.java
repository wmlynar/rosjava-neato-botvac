package com.github.wmlynar.rosjava.logging;

import org.ros.internal.message.Message;
import org.ros.message.MessageListener;
import org.ros.message.Time;

public class MessageWithTime<T> {
    public Time time;
    public Message message;
    public MessageListener<T> messageListener;

    public MessageWithTime(Time time, Message message, MessageListener<T> messageListener) {
        this.time = time;
        this.message = message;
        this.messageListener = messageListener;
    }
}
