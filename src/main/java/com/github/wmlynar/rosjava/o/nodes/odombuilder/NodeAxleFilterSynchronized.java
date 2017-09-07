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

    private Subscriber<Vector3Stamped> distSubscriber;
	private OdomStateMachine filter = new OdomStateMachine();
    private int queueSize;

    public NodeAxleFilterSynchronized(int queueSize) {
        this.queueSize = queueSize;
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("ros_odom_builder");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
    	
        distSubscriber = connectedNode.newSubscriber("dist", Vector3Stamped._TYPE);
        distSubscriber.addSubscriberListener(RosMain.getSubscriberListener());
        distSubscriber.addMessageListener(new MessageListener<Vector3Stamped>() {
            @Override
            public void onNewMessage(Vector3Stamped m) {
            	Dist dist = RosToJava.fromDist(m);
            	filter.processDist(dist);
            }
        }, queueSize);
    }

}
