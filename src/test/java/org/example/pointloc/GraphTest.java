package org.example.pointloc;

import junit.framework.TestCase;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Vector;

public class GraphTest extends TestCase {

    public ArrayList<GraphNode> getNodes() {
        ArrayList<GraphNode> nodes = new ArrayList<>();
        nodes.add(new GraphNode(new Point2D.Float(1, 8)));
        nodes.add(new GraphNode(new Point2D.Float(2, 4)));
        nodes.add(new GraphNode(new Point2D.Float(3, 1)));
        nodes.add(new GraphNode(new Point2D.Float(4.5f, 4.5f)));
        nodes.add(new GraphNode(new Point2D.Float(5, 6.5f)));
        nodes.add(new GraphNode(new Point2D.Float(5.5f, 9)));
        nodes.add(new GraphNode(new Point2D.Float(8, 7)));
        nodes.add(new GraphNode(new Point2D.Float(8.5f, 6)));
        nodes.add(new GraphNode(new Point2D.Float(9, 2)));
        return nodes;
    }

    public ArrayList<WeightedEdge> getEdges(GraphNode[] nodes) {
        ArrayList<WeightedEdge>  edges = new ArrayList<>();

        edges.add(new WeightedEdge(nodes[0], nodes[5]));
        edges.add(new WeightedEdge(nodes[0], nodes[1]));
        edges.add(new WeightedEdge(nodes[1], nodes[2]));
        edges.add(new WeightedEdge(nodes[1], nodes[3]));
        edges.add(new WeightedEdge(nodes[1], nodes[4]));
        edges.add(new WeightedEdge(nodes[2], nodes[3]));
        edges.add(new WeightedEdge(nodes[2], nodes[7]));
        edges.add(new WeightedEdge(nodes[2], nodes[8]));
        edges.add(new WeightedEdge(nodes[3], nodes[4]));
        edges.add(new WeightedEdge(nodes[3], nodes[7]));
        edges.add(new WeightedEdge(nodes[4], nodes[5]));
        edges.add(new WeightedEdge(nodes[4], nodes[6]));
        edges.add(new WeightedEdge(nodes[5], nodes[6]));
        edges.add(new WeightedEdge(nodes[6], nodes[7]));
        edges.add(new WeightedEdge(nodes[7], nodes[8]));
        edges.add(new WeightedEdge(nodes[4], nodes[7]));

        return edges;
    }

    public void testSortPoints() {
        ArrayList<GraphNode> nodes = getNodes();
        ArrayList<GraphNode> sortedNodes = new ArrayList<>();
        sortedNodes.addAll(nodes);
        sortedNodes.sort(new GraphNodeYComparator());
        assertEquals(nodes.get(0), sortedNodes.get(7));
        assertEquals(nodes.get(1), sortedNodes.get(2));
        assertEquals(nodes.get(2), sortedNodes.get(0));
        assertEquals(nodes.get(3), sortedNodes.get(3));
        assertEquals(nodes.get(4), sortedNodes.get(5));
        assertEquals(nodes.get(5), sortedNodes.get(8));
        assertEquals(nodes.get(6), sortedNodes.get(6));
        assertEquals(nodes.get(7), sortedNodes.get(4));
        assertEquals(nodes.get(8), sortedNodes.get(1));
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