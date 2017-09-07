package com.github.wmlynar.rosjava.logging;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

import org.ros.concurrent.Rate;
import org.ros.concurrent.WallTimeRate;
import org.ros.internal.message.Message;
import org.ros.message.MessageListener;
import org.ros.message.Time;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

public class TimeSequencer<T extends Message> {

    private Object lock = new Object();
    private long windowNanoseconds;
    private PriorityBlockingQueue<MessageWithTime> queue = new PriorityBlockingQueue<>(50,
            new Comparator<MessageWithTime>() {

                @Override
                public int compare(MessageWithTime m1, MessageWithTime m2) {
                    return m1.time.compareTo(m2.time);
                }
            });
    private Thread thread;
    private Rate rate;

    public TimeSequencer(double windowSeconds, double periodSeconds, ConnectedNode node) {
        this.rate = new WallTimeRate((int) (1 / periodSeconds));
        this.windowNanoseconds = (long) (windowSeconds * 1000000000L);
        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (lock) {
                        Time currentTime = node.getCurrentTime();
                        while (true) {
                            MessageWithTime message = queue.peek();
                            if (message == null) {
                                break;
                            }
                            if (currentTime.totalNsecs() > message.time.totalNsecs() + windowNanoseconds) {
                                queue.poll();
                                message.messageListener.onNewMessage(message.message);
                            } else {
                                break;
                            }
                        }
                    }
                    rate.sleep();
                }

            }
        });
        this.thread.start();
    }

    public void addSubscriber(Subscriber<T> subscriber, MessageListener<T> messageListener, int queueSize) {
        subscriber.addMessageListener(new MessageListener<T>() {
            @Override
            public void onNewMessage(T message) {
                Time time = message.toRawMessage().getMessage("header").toRawMessage().getTime("stamp");
                queue.add(new MessageWithTime(time, message, messageListener));
            }
        }, queueSize);
    }

}
