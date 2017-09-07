/*


 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * ZXlKdn/QCopyright (C) 2014 woj.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.wmlynar.rosjava.o.nodes.axlefilter;

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
public class NodeAxleFilterSynchronized extends AbstractNodeMain {

    private Subscriber<Odometry> odomSubscriber;
    private Subscriber<LaserScan> scanSubscriber;
    private Subscriber<Vector3Stamped> distSubscriber;
    private Subscriber<Imu> imuSubscriber;

    private CountUpSubscriberListener subscriberListener = new CountUpSubscriberListener();
    private CountUpLatch messagesCount = new CountUpLatch(0);

    private ScanMessageInterpreter scanInterpreter = new ScanMessageInterpreter();
    
    private int queueSize;
	
	private AxleFilterStateMachine filter = new AxleFilterStateMachine();

    public NodeAxleFilterSynchronized(int queueSize) {
        this.queueSize = queueSize;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("ros_log_recorder");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
    	
    	subscriberListener.setLog(connectedNode.getLog());

        TimeSequencer timeSquencer = new TimeSequencer(0.5, 0.1, connectedNode);

        odomSubscriber = connectedNode.newSubscriber("odom", Odometry._TYPE);
        odomSubscriber.addSubscriberListener(subscriberListener);
        odomSubscriber.addMessageListener(timeSquencer.getListener(new MessageListener<Odometry>() {
            @Override
            public void onNewMessage(Odometry m) {
            	Odom odom = RosToJava.fromOdom(m);
            	filter.processOdom(odom);
                countUp();
            }
        },0), 10);

        scanSubscriber = connectedNode.newSubscriber("scan", LaserScan._TYPE);
        scanSubscriber.addSubscriberListener(subscriberListener);
        scanSubscriber.addMessageListener(timeSquencer.getListener(new MessageListener<LaserScan>() {
            @Override
            public void onNewMessage(LaserScan m) {
            	Scan scan = RosToJava.fromScan(m);
                countUp();
            }
        },1), queueSize);

        distSubscriber = connectedNode.newSubscriber("dist", Vector3Stamped._TYPE);
        distSubscriber.addSubscriberListener(subscriberListener);
        distSubscriber.addMessageListener(timeSquencer.getListener(new MessageListener<Vector3Stamped>() {
            @Override
            public void onNewMessage(Vector3Stamped m) {
            	Dist dist = RosToJava.fromDist(m);
            	filter.processDist(dist);
                countUp();
            }
        },2), queueSize);
        
        imuSubscriber = connectedNode.newSubscriber("data", Imu._TYPE);
        imuSubscriber.addSubscriberListener(subscriberListener);
        imuSubscriber.addMessageListener(timeSquencer.getListener(new MessageListener<Imu>() {
            @Override
            public void onNewMessage(Imu imu) {
            	Inertial in = RosToJava.fromImu(imu);
                countUp();
            }
        },3), queueSize);
    }

    public void awaitForMessages(int count) {
        try {
			messagesCount.awaitFor(count);
		} catch (InterruptedException e) {
		}
    }
    
    public void awaitForConnections(int count) throws InterruptedException {
    	subscriberListener.awaitForConnections(count);
    }

	private void countUp() {
		messagesCount.countUp();
	}
}
