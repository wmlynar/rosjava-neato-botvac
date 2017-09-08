package com.github.wmlynar.rosjava.o.main;

import com.github.wmlynar.rosjava.o.nodes.beacondetector.NodeBeaconDetector;
import com.github.wmlynar.rosjava.o.nodes.csvplayer.NodeRosLogPlayer;
import com.github.wmlynar.rosjava.utils.RosMain;

public class MainPlayCsvBeaconDetector {
	
	public static void main(String[] args) throws InterruptedException {
        RosMain.connectToRosCoreWithoutEnvironmentVariables();

        NodeRosLogPlayer playerNode = new NodeRosLogPlayer("src/main/resources/logs/with_gyro/rotating.csv");
        RosMain.executeNode(playerNode);
        
        NodeBeaconDetector beaconDetector = new NodeBeaconDetector(10);
        RosMain.executeNode(beaconDetector);
        
        RosMain.awaitForConnectionsNotIncreasing();
        
        playerNode.start();
		
        
	}

}
