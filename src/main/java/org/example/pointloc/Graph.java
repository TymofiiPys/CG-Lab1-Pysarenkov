package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.Arrays;

public class Graph {
    private final Point2D.Float[] nodes;
    private final int N;
    private final WeightedEdge[] edges;

    public Graph(Point2D.Float[] nodes, Edge[] edges) {
        this.N = nodes.length;
        Point2D.Float[] sortedNodes = new Point2D.Float[N];
        for (int i = 0; i < N; i++) {
            sortedNodes[i] = new Point2D.Float((float) nodes[i].getX(), (float) nodes[i].getY());
        }
        Arrays.sort(sortedNodes, new Point2DYFirstComparator());
        this.nodes = sortedNodes;
        this.edges = new WeightedEdge[edges.length];
        int k = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                for (var edge : edges) {
                    if ((edge.getSrc().equals(this.nodes[i])
                            && edge.getDest().equals(this.nodes[j])) ||
                            (edge.getSrc().equals(this.nodes[j])
                            && edge.getDest().equals(this.nodes[i]))) {
                        this.edges[k] = new WeightedEdge(this.nodes[i], this.nodes[j]);
                        k++;
                    }
                }
            }
        }
    }

    private int computeWIn(int in) {
        int wIn = 0;
        for (WeightedEdge edge : edges) {
            if (edge.getDest() == nodes[in]) {
                wIn += edge.getWeight();
            }
        }
        return wIn;
    }

    private int computeWOut(int out) {
        int wOut = 0;
        for (WeightedEdge edge : edges) {
            if (edge.getSrc() == nodes[out]) {
                wOut += edge.getWeight();
            }
        }
        return wOut;
    }

    private WeightedEdge leftMostEdgeFromPoint(int out) {
        double minX = Double.POSITIVE_INFINITY;
        WeightedEdge leftMostEdge = null;
        for (var edge : edges) {
            if (edge.getSrc() == nodes[out]) {
                Point2D dest = edge.getDest();
                if (dest.getX() < minX) {
                    leftMostEdge = edge;
                    minX = dest.getX();
                }
            }
        }
        return leftMostEdge;
    }

    private WeightedEdge leftMostEdgeToPoint(int out) {
        double minX = Double.POSITIVE_INFINITY;
        WeightedEdge leftMostEdge = null;
        for (var edge : edges) {
            if (edge.getDest() == nodes[out]) {
                Point2D src = edge.getSrc();
                if (src.getX() < minX) {
                    leftMostEdge = edge;
                    minX = src.getX();
                }
            }
        }
        return leftMostEdge;
    }

    public void weightBalancing() {
        for (var edge : edges) {
            edge.setWeight(1);
        }

        int[] wIn = new int[N];
        int[] wOut = new int[N];

        for (int i = 1; i < N - 1; i++) {
            wIn[i] = computeWIn(i);
            wOut[i] = computeWOut(i);
            if (wIn[i] > wOut[i]) {
                WeightedEdge d1 = leftMostEdgeFromPoint(i);
                d1.setWeight(wIn[i] - wOut[i] + 1);
            }
        }

        for (int i = N - 2; i > 0; i--) {
            wIn[i] = computeWIn(i);
            wOut[i] = computeWOut(i);
            if (wOut[i] > wIn[i]) {
                WeightedEdge d2 = leftMostEdgeToPoint(i);
                d2.setWeight(wOut[i] - wIn[i] + d2.getWeight());
            }
        }
    }

    public WeightedEdge[] getEdges() {
        return edges;
    }
}
