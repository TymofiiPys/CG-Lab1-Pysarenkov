package org.example.pointloc;

import junit.framework.TestCase;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class GraphTest extends TestCase {

    public GraphNode[] getNodes() {
        GraphNode[] nodes = new GraphNode[9];
        nodes[0] = new GraphNode(new Point2D.Float(1, 8));
        nodes[1] = new GraphNode(new Point2D.Float(2, 4));
        nodes[2] = new GraphNode(new Point2D.Float(3, 1));
        nodes[3] = new GraphNode(new Point2D.Float(4.5f, 4.5f));
        nodes[4] = new GraphNode(new Point2D.Float(5, 6.5f));
        nodes[5] = new GraphNode(new Point2D.Float(5.5f, 9));
        nodes[6] = new GraphNode(new Point2D.Float(8, 7));
        nodes[7] = new GraphNode(new Point2D.Float(8.5f, 6));
        nodes[8] = new GraphNode(new Point2D.Float(9, 2));
        return nodes;
    }

    public WeightedEdge[] getEdges(GraphNode[] nodes) {
        WeightedEdge[] edges = new WeightedEdge[16];

        edges[0] = new WeightedEdge(nodes[0], nodes[5]);
        edges[1] = new WeightedEdge(nodes[0], nodes[1]);
        edges[2] = new WeightedEdge(nodes[1], nodes[2]);
        edges[3] = new WeightedEdge(nodes[1], nodes[3]);
        edges[4] = new WeightedEdge(nodes[1], nodes[4]);
        edges[5] = new WeightedEdge(nodes[2], nodes[3]);
        edges[6] = new WeightedEdge(nodes[2], nodes[7]);
        edges[7] = new WeightedEdge(nodes[2], nodes[8]);
        edges[8] = new WeightedEdge(nodes[3], nodes[4]);
        edges[9] = new WeightedEdge(nodes[3], nodes[7]);
        edges[10] = new WeightedEdge(nodes[4], nodes[5]);
        edges[11] = new WeightedEdge(nodes[4], nodes[6]);
        edges[12] = new WeightedEdge(nodes[5], nodes[6]);
        edges[13] = new WeightedEdge(nodes[6], nodes[7]);
        edges[14] = new WeightedEdge(nodes[7], nodes[8]);
        edges[15] = new WeightedEdge(nodes[4], nodes[7]);

        return edges;
    }

    public void testSortPoints() {
        GraphNode[] nodes = getNodes();
        GraphNode[] sortedNodes = nodes.clone();
        Arrays.sort(sortedNodes, new GraphNodeYFirstComparator());
        assertEquals(nodes[0], sortedNodes[7]);
        assertEquals(nodes[1], sortedNodes[2]);
        assertEquals(nodes[2], sortedNodes[0]);
        assertEquals(nodes[3], sortedNodes[3]);
        assertEquals(nodes[4], sortedNodes[5]);
        assertEquals(nodes[5], sortedNodes[8]);
        assertEquals(nodes[6], sortedNodes[6]);
        assertEquals(nodes[7], sortedNodes[4]);
        assertEquals(nodes[8], sortedNodes[1]);
    }

    public void testWeightBalancing() {
        GraphNode[] nodes = getNodes();
        WeightedEdge[] edges = getEdges(nodes);

        Graph gr = new Graph(nodes, edges);

        gr.weightBalancing();
        WeightedEdge[] weightedEdges = gr.getEdges();
        assertEquals(1, weightedEdges[0].getWeight());
        assertEquals(3, weightedEdges[1].getWeight());
        assertEquals(1, weightedEdges[2].getWeight());
        assertEquals(1, weightedEdges[3].getWeight());
        assertEquals(1, weightedEdges[4].getWeight());
        assertEquals(1, weightedEdges[5].getWeight());
        assertEquals(1, weightedEdges[6].getWeight());
        assertEquals(1, weightedEdges[7].getWeight());
        assertEquals(1, weightedEdges[8].getWeight());
        assertEquals(1, weightedEdges[9].getWeight());
        assertEquals(2, weightedEdges[10].getWeight());
        assertEquals(1, weightedEdges[11].getWeight());
        assertEquals(1, weightedEdges[12].getWeight());
        assertEquals(3, weightedEdges[13].getWeight());
        assertEquals(2, weightedEdges[14].getWeight());
        assertEquals(1, weightedEdges[15].getWeight());
    }

    public void testGetChains() {
        Point2D.Float[] points = getPoints();
        WeightedEdge[] edges = getEdges(points);

        Graph gr = new Graph(points, edges);
        boolean thrown = false;
        try {
            gr.getChains();
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;
        gr.weightBalancing();

        Vector<ArrayList<WeightedEdge>> chains = null;
        try {
            chains = gr.getChains();
        } catch (Exception e) {
            thrown = true;
        }
        assertFalse(thrown);
        assertNotNull(chains);
        assertEquals(6, chains.size());
    }
}