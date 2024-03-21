package org.example.pointloc;

import java.awt.geom.Point2D;

public class WeightedEdge extends Edge {
    private int weight;

    public WeightedEdge(Point2D.Float src, Point2D.Float dest, int weight) {
        super(src, dest);
        this.weight = weight;
    }

    public WeightedEdge(Point2D.Float src, Point2D.Float dest) {
        super(src, dest);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
