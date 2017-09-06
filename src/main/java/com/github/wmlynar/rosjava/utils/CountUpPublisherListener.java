package com.github.wmlynar.rosjava.utils;

import org.ros.internal.node.topic.SubscriberIdentifier;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.PublisherListener;

public class CountUpPublisherListener implements PublisherListener {

    private CountUpLatch registrations;
    private CountUpLatch connections;

    public CountUpPublisherListener(CountUpLatch registrations, CountUpLatch connections) {
        this.registrations = registrations;
        this.connections = connections;
        this.registrations.countUp();
        this.connections.countUp();
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
    public void onNewSubscriber(Publisher arg0, SubscriberIdentifier arg1) {
        connections.countUp();
    }

    @Override
    public void onShutdown(Publisher arg0) {
    }

}
