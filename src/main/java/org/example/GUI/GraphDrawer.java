package org.example.GUI;

import org.example.pointloc.Edge;
import org.example.pointloc.Graph;
import org.example.pointloc.WeightedEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import java.lang.Math.*;
import java.util.Random;
import java.util.Vector;

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
    private int layer = 0;
    private int colorSeed = 0;

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

    public Point2D.Float adaptFromPanel(Point2D.Float p) {
        int[] offsets = offsets();
        return new Point2D.Float(p.x - offsets[0], panelDraw.getHeight() - (p.y + offsets[1]));
    }

    public void drawGraph(boolean drawEdges) {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        if (layer == 0)
            gr.clearRect(0, 0, panelDraw.getWidth(), panelDraw.getHeight());
        ArrayList<WeightedEdge> edges = graph.getEdges();
        int[] offsets = offsets();
        //axes
        gr.drawLine(0, panelDraw.getHeight() - offsets[1], panelDraw.getWidth(), panelDraw.getHeight() - offsets[1]);
        gr.drawLine(offsets[0], 0, offsets[0], panelDraw.getHeight());
        if (drawEdges) {
            for (WeightedEdge edge : edges) {
                Point2D.Float pt1 = adaptToPanel(edge.getSrc());
                Point2D.Float pt2 = adaptToPanel(edge.getDest());
                gr.drawLine((int) pt1.x,
                        (int) pt1.y,
                        (int) pt2.x,
                        (int) pt2.y);
            }
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
        layer = 0;
    }

    public void drawDirectionsOnEdge(Edge edge) {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        gr.setColor(Color.decode("#3d823f"));
        Point2D.Float pt1 = adaptToPanel(edge.getSrc());
        Point2D.Float pt2 = adaptToPanel(edge.getDest());
        //minus because jpanel's origin is on left top, not bottom
        float tangent = -(pt1.y - pt2.y) / (pt1.x - pt2.x);
        double angle = Math.atan(tangent);
        double angle1 = angle + Math.toRadians(arrowSticksAngle);
        double angle2 = angle - Math.toRadians(arrowSticksAngle);
        int mult1 = angle < 0 ? -1 : 1;
        int mult2 = angle < 0 ? -1 : 1;
        int x1 = (int) (pt2.x - arrowSticksLength * mult1 * Math.cos(angle1));
        int y1 = (int) (pt2.y + arrowSticksLength * mult1 * Math.sin(angle1));
        int x2 = (int) (pt2.x - arrowSticksLength * mult2 * Math.cos(angle2));
        int y2 = (int) (pt2.y + arrowSticksLength * mult2 * Math.sin(angle2));
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

    public void drawDirectedEnumeratedGraph(boolean drawEdges) {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        if (layer == 0)
            gr.clearRect(0, 0, panelDraw.getWidth(), panelDraw.getHeight());
        layer++;
        drawGraph(drawEdges);
        ArrayList<Point2D.Float> nodes = graph.getNodes();
        int[] offsets = offsets();
        int k = 0;
        for (Point2D.Float node : nodes) {
            Point2D.Float adapted = adaptToPanel(node);
            gr.drawString(k++ + "", (int) adapted.x + textXOffset, (int) adapted.y);
        }
        graph.getEdges().stream().forEach(this::drawDirectionsOnEdge);
    }

    public void drawChains() {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        gr.clearRect(0, 0, panelDraw.getWidth(), panelDraw.getHeight());
        layer = 1;
        graph.weightBalancing();
        Vector<ArrayList<WeightedEdge>> chains = (Vector<ArrayList<WeightedEdge>>) graph.getChains().clone();
        gr.setStroke(new BasicStroke(2.0f));
        Random colorRand = new Random(colorSeed);
        for (ArrayList<WeightedEdge> chain : chains.reversed()) {
            gr.setColor(
                    new Color(
                            colorRand.nextInt(256),
                            colorRand.nextInt(256),
                            colorRand.nextInt(256)
                    )
            );
            for (WeightedEdge edge : chain) {
                Point2D.Float pt1 = adaptToPanel(edge.getSrc());
                Point2D.Float pt2 = adaptToPanel(edge.getDest());
                double tangent = Math.atan(-(pt2.y - pt1.y)/(pt2.x - pt1.x));
                double sin = Math.sin(tangent);
                double cos = Math.cos(tangent);
                gr.drawLine((int) (pt1.x + 2 * (edge.getWeight() - 1)),
                        (int) (pt1.y - 2 * (edge.getWeight() - 1)),
                        (int) (pt2.x + 2 * (edge.getWeight() - 1)),
                        (int) (pt2.y - 2 * (edge.getWeight() - 1)));
                edge.setWeight(edge.getWeight() - 1);
            }
        }
        drawDirectedEnumeratedGraph(false);
    }

    public void drawPoint(Point2D p) {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        gr.setColor(Color.BLUE);
        gr.fillOval((int) p.getX() - nodesRad, (int) p.getY() - nodesRad, 2 * nodesRad, 2 * nodesRad);
    }

    public int[] pointLocation(Point2D.Float p) {
        graph.weightBalancing();
        graph.getChains();
        int[] chains = graph.pointLocation(p);
        return chains;
    }

    public boolean graphSet() {
        return graph != null;
    }


}
