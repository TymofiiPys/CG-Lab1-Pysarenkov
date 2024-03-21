package org.example.pointloc;

import junit.framework.TestCase;

import java.awt.geom.Point2D;
import java.util.Arrays;

public class GraphTest extends TestCase {

    public Point2D.Float[] getPoints() {
        Point2D.Float[] points = new Point2D.Float[9];
        points[0] = new Point2D.Float(1, 8);
        points[1] = new Point2D.Float(2, 4);
        points[2] = new Point2D.Float(3, 1);
        points[3] = new Point2D.Float(4.5f, 4.5f);
        points[4] = new Point2D.Float(5, 6.5f);
        points[5] = new Point2D.Float(5.5f, 9);
        points[6] = new Point2D.Float(8, 7);
        points[7] = new Point2D.Float(8.5f, 6);
        points[8] = new Point2D.Float(9, 2);
        return points;
    }

    public void testSortPoints() {
        Point2D.Float[] points = getPoints();
        Point2D.Float[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints, new Point2DYFirstComparator());
        assertEquals(points[0], sortedPoints[7]);
        assertEquals(points[1], sortedPoints[2]);
        assertEquals(points[2], sortedPoints[0]);
        assertEquals(points[3], sortedPoints[3]);
        assertEquals(points[4], sortedPoints[5]);
        assertEquals(points[5], sortedPoints[8]);
        assertEquals(points[6], sortedPoints[6]);
        assertEquals(points[7], sortedPoints[4]);
        assertEquals(points[8], sortedPoints[1]);
    }

    public void testWeightBalancing() {
        Point2D.Float[] points = getPoints();
        WeightedEdge[] edges = new WeightedEdge[16];
        edges[0] = new WeightedEdge(points[0], points[5]);
        edges[1] = new WeightedEdge(points[0], points[1]);
        edges[2] = new WeightedEdge(points[1], points[2]);
        edges[3] = new WeightedEdge(points[1], points[3]);
        edges[4] = new WeightedEdge(points[1], points[4]);
        edges[5] = new WeightedEdge(points[2], points[3]);
        edges[6] = new WeightedEdge(points[2], points[7]);
        edges[7] = new WeightedEdge(points[2], points[8]);
        edges[8] = new WeightedEdge(points[3], points[4]);
        edges[9] = new WeightedEdge(points[3], points[7]);
        edges[10] = new WeightedEdge(points[4], points[5]);
        edges[11] = new WeightedEdge(points[4], points[6]);
        edges[12] = new WeightedEdge(points[5], points[6]);
        edges[13] = new WeightedEdge(points[6], points[7]);
        edges[14] = new WeightedEdge(points[7], points[8]);
        edges[15] = new WeightedEdge(points[4], points[7]);
        Graph gr = new Graph(points, edges);

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
}