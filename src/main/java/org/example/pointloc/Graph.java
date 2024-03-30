package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

/**
 * The Graph class represents a directed weighted graph with additional functions such as
 * weight balancing and chain set retrieval
 */
public class Graph {
    private final ArrayList<GraphNode> nodes;
    private final int N; // amount of nodes in the graph
    private final ArrayList<WeightedEdge> edges;
    private final ArrayList<LinkedList<Integer>> incomingEdgeInds;
    private final ArrayList<LinkedList<Integer>> outgoingEdgeInds;
    private Vector<ArrayList<WeightedEdge>> chains;
    private boolean balanced = false;
    private boolean chainsFound = false;

    public Graph(ArrayList<GraphNode> nodes, ArrayList<Edge> edges) {
        this.N = nodes.size();

        // nodes are deep-copied from the respective parameter and sorted
        ArrayList<GraphNode> sortedNodes = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            Point2D.Float curPt = new Point2D.Float();
            curPt.setLocation(nodes.get(i).getLocation());
            sortedNodes.add(i, new GraphNode(curPt));
        }
        sortedNodes.sort(new GraphNodeYComparator());
        this.nodes = sortedNodes;

        // edges are deep-copied from the respective parameter
        this.edges = new ArrayList<>();

        for (Edge edge : edges) {
            this.edges.add(new WeightedEdge(this.nodes.get(this.nodes.indexOf(edge.src)),
                    this.nodes.get(this.nodes.indexOf(edge.dest))));
        }

        this.incomingEdgeInds = new ArrayList<>();
        this.outgoingEdgeInds = new ArrayList<>();
    }

    /**
     * @param in index of node in the nodes array
     * @return total weight of edges incoming to the node
     */
    private int computeWIn(int in) {
        int wIn = 0;
        for (WeightedEdge edge : nodes.get(in).getIn()) {
            wIn += edge.getWeight();
        }
        return wIn;
    }

    /**
     * @param out index of node in the nodes array
     * @return total weight of edges outgoing from the node
     */
    private int computeWOut(int out) {
        int wOut = 0;
        for (WeightedEdge edge : nodes.get(out).getOut()) {
            wOut += edge.getWeight();
        }
        return wOut;
    }

    private WeightedEdge leftMostEdgeFromPoint(int out) {
        return nodes.get(out).getOut().get(0);
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

    private WeightedEdge leftMostEdgeToPoint(int to) {
        return nodes.get(to).getIn().get(0);
    }

    public void weightBalancing() {
        for (var edge : edges) {
            edge.setWeight(1);
        }

        int wIn, wOut;

        for (int i = 1; i < N - 1; i++) {
            wIn = computeWIn(i);
            wOut = computeWOut(i);
            if (wIn > wOut) {
                WeightedEdge d1 = leftMostEdgeFromPoint(i);
                d1.setWeight(wIn - wOut + 1);
            }
        }

        for (int i = N - 2; i > 0; i--) {
            wIn = computeWIn(i);
            wOut = computeWOut(i);
            if (wOut > wIn) {
                WeightedEdge d2 = leftMostEdgeToPoint(i);
                d2.setWeight(wOut - wIn + d2.getWeight());
            }
        }

        balanced = true;
    }

    private boolean presentEdgesFrom0(int[] we) {
        for (WeightedEdge edge : nodes.getFirst().getOut()) {
            if (edge.getWeight() > 0) {
                return true;
            }
        }
        return false;
    }

    private int findInd(Point2D.Float pointToFind) {
        for (int i = 0; i < N; i++) {
            if (nodes.get(i).equals(pointToFind)) {
                return i;
            }
        }
        return -1;
    }

    public Vector<ArrayList<WeightedEdge>> getChains() {
        if (!balanced) {
            throw new RuntimeException("The graph must be balanced first!");
        }

        // weights are copied to a separate array
        // so that weights in edges array are not changed
        int[] weightsCopy = new int[edges.size()];
        int k = 0;
        for (var edge : edges) {
            weightsCopy[k++] = edge.getWeight();
        }

        Vector<ArrayList<WeightedEdge>> chains = new Vector<>();
        ArrayList<WeightedEdge> curChain = null;
        int curSource = 0;
        WeightedEdge edgeToAdd;
        while (presentEdgesFrom0()) {
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
        int[] chainsBetween = new int[]{-1, -1};
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
                    if (k == 0 && doubledSquare < 0) {
                        chainsBetween[k++] = i;
                        continue;
                    }
                    if (k == 1 && doubledSquare > 0) {
                        chainsBetween[k] = i;
                        return chainsBetween;
                    }
                    if (k == 0 && doubledSquare == 0) {
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
