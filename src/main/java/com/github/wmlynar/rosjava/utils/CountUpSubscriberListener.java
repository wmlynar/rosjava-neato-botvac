package com.github.wmlynar.rosjava.utils;

import org.apache.commons.logging.Log;
import org.ros.internal.node.topic.PublisherIdentifier;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.ros.node.topic.SubscriberListener;

public class CountUpSubscriberListener implements SubscriberListener {

    private CountUpLatch registrations;
    private CountUpLatch connections;
	private Log log = null;

    public CountUpSubscriberListener() {
        this.registrations = new CountUpLatch();
        this.connections = new CountUpLatch();
    }

    public CountUpSubscriberListener(Log log) {
        this.registrations = new CountUpLatch();
        this.connections = new CountUpLatch();
    	this.log = log;
	}

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

	public void setLog(Log log) {
		this.log = log;
	}
	
	public void awaitForConnections(int count) throws InterruptedException {
		int actual = 0;
		while(actual < count) {
			actual = connections.awaitFor(count,1000);
			if(log!=null) {
				log.info("Connections: " + connections.getCount() + " out of " +  count);
			}
		}
	}

}
