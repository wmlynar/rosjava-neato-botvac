package com.github.wmlynar.rosjava.utils;

import org.ros.internal.node.topic.PublisherIdentifier;
import org.ros.node.topic.Subscriber;
import org.ros.node.topic.SubscriberListener;

public class CountUpSubscriberListener implements SubscriberListener {

    private CountUpLatch registrations;
    private CountUpLatch connections;

    public CountUpSubscriberListener(CountUpLatch registrations, CountUpLatch connections) {
        this.registrations = registrations;
        this.connections = connections;
    }

    @Override
    public void onMasterRegistrationFailure(Object arg0) {
    }

    @Override
    public void onMasterRegistrationSuccess(Object arg0) {
        registrations.countUp();
    }

    @Override
    public void onMasterUnregistrationFailure(Object arg0) {
    }

    @Override
    public void onMasterUnregistrationSuccess(Object arg0) {
    }

    @Override
    public void onNewPublisher(Subscriber arg0, PublisherIdentifier arg1) {
        connections.countUp();
    }

    @Override
    public void onShutdown(Subscriber arg0) {
    }
}
