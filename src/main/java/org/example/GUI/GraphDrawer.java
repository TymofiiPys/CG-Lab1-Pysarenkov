package org.example.GUI;

import org.example.pointloc.Edge;
import org.example.pointloc.Graph;
import org.example.pointloc.WeightedEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import java.lang.Math.*;

/**
 * Class for drawing graphs
 */
public class GraphDrawer {
    private Graph graph;
    private Graphics2D graphics;
    private final JPanel panelDraw;
    private final int nodesRad = 4;
    private final int textXOffset = -25;
    private final float arrowSticksLength = 12;
    private final double arrowSticksAngle = 20;

    /**
     * @param panelDraw a {@code JPanel} on which graphs will be drawn
     */
    public GraphDrawer(JPanel panelDraw) {
        this.panelDraw = panelDraw;
        this.graphics = (Graphics2D) panelDraw.getGraphics();
    }

    /**
     * Change graph to draw
     *
     * @param gr a new graph
     */
    public void setGraph(Graph gr) {
        this.graph = gr;
    }

    /**
     * @return offsets which will be used to draw graph in the centre of panelDraw
     */
    public int[] offsets() {
        Point2D.Float first = graph.getNodes().getFirst();
        Point2D.Float last = graph.getNodes().getLast();
        Point2D.Float center = new Point2D.Float((first.x + last.x) / 2, (first.y + last.y) / 2);

        Point2D.Float panelDrawCenter = new Point2D.Float((float) panelDraw.getWidth() / 2, (float) panelDraw.getHeight() / 2);

        return new int[]{(int) (panelDrawCenter.x - center.x), (int) (panelDrawCenter.y - center.y)};
    }

    public Point2D.Float adaptToPanel(Point2D.Float p) {
        int[] offsets = offsets();
        return new Point2D.Float(p.x + offsets[0], panelDraw.getHeight() - (p.y + offsets[1]));
    }

    public void drawGraph() {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        ArrayList<WeightedEdge> edges = graph.getEdges();
        int[] offsets = offsets();
        //axes
        gr.drawLine(0, panelDraw.getHeight() - offsets[1], panelDraw.getWidth(), panelDraw.getHeight() - offsets[1]);
        gr.drawLine(offsets[0], 0, offsets[0], panelDraw.getHeight());
        for (WeightedEdge edge : edges) {
            Point2D.Float pt1 = adaptToPanel(edge.getSrc());
            Point2D.Float pt2 = adaptToPanel(edge.getDest());
            gr.drawLine((int) pt1.x,
                    (int) pt1.y,
                    (int) pt2.x,
                    (int) pt2.y);
        }
        ArrayList<Point2D.Float> nodes = graph.getNodes();
        gr.setColor(Color.RED);
        for (Point2D.Float node : nodes) {
            Point2D.Float adapted = adaptToPanel(node);
            gr.fillOval((int) (adapted.x - nodesRad),
                    (int) (adapted.y - nodesRad),
                    (int) (2 * nodesRad),
                    (int) (2 * nodesRad));
        }
    }

    public void drawDirectionsOnEdge(Edge edge) {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        Point2D.Float pt1 = adaptToPanel(edge.getSrc());
        Point2D.Float pt2 = adaptToPanel(edge.getDest());
        //minus because jpanel's origin is on left top, not bottom
        float tangent = -(pt1.y - pt2.y) / (pt1.x - pt2.x);
        double angle = Math.atan(tangent);
        double angle1 = angle + Math.toRadians(arrowSticksAngle);
        double angle2 = angle - Math.toRadians(arrowSticksAngle);
        int x1 = (int) (pt2.x - arrowSticksLength * Math.cos(angle1));
        int y1 = (int) (pt2.y + arrowSticksLength * Math.sin(angle1));
        int x2 = (int) (pt2.x - arrowSticksLength * Math.cos(angle2));
        int y2 = (int) (pt2.y + arrowSticksLength * Math.sin(angle2));
//        gr.drawLine((int) pt2.x,
//                (int) pt2.y,
//                (int) (pt2.x + arrowSticksLength * Math.cos(angle + Math.toRadians(arrowSticksAngle))),
//                (int) (pt2.x + arrowSticksLength * Math.sin(angle + Math.toRadians(arrowSticksAngle))));
        gr.drawLine((int) pt2.x,
                (int) pt2.y,
                 x1,
                 y1);
        gr.drawLine((int) pt2.x,
                (int) pt2.y,
                x2,
                y2);
//                gr.drawLine((int) pt2.x,
//                (int) pt2.y,
//                (int) (pt2.x + arrowSticksLength * Math.cos(angle - Math.toRadians(arrowSticksAngle))),
//                (int) (pt2.x + arrowSticksLength * Math.sin(angle - Math.toRadians(arrowSticksAngle))));
    }

    public void drawDirectedEnumeratedGraph() {
        drawGraph();
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        ArrayList<Point2D.Float> nodes = graph.getNodes();
        int[] offsets = offsets();
        int k = 0;
        for (Point2D.Float node : nodes) {
            Point2D.Float adapted = adaptToPanel(node);
            gr.drawString(k++ + "", (int) adapted.x + textXOffset, (int) adapted.y);
        }
        graph.getEdges().stream().forEach(this::drawDirectionsOnEdge);
    }

    public boolean graphSet() {
        return graph != null;
    }
}
