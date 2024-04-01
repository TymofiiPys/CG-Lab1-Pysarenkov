package org.example.pointloc;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * The Graph class represents a directed weighted graph with additional functions such as
 * weight balancing and chain set retrieval
 */
public class Graph {
    private final ArrayList<Point2D.Float> nodes;
    /**
     * Amount of nodes in the graph
     */
    private final int N;
    private final ArrayList<WeightedEdge> edges;

    /**
     * Array of lists of incoming edges indices.
     * ith element corresponds to list of incoming edge indices for ith node.
     */
    private final ArrayList<LinkedList<Integer>> incomingEdgeIndices;
    /**
     * Array of lists of outgoing edges indices.
     * ith element corresponds to list of outgoing edge indices for ith node.
     */
    private final ArrayList<LinkedList<Integer>> outgoingEdgeIndices;
    private Vector<ArrayList<WeightedEdge>> chains;
    private boolean balanced = false;
    private boolean chainsFound = false;

    public Graph(ArrayList<Point2D.Float> nodes, ArrayList<Edge> edges) {
        if (nodes == null) {
            throw new IllegalArgumentException("nodes is null");
        }
        if (edges == null) {
            throw new IllegalArgumentException("edges is null");
        }
        this.N = nodes.size();

        // nodes are deep-copied from the respective parameter and sorted
        ArrayList<Point2D.Float> sortedNodes = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            Point2D.Float curPt = new Point2D.Float();
            curPt.setLocation(nodes.get(i));
            sortedNodes.add(i, curPt);
        }
        sortedNodes.sort(new Point2DYComparator());
        this.nodes = sortedNodes;

        // edges are deep-copied from the respective parameter
        this.edges = new ArrayList<>(edges.size());

        this.incomingEdgeIndices = new ArrayList<>();
        this.outgoingEdgeIndices = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            incomingEdgeIndices.add(new LinkedList<>());
            outgoingEdgeIndices.add(new LinkedList<>());
        }

        int k = 0;
        for (Edge edge : edges) {
            int srcInd = this.nodes.indexOf(edge.getSrc());
            int destInd = this.nodes.indexOf(edge.getDest());
            //swap indices if the srcInd-th point is larger than destInd-th,
            //use the fact that nodes array is already sorted
            if (srcInd > destInd) {
                int temp = srcInd;
                srcInd = destInd;
                destInd = temp;
            }
            this.edges.add(k, new WeightedEdge(this.nodes.get(srcInd),
                    this.nodes.get(destInd)));
            addToNodeLists(srcInd, destInd, k);
            k++;
        }
        System.out.println();
    }

    /**
     * Read a graph from file. File should be in such format that nodes go first and described by
     * two float numbers (coordinates), one line per node.
     * To finish reading nodes, file should contain an empty line, after which goes list of edges.
     * Edges are described by two integers (source and destination indices in read list).
     *
     * @param filename path to the file containing graph properties
     * @return Graph object read from the file
     */
    public static Graph readFromFile(String filename) {
        Stream<String> fileLines;
        try {
            fileLines = Files.lines(Paths.get(filename));
        } catch (IOException e) {
            throw new RuntimeException("Literally impossible to get this one, man", e);
        }

        ArrayList<Point2D.Float> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        Scanner lineScanner;
        float x, y;
        int pt1, pt2;
        boolean readEdges = false;
        for (String line : fileLines.toList()) {
            if (line.isEmpty()) {
                readEdges = true;
                continue;
            }
            lineScanner = new Scanner(line);
            if (!readEdges) {
                try {
                    x = lineScanner.nextFloat();
                    y = lineScanner.nextFloat();
                } catch (Exception e) {
                    throw new IllegalArgumentException("File opened has wrong format");
                }
                nodes.add(new Point2D.Float(x, y));
            } else {
                try {
                    pt1 = lineScanner.nextInt();
                    pt2 = lineScanner.nextInt();
                } catch (Exception e) {
                    throw new IllegalArgumentException("File opened has wrong format");
                }
                try {
                    edges.add(new Edge(nodes.get(pt1), nodes.get(pt2)));
                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Wrong indices of points in edge section");
                }
            }
        }

        return new Graph(nodes, edges);
    }

    /**
     * Add edge index to list of outgoing edges from srcNodeIndex-th node
     * and to list of incoming edges to destNodeIndex-th node
     * Lists are sorted after addition.
     *
     * @param srcNodeIndex  index of source node for the edge
     * @param destNodeIndex index of destination node for the edge
     * @param edgeIndex     index of the added edge
     */
    private void addToNodeLists(int srcNodeIndex, int destNodeIndex, int edgeIndex) {
        outgoingEdgeIndices.get(srcNodeIndex).add(edgeIndex);
        incomingEdgeIndices.get(destNodeIndex).add(edgeIndex);

        outgoingEdgeIndices.get(srcNodeIndex).sort(new EdgeDestXComparator(edges));
        incomingEdgeIndices.get(destNodeIndex).sort(new EdgeSrcXComparator(edges));
    }

    /**
     * @param in index of node in the nodes array
     * @return total weight of edges incoming to the node
     */
    private int computeWIn(int in) {
        int wIn = 0;
        for (int index : incomingEdgeIndices.get(in)) {
            wIn += edges.get(index).getWeight();
        }
        return wIn;
    }

    /**
     * @param out index of node in the nodes array
     * @return total weight of edges outgoing from the node
     */
    private int computeWOut(int out) {
        int wOut = 0;
        for (int index : outgoingEdgeIndices.get(out)) {
            wOut += edges.get(index).getWeight();
        }
        return wOut;
    }

    private WeightedEdge leftMostEdgeFromPoint(int out) {
        return edges.get(outgoingEdgeIndices.get(out).get(0));
    }

    /**
     * @param out     index of node
     * @param weights weights of edges
     * @return the leftmost edge with positive weight index outgoing from out-th node
     */
    private int leftMostEdgeIndFromPoint(int out, int[] weights) {
        for (int index : outgoingEdgeIndices.get(out)) {
            if (weights[index] > 0) {
                return index;
            }
        }
        return -1;
    }

    private WeightedEdge leftMostEdgeToPoint(int to) {
        return edges.get(incomingEdgeIndices.get(to).get(0));
    }

    /**
     * Does graph weight-balancing using information about
     * incoming and outgoing edges from each node except the first and the last
     */
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

    /**
     * @param weights edge weights
     * @return true if there are positive weight edges outgoing from first node
     */
    private boolean presentEdgesFrom0(int[] weights) {
        for (int index : outgoingEdgeIndices.get(0)) {
            if (weights[index] > 0) {
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
        ArrayList<WeightedEdge> curChain;
        int curSource = 0;
        WeightedEdge edgeToAdd;
        while (presentEdgesFrom0(weightsCopy)) {
            curChain = new ArrayList<>();
            while (curSource != N - 1) {
                int edgeIndex = leftMostEdgeIndFromPoint(curSource, weightsCopy);
                edgeToAdd = edges.get(edgeIndex);
                curChain.add(edgeToAdd);
                weightsCopy[edgeIndex]--;
                curSource = nodes.indexOf(edgeToAdd.getDest());
//                curSource = findInd(edgeToAdd.getDest());
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

    /**
     * find between which chains lies the point
     * @param point point to locate
     * @return indices of chains between which lies the point
     */
    public int[] pointLocation(Point2D.Float point) {
        int[] chainsBetween = new int[]{-1, -1};
        int k = 0;
        for (int i = 0; i < chains.size(); i++) {
            var curChain = chains.get(i);
            for (WeightedEdge weightedEdge : curChain) {
                Point2D.Float src = weightedEdge.getSrc();
                Point2D.Float dest = weightedEdge.getDest();
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
                        return chainsBetween;
                    }
                }
            }
        }
        return chainsBetween;
    }

    public ArrayList<WeightedEdge> getEdges() {
        return edges;
    }

    public ArrayList<Point2D.Float> getNodes() {
        return nodes;
    }
}
