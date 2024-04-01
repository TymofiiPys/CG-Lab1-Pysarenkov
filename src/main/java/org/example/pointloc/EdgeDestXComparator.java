package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Class for comparing edges by their destinations' x coordinates
 */
public class EdgeDestXComparator implements Comparator<Integer> {
    private final ArrayList<WeightedEdge> edges;

    /**
     * @param edges edges whose indices will be sorted
     */
    public EdgeDestXComparator(ArrayList<WeightedEdge> edges) {
        this.edges = edges;
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        Point2D.Float o1point = edges.get(o1).getDest();
        Point2D.Float o2point = edges.get(o2).getDest();
        if (o1point.getX() > o2point.getX())
            return 1;
        if (o1point.getX() < o2point.getX())
            return -1;
        return Double.compare(o1point.getY(), o2point.getY());
    }
}