package com.github.wmlynar.rosjava.utils;

import java.net.URI;

import org.ros.RosCore;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import com.google.common.collect.Lists;

public class RosMain {
    private static String[] EMPTY = { "" };
    private static NodeConfiguration nodeConfiguration;
    private static NodeMainExecutor nodeMainExecutor;

    private static CountUpLatch registrations = new CountUpLatch(0);
    private static CountUpLatch connections = new CountUpLatch(0);

    private static CountUpPublisherListener publisherlistener = new CountUpPublisherListener(registrations, connections);
    private static CountUpSubscriberListener subscriberlistener = new CountUpSubscriberListener(registrations, connections);

    public static void startRosCore() {
        RosCore rosCore = RosCore.newPublic(11311);
        rosCore.start();
        try {
            rosCore.awaitStart();
        } catch (Exception e) {
            // throw new RuntimeException(e);
            System.out.println("Unable to start core, continuing");
        }
    }

    /**
     * Starts RosCore without taking environment variables into account.
     */
    public static void connectToRosCoreWithoutEnvironmentVariables() {
        URI masteruri = URI.create("http://127.0.0.1:11311");
        String host = "127.0.0.1";

        nodeConfiguration = NodeConfiguration.newPublic(host, masteruri);
        nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
    }

    /**
     * Starts RosCore, takes parameters from command line and from environment
     * variables.
     *
     * @param args
     */
    public static void connectToRosCoreUsingEnvironmentVariables(String[] args) {
        if (args.length == 0) {
            args = EMPTY;
        }
        CommandLineLoader loader = new CommandLineLoader(Lists.newArrayList(args));
        nodeConfiguration = loader.build();

        nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
    }

    public static void startAndConnectToRosCoreWithoutEnvironmentVariables() {
        startRosCore();
        connectToRosCoreWithoutEnvironmentVariables();
    }

    public static void startRosCoreLocal() {
        RosCore rosCore = RosCore.newPrivate();
        rosCore.start();
        try {
            rosCore.awaitStart();
        } catch (Exception e) {
            // throw new RuntimeException(e);
            System.out.println("Unable to start core, continuing");
        }
        nodeConfiguration = NodeConfiguration.newPrivate();
        nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
    }

    public static void executeNode(NodeMain node) {
        nodeMainExecutor.execute(node, nodeConfiguration);
    }

    public static void shutdown() {
        nodeMainExecutor.shutdown();
    }

    public static void awaitForRegistrations(int number) throws InterruptedException {
        registrations.awaitFor(number);
    }

    public static void awaitForConnections(int number) throws InterruptedException {
        connections.awaitFor(number);
    }

    public static CountUpPublisherListener getPublisherListener() {
        return publisherlistener;
    }

    public static CountUpSubscriberListener getSubscriberListener() {
        return subscriberlistener;
    }

}
