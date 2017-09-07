package com.github.wmlynar.rosjava.o.main;

import com.github.wmlynar.rosjava.o.nodes.beacondetector.NodeBeaconDetector;
import com.github.wmlynar.rosjava.o.nodes.csvplayer.NodeRosLogPlayer;
import com.github.wmlynar.rosjava.o.nodes.odombuilder.NodeOdomBuilder;
import com.github.wmlynar.rosjava.utils.RosMain;

public class MainPlayCsvOdomBuilder {
	
	public static void main(String[] args) throws InterruptedException {
        RosMain.connectToRosCoreWithoutEnvironmentVariables();

        NodeRosLogPlayer playerNode = new NodeRosLogPlayer("src/main/resources/logs/with_gyro/driving_around.csv");
        RosMain.executeNode(playerNode);
        
        NodeOdomBuilder odomBuilder = new NodeOdomBuilder(10);
        RosMain.executeNode(odomBuilder);
        
        RosMain.awaitForConnections(2);
        
        playerNode.start();
		
        
	}

}
