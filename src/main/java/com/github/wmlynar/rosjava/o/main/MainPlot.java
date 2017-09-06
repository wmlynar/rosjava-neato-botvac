package com.github.wmlynar.rosjava.o.main;

import java.net.URI;

import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import com.github.wmlynar.rosjava.o.nodes.plotter.NodePlotter;

public class MainPlot {
	
	
    public static void main(String[] args) {

        URI masteruri = URI.create("http://127.0.0.1:11311");
        String host = "127.0.0.1";

        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(host, masteruri);
        NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();

        NodePlotter subscriber = new NodePlotter(10);
        nodeMainExecutor.execute(subscriber, nodeConfiguration);

    }

}
