package com.github.wmlynar.rosjava.o.main;

import com.github.wmlynar.rosjava.o.nodes.csvplayer.NodeRosLogPlayer;
import com.github.wmlynar.rosjava.utils.RosMain;

public class MainPlayCsv {
	
	public static void main(String[] args) {
        RosMain.connectToRosCoreWithoutEnvironmentVariables();

        NodeRosLogPlayer playerNode = new NodeRosLogPlayer("src/main/resources/logs/with_gyro/rotating.csv");
        RosMain.executeNode(playerNode);
        playerNode.start();
		
	}

}
