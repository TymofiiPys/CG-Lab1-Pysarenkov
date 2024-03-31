package org.example.pointloc;

import java.awt.geom.Point2D;

/**
 * The WeightedEdge class represents weighted directed edge
 */
public class WeightedEdge extends Edge {
    private int weight;

    public WeightedEdge(Point2D.Float src, Point2D.Float dest, int weight) {
        super(src, dest);
        this.weight = weight;
//        this.src.getOut().add(this);
//        this.dest.getIn().add(this);
//        this.src.getOut().sort(new EdgeDestXComparator(edges));
//        this.src.getIn().sort(new EdgeDestXComparator(edges));
    }

    public WeightedEdge(Point2D.Float src, Point2D.Float dest) {
        super(src, dest);
//        this.src.addOutEdge(this);
//        this.dest.addInEdge(this);
    }

    public int getWeight() {
        return weight;
    }
//    public GraphNode getSrc() {
//        return src;
//    }
//
//    public GraphNode getDest() {
//        return dest;
//    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
