package org.example.pointloc;

import java.awt.geom.Point2D;

public class WeightedEdge extends Edge {
    private int weight;

    public WeightedEdge(GraphNode src, GraphNode dest, int weight) {
        super(src, dest);
        this.weight = weight;
    }

    public WeightedEdge(GraphNode src, GraphNode dest) {
        super(src, dest);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
