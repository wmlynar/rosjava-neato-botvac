package com.github.wmlynar.rosjava.plots;

import java.util.HashMap;

import org.jfree.ui.RefineryUtilities;

public class Plots {

    private static HashMap<String, XTimePlotter> plotsXTimeMap = new HashMap<>();
    private static HashMap<String, XyPlotter> plotsXyMap = new HashMap<>();

    public static void plotXTime(String plotName, String seriesName, double time, double x) {
        XTimePlotter plot = Plots.plotsXTimeMap.get(plotName);
        if (plot == null) {
            plot = new XTimePlotter(plotName);
            RefineryUtilities.centerFrameOnScreen(plot);
            plot.setVisible(true);
            Plots.plotsXTimeMap.put(plotName, plot);
        }
        plot.addValues(seriesName, time, x);
    }

    public static void plotXy(String plotName, String seriesName, double x, double y) {
        XyPlotter plot = Plots.plotsXyMap.get(plotName);
        if (plot == null) {
            plot = new XyPlotter(plotName);
            RefineryUtilities.centerFrameOnScreen(plot);
            plot.setVisible(true);
            Plots.plotsXyMap.put(plotName, plot);
        }
        plot.addValues(seriesName, x, y);
    }

}
