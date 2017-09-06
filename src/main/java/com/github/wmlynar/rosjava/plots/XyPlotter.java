package com.github.wmlynar.rosjava.plots;

import java.util.HashMap;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class XyPlotter extends ApplicationFrame {

    private JFreeChart chart;
    private XYPlot plot;
    private NumberAxis xAxis;
    private NumberAxis yAxis;

    private XYSeriesCollection data = new XYSeriesCollection();
    private HashMap<String, XYSeries> seriesMap = new HashMap<String, XYSeries>();
    private int maxItems = -1;

    public XyPlotter(String title) {
        super(title);

        chart = ChartFactory.createScatterPlot("XY Series Demo", "X", "Y", data, PlotOrientation.VERTICAL, true, true,
                false);

        plot = (XYPlot) chart.getPlot();

        xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setAutoRangeIncludesZero(false);
        xAxis.setAutoRange(true);

        yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setAutoRange(true);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1200, 600));
        setContentPane(chartPanel);
        pack();
    }

    public void setMaximumItemCount(int numItems) {
        this.maxItems = numItems;
        List<XYSeries> series = data.getSeries();
        for (XYSeries s : series) {
            s.setMaximumItemCount(maxItems);
        }
    }

    public void addValues(String name, double x, double y) {
        XYSeries series = seriesMap.get(name);
        if (series == null) {
            series = new XYSeries(name);
            if (this.maxItems > 0) {
                series.setMaximumItemCount(this.maxItems);
            }
            seriesMap.put(name, series);
            data.addSeries(series);
        }
        series.add(x, y);
    }

    public static void main(String[] args) {

        final XyPlotter demo = new XyPlotter("XyTimePlotter");
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

        demo.setMaximumItemCount(10);

        demo.addValues("scan", 100, 200);
        demo.addValues("scan", 150, 230);
        demo.addValues("scan", 300, 330);

        demo.addValues("odom", 120, 200);
        demo.addValues("odom", 160, 230);
    }

}
