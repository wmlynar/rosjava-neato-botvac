package com.github.wmlynar.rosjava.o.main;

import com.github.wmlynar.rosjava.o.nodes.axlefilter.NodeAxleFilterSynchronized;
import com.github.wmlynar.rosjava.o.nodes.axlesim.NodeAxleSim;
import com.github.wmlynar.rosjava.utils.RosMain;

public class MainSimAxleFilter {
	public static void main(String[] args) throws InterruptedException {
        RosMain.connectToRosCoreWithoutEnvironmentVariables();

        NodeAxleFilterSynchronized filter = new NodeAxleFilterSynchronized(10);
        RosMain.executeNode(filter);
        
        NodeAxleSim simNode = new NodeAxleSim();
        RosMain.executeNode(simNode);
        
        RosMain.awaitForConnectionsNotIncreasing();
        
        simNode.start();
	}

}
