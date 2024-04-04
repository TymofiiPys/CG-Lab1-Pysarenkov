package org.example.pointloc;

import java.awt.geom.Point2D;

/**
 * The WeightedEdge class represents weighted directed edge
 */
public class WeightedEdge extends Edge {
    private int weight;

    public WeightedEdge(Point2D.Float src, Point2D.Float dest) {
        super(src, dest);
    }

    public WeightedEdge(WeightedEdge e) {
        super(e.getSrc(), e.getDest());
        this.weight = e.getWeight();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
