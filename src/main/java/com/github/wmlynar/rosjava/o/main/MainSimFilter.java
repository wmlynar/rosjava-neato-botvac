package com.github.wmlynar.rosjava.o.main;

import com.github.wmlynar.rosjava.o.nodes.axlefilter.NodeAxleFilter;
import com.github.wmlynar.rosjava.o.nodes.axlefilter.NodeAxleFilterSynchronized;
import com.github.wmlynar.rosjava.o.nodes.axlesim.NodeAxleSim;
import com.github.wmlynar.rosjava.o.nodes.plotter.NodePlotter;
import com.github.wmlynar.rosjava.utils.RosMain;

public class MainSimFilter {
	public static void main(String[] args) {
        RosMain.connectToRosCoreWithoutEnvironmentVariables();

        NodeAxleFilterSynchronized subscriber = new NodeAxleFilterSynchronized(10);
        RosMain.executeNode(subscriber);
        
        NodeAxleSim simNode = new NodeAxleSim();
        RosMain.executeNode(simNode);
        simNode.start();
        
		
	}

}
