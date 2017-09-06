package com.github.wmlynar.rosjava.o.main;

import com.github.wmlynar.rosjava.o.nodes.axlesim.NodeAxleSim;
import com.github.wmlynar.rosjava.o.nodes.plotter.NodePlotter;
import com.github.wmlynar.rosjava.utils.RosMain;

public class MainSimPlot {
	public static void main(String[] args) {
        RosMain.connectToRosCoreWithoutEnvironmentVariables();

        NodePlotter subscriber = new NodePlotter(10);
        RosMain.executeNode(subscriber);
        
        NodeAxleSim simNode = new NodeAxleSim();
        RosMain.executeNode(simNode);
        simNode.start();
        
		
	}

}
