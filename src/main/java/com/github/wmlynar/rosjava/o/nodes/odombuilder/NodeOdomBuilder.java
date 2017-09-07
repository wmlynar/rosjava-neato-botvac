package com.github.wmlynar.rosjava.o.nodes.odombuilder;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

import com.github.wmlynar.rosjava.logging.TimeSequencer;
import com.github.wmlynar.rosjava.o.messages.Dist;
import com.github.wmlynar.rosjava.o.messages.Inertial;
import com.github.wmlynar.rosjava.o.messages.Odom;
import com.github.wmlynar.rosjava.o.messages.Scan;
import com.github.wmlynar.rosjava.o.messages.translation.RosToJava;
import com.github.wmlynar.rosjava.o.utils.operators.Differentiator;
import com.github.wmlynar.rosjava.plots.Plots;
import com.github.wmlynar.rosjava.utils.CountUpLatch;
import com.github.wmlynar.rosjava.utils.CountUpSubscriberListener;
import com.github.wmlynar.rosjava.utils.RosMain;
import com.github.wmlynar.rosjava.utils.ScanMessageInterpreter;

import geometry_msgs.Vector3Stamped;
import nav_msgs.Odometry;
import sensor_msgs.Imu;
import sensor_msgs.LaserScan;

/**
 * A simple {@link Subscriber} {@link NodeMain}.
 */
public class NodeOdomBuilder extends AbstractNodeMain {

    private Subscriber<Odometry> odomSubscriber;
    private Subscriber<LaserScan> scanSubscriber;
    private Subscriber<Vector3Stamped> distSubscriber;
    private Subscriber<Imu> imuSubscriber;

	// odom
	OdomSimulator simulator = new OdomSimulator();
	private Differentiator speedDifferentiator = new Differentiator(1e-6);
	
    
    private int queueSize;

    public NodeOdomBuilder(int queueSize) {
        this.queueSize = queueSize;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("ros_log_recorder");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
    	
        TimeSequencer timeSquencer = new TimeSequencer(1, 0.1, connectedNode);

        odomSubscriber = connectedNode.newSubscriber("odom", Odometry._TYPE);
        odomSubscriber.addSubscriberListener(RosMain.getSubscriberListener());
        odomSubscriber.addMessageListener(timeSquencer.getListener(new MessageListener<Odometry>() {
            @Override
            public void onNewMessage(Odometry m) {
            	Odom odom = RosToJava.fromOdom(m);
            	
                Plots.plotXTime("speed", "odom", odom.time.toSeconds(), odom.linear);

                Plots.plotXy("pos", "odom", odom.x, odom.y);
            }
        },0), 10);

        scanSubscriber = connectedNode.newSubscriber("scan", LaserScan._TYPE);
        scanSubscriber.addSubscriberListener(RosMain.getSubscriberListener());
        scanSubscriber.addMessageListener(timeSquencer.getListener(new MessageListener<LaserScan>() {
            @Override
            public void onNewMessage(LaserScan m) {
            	Scan scan = RosToJava.fromScan(m);

            }
        },1), queueSize);

        distSubscriber = connectedNode.newSubscriber("dist", Vector3Stamped._TYPE);
        distSubscriber.addSubscriberListener(RosMain.getSubscriberListener());
        distSubscriber.addMessageListener(timeSquencer.getListener(new MessageListener<Vector3Stamped>() {
            @Override
            public void onNewMessage(Vector3Stamped m) {
            	Dist dist = RosToJava.fromDist(m);

            	double time = dist.time.toSeconds();
            	
        		double distance = (dist.left + dist.right) / 2;
        		double speed = speedDifferentiator.differentiate(dist.time.toSeconds(),distance);
        		if(!speedDifferentiator.isOk()) {
        			return;
        		}
        		simulator.simulate(time);
        		simulator.speed = speed;
            	
                Plots.plotXTime("speed", "d/dt distance", dist.time.toSeconds(), speed);
            }
        },2), queueSize);
        
        imuSubscriber = connectedNode.newSubscriber("data", Imu._TYPE);
        imuSubscriber.addSubscriberListener(RosMain.getSubscriberListener());
        imuSubscriber.addMessageListener(timeSquencer.getListener(new MessageListener<Imu>() {
            @Override
            public void onNewMessage(Imu imu) {
            	Inertial in = RosToJava.fromImu(imu);

            	double time = in.time.toSeconds();
        		simulator.simulate(time);
        		simulator.rotation = -in.angularYaw;
        		
                Plots.plotXy("pos", "sim", simulator.x, simulator.y);
            }
        },3), queueSize);
    }

}
