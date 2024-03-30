package org.example.pointloc;

import java.awt.geom.Point2D;

/**
 * The WeightedEdge class represents weighted directed edge
 */
public class WeightedEdge extends Edge {
    private int weight;

    public WeightedEdge(GraphNode src, GraphNode dest, int weight) {
        super(src, dest);
        this.weight = weight;
        this.src.getOut().add(this);
        this.dest.getIn().add(this);
        this.src.getOut().sort(new EdgeDestXComparator());
        this.src.getIn().sort(new EdgeDestXComparator());
    }

    public WeightedEdge(GraphNode src, GraphNode dest) {
        super(src, dest);
        this.src.addOutEdge(this);
        this.dest.addInEdge(this);
    }

    public int getWeight() {
        return weight;
    }
    public GraphNode getSrc() {
        return src;
    }

    public GraphNode getDest() {
        return dest;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
