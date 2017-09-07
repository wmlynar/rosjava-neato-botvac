package com.github.wmlynar.rosjava.o.nodes.axlesim;

import java.util.concurrent.CountDownLatch;

import org.ros.concurrent.CancellableLoop;
import org.ros.message.Duration;
import org.ros.message.Time;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import com.github.wmlynar.rosjava.o.messages.Dist;
import com.github.wmlynar.rosjava.o.messages.Odom;
import com.github.wmlynar.rosjava.o.messages.translation.JavaToRos;
import com.github.wmlynar.rosjava.o.nodes.axlefilter.internal.AxleWidthSimulator;
import com.github.wmlynar.rosjava.utils.RosMain;

import geometry_msgs.Vector3Stamped;
import nav_msgs.Odometry;
import sensor_msgs.Imu;
import sensor_msgs.LaserScan;

public class NodeAxleSim extends AbstractNodeMain {

	private Publisher<Odometry> odomPublisher;
	private Publisher<LaserScan> scanPublisher;
	private Publisher<Vector3Stamped> distPublisher;
	private Publisher<Imu> imuPublisher;

	private ConnectedNode connectedNode;

	private CountDownLatch initializedLatch = new CountDownLatch(1);
	private CountDownLatch finishedLatch = new CountDownLatch(1);
	
	private double simTime;
	private Time startTime;

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("ros_log_player");
	}

	@Override
	public void onStart(final ConnectedNode connectedNode) {
		odomPublisher = connectedNode.newPublisher("odom", Odometry._TYPE);
		odomPublisher.addListener(RosMain.getPublisherListener());
		scanPublisher = connectedNode.newPublisher("scan", LaserScan._TYPE);
		scanPublisher.addListener(RosMain.getPublisherListener());
		distPublisher = connectedNode.newPublisher("dist", Vector3Stamped._TYPE);
		distPublisher.addListener(RosMain.getPublisherListener());
		imuPublisher = connectedNode.newPublisher("data", Imu._TYPE);
		imuPublisher.addListener(RosMain.getPublisherListener());

		this.connectedNode = connectedNode;

		initializedLatch.countDown();
	}

	public void start() {
		try {
			initializedLatch.await();
		} catch (InterruptedException e1) {
		}

		AxleWidthSimulator simulator = new AxleWidthSimulator();
		simulator.setAxleWidth(0.25);
        simulator.setRotationNoise(.1);
        simulator.setSpeed(1);
        simulator.setAccelerationNoise(1);
        simulator.setTimeStep(0.1);
        
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			@Override
			protected void loop() throws InterruptedException {
	            simulator.simulate(simTime);
	            
	            Time time = getSimTime(simTime);
	            
	            Odom o = new Odom();
	            o.time = time;
	            o.x = simulator.getX();
	            o.y = simulator.getY();
	            o.yaw = simulator.getAngle();
	            o.linear = simulator.getSpeed();
	            o.angular = simulator.getRotation();
	            Odometry odometry = JavaToRos.newOdomMessage(o);
	            odomPublisher.publish(odometry);
	            
	            Dist dist = new Dist();
	            dist.time = time;
	            dist.left = simulator.getDistanceLeft();
	            dist.right = simulator.getDistanceRight();
	            Vector3Stamped distances = JavaToRos.newDistMessage(dist);
	            distPublisher.publish(distances);
	            
	            simTime +=0.25;
	            if(simTime>100) {
	            	finishedLatch.countDown();
	            	cancel();
	            }
	            Thread.sleep(250);
			}
		});
	}

	public void awaitFinished() throws InterruptedException {
		finishedLatch.await();
	}

	private Time getSimTime(double simTime) {
		if (this.startTime==null) {
			this.startTime=connectedNode.getCurrentTime();
		}
		return startTime.add(new Duration(simTime));
	}
}
