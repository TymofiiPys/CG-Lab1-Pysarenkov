package org.example.GUI;

import org.example.pointloc.Graph;
import org.example.pointloc.WeightedEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Class for drawing graphs
 */
public class GraphDrawer {
    private Graph graph;
    private final JPanel panelDraw;
    private final float nodesRad = 4;

    public GraphDrawer(JPanel panelDraw) {
        this.panelDraw = panelDraw;
    }

    public void setGraph(Graph gr) {
        this.graph = gr;
    }

    public void drawGraph() {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        ArrayList<WeightedEdge> edges = graph.getEdges();
        for (WeightedEdge edge : edges) {
            Point2D.Float pt1 = edge.getSrc();
            Point2D.Float pt2 = edge.getDest();
            gr.drawLine((int) pt1.x,
                    (int) pt1.y,
                    (int) pt2.x,
                    (int) pt2.y);
        }
        ArrayList<Point2D.Float> nodes = graph.getNodes();
        gr.setColor(Color.RED);
        for (Point2D.Float node : nodes) {
            gr.fillOval((int) (node.getX() - nodesRad),
                    (int) (node.getY() - nodesRad),
                    (int) (2 * nodesRad),
                    (int) (2 * nodesRad));
        }
    }
}
