package com.github.wmlynar.rosjava.o.main;

import com.github.wmlynar.rosjava.o.nodes.axlefilter.NodeAxleFilterSynchronized;
import com.github.wmlynar.rosjava.o.nodes.axlesim.NodeAxleSim;
import com.github.wmlynar.rosjava.o.nodes.beacondetector.NodeBeaconDetector;
import com.github.wmlynar.rosjava.o.nodes.csvplayer.NodeRosLogPlayer;
import com.github.wmlynar.rosjava.utils.RosMain;

public class MainPlayCsvAxleFilter {
	public static void main(String[] args) throws InterruptedException {
        RosMain.connectToRosCoreWithoutEnvironmentVariables();

        NodeAxleFilterSynchronized filter = new NodeAxleFilterSynchronized(10);
        RosMain.executeNode(filter);

        NodeBeaconDetector beaconDetector = new NodeBeaconDetector(10);
        RosMain.executeNode(beaconDetector);
        
        NodeRosLogPlayer playerNode = new NodeRosLogPlayer("src/main/resources/logs/with_gyro/rotating.csv");
        RosMain.executeNode(playerNode);
        
        RosMain.awaitForConnectionsNotIncreasing();
       
        playerNode.start();
	}

}
