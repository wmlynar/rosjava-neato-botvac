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

package com.github.wmlynar.rosjava.o.nodes.beacondetector;

import java.util.Scanner;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import com.github.wmlynar.rosjava.logging.TimeSequencer;
import com.github.wmlynar.rosjava.o.messages.Dist;
import com.github.wmlynar.rosjava.o.messages.Inertial;
import com.github.wmlynar.rosjava.o.messages.Odom;
import com.github.wmlynar.rosjava.o.messages.Scan;
import com.github.wmlynar.rosjava.o.messages.translation.JavaToRos;
import com.github.wmlynar.rosjava.o.messages.translation.RosToJava;
import com.github.wmlynar.rosjava.o.utils.operators.AngleDenormalize;
import com.github.wmlynar.rosjava.o.utils.operators.AngleDifferentiator;
import com.github.wmlynar.rosjava.o.utils.operators.Differentiator;
import com.github.wmlynar.rosjava.o.utils.operators.Integrator;
import com.github.wmlynar.rosjava.o.utils.operators.StartFromZero;
import com.github.wmlynar.rosjava.o.utils.operators.Summator;
import com.github.wmlynar.rosjava.o.utils.operators.ZeroWhenNotChanging;
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
public class NodeBeaconDetector extends AbstractNodeMain {

	private int queueSize;
	private Subscriber<LaserScan> scanSubscriber;
	private Publisher<Odometry> odomPublisher;
    private Subscriber<Odometry> odomSubscriber;
    private Subscriber<Imu> imuSubscriber;
    
	private ScanMessageInterpreter scanInterpreter = new ScanMessageInterpreter();
	private AngleDifferentiator differentiator = new AngleDifferentiator(1e-6);
	private ZeroWhenNotChanging zeroScanAngle = new ZeroWhenNotChanging(2, 1e-3);
	private ZeroWhenNotChanging zeroOdomAngle = new ZeroWhenNotChanging(2, 1e-3);
	private Integrator gyroSummator = new Integrator();
	private ZeroWhenNotChanging zeroGyroAngle = new ZeroWhenNotChanging(20, 1e-3);
	private AngleDenormalize denormalizeScanAngle = new AngleDenormalize();
	private AngleDenormalize denormalizeOdomAngle = new AngleDenormalize();
	

	public NodeBeaconDetector(int queueSize) {
		this.queueSize = queueSize;
	}

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("ros_beacon_detector");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {

		scanSubscriber = connectedNode.newSubscriber("scan", LaserScan._TYPE);
		scanSubscriber.addSubscriberListener(RosMain.getSubscriberListener());
		scanSubscriber.addMessageListener(new MessageListener<LaserScan>() {
			
			@Override
			public void onNewMessage(LaserScan m) {
				Scan scan = RosToJava.fromScan(m);
				
				double angle = scanInterpreter.processScanToAngle(scan.ranges);
				double angular = differentiator.differentiate(scan.time.toSeconds(), angle);
				double zeroangle = zeroScanAngle.process(denormalizeScanAngle.denormalize(angle));
				if(!differentiator.isOk() || !zeroScanAngle.isOk() || !denormalizeScanAngle.isOk()) {
					return;
				}
				Odom odom = new Odom();
				odom.time = scan.time;
				odom.angular = angular;
				
		        Plots.plotXTime("angle", "scan", odom.time.toSeconds(), zeroangle);
		        Plots.plotXTime("rotation", "scan", odom.time.toSeconds(), angular);
				
				odomPublisher.publish(JavaToRos.newOdometryMessage(odom));
			}
		}, queueSize);
		
		odomPublisher = connectedNode.newPublisher("odom_beacon", Odometry._TYPE);
		odomPublisher.addListener(RosMain.getPublisherListener());
		
        odomSubscriber = connectedNode.newSubscriber("odom", Odometry._TYPE);
        odomSubscriber.addSubscriberListener(RosMain.getSubscriberListener());
        odomSubscriber.addMessageListener(new MessageListener<Odometry>() {
            @Override
            public void onNewMessage(Odometry m) {
            	Odom odom = RosToJava.fromOdom(m);
        		double zeroangle = zeroOdomAngle.process(denormalizeOdomAngle.denormalize(odom.yaw));
        		if(!zeroOdomAngle.isOk() || !denormalizeOdomAngle.isOk()) {
        			return;
        		}
                Plots.plotXTime("angle", "odom", odom.time.toSeconds(), zeroangle);
                Plots.plotXTime("rotation", "odom", odom.time.toSeconds(), odom.angular);
            }
        }, queueSize);
        
        imuSubscriber = connectedNode.newSubscriber("data", Imu._TYPE);
        imuSubscriber.addSubscriberListener(RosMain.getSubscriberListener());
        imuSubscriber.addMessageListener(new MessageListener<Imu>() {
            @Override
            public void onNewMessage(Imu imu) {
            	Inertial in = RosToJava.fromImu(imu);
            	double angle = zeroGyroAngle.process(gyroSummator.integrate(in.time.toSeconds(), -in.angularYaw));
                Plots.plotXTime("rotation", "gyro", in.time.toSeconds(), -in.angularYaw);
                Plots.plotXTime("angle", "gyro", in.time.toSeconds(), angle);
            }
        }, queueSize);
        
//        Scanner keyboard= new Scanner(System.in);
//
//        while(true) {
//            String myint = keyboard.next();
//            System.out.println(myint);
//            zeroOdomAngle.zero();
//            zeroScanAngle.zero();
//        }
        
	}

}
