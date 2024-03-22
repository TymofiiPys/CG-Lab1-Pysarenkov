package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Graph {
    private final Point2D.Float[] nodes;
    private final int N;
    private final WeightedEdge[] edges;
    private Vector<ArrayList<WeightedEdge>> chains;
    private boolean balanced = false;
    private boolean chainsFound = false;

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

    private int leftMostEdgeIndFromPoint(int out, WeightedEdge[] edgeList) {
        double minX = Double.POSITIVE_INFINITY;
        int leftMostEdgeInd = -1;
        for (int i = 0; i < edgeList.length; i++) {
            if (edgeList[i].getWeight() > 0 && edgeList[i].getSrc() == nodes[out]) {
                Point2D dest = edgeList[i].getDest();
                if (dest.getX() < minX) {
                    leftMostEdgeInd = i;
                    minX = dest.getX();
                }
            }
        }
        return leftMostEdgeInd;
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

        balanced = true;
    }

    private boolean presentEdgesFrom0(WeightedEdge[] edgeList) {
        for (int i = 0; i < edgeList.length; i++) {
            if (edgeList[i].getWeight() > 0 && edgeList[i].getSrc() == nodes[0]) {
                return true;
            }
        }
        return false;
    }

    private int findInd(Point2D.Float pointToFind) {
        for (int i = 0; i < N; i++) {
            if (nodes[i].equals(pointToFind)) {
                return i;
            }
        }
        return -1;
    }

    public Vector<ArrayList<WeightedEdge>> getChains() {
        if (!balanced) {
            throw new RuntimeException("The graph must be balanced first!");
        }

        WeightedEdge[] edgesCopy = new WeightedEdge[edges.length];

        for (int i = 0; i < edges.length; i++) {
            edgesCopy[i] = new WeightedEdge(edges[i].getSrc(), edges[i].getDest(), edges[i].getWeight());
        }

        Vector<ArrayList<WeightedEdge>> chains = new Vector<>();
        ArrayList<WeightedEdge> curChain = null;
        int curSource = 0;
        WeightedEdge edgeToAdd;
        while (presentEdgesFrom0(edgesCopy)) {
            curChain = new ArrayList<>();
            while (curSource != N - 1) {
                edgeToAdd = edgesCopy[leftMostEdgeIndFromPoint(curSource, edgesCopy)];
                curChain.add(edgeToAdd);
                edgeToAdd.setWeight(edgeToAdd.getWeight() - 1);
                curSource = findInd(edgeToAdd.getDest());
            }
            chains.add(curChain);
            curSource = 0;
        }
        if (!chains.isEmpty()) {
            chainsFound = true;
            this.chains = chains;
        }
        return chains;
    }

    public int[] pointLocation(Point2D.Float point) {
        int[] chainsBetween = new int[] {-1, -1};
        int k = 0;
        for (int i = 0; i < chains.size(); i++) {
            var curChain = chains.get(i);
            int edgesInCurChain = curChain.size();
            for (int j = 0; j < edgesInCurChain; j++) {
                Point2D.Float src = curChain.get(j).getSrc();
                Point2D.Float dest = curChain.get(j).getDest();
                if (src.getY() < point.getY() && dest.getY() > point.getY()) {
                    double doubledSquare = src.getX() * dest.getY()
                            + point.getX() * src.getY()
                            + dest.getX() * point.getY()
                            - point.getX() * dest.getY()
                            - src.getX() * point.getY()
                            - dest.getX() * src.getY();
                    if(k == 0 && doubledSquare < 0) {
                        chainsBetween[k++] = i;
                        continue;
                    }
                    if(k == 1 && doubledSquare > 0) {
                        chainsBetween[k] = i;
                        return chainsBetween;
                    }
                    if(k == 0 && doubledSquare == 0) {
                        chainsBetween[k] = i;
                    }
                }
            }
        }
        return chainsBetween;
    }

    public WeightedEdge[] getEdges() {
        return edges;
    }
}
