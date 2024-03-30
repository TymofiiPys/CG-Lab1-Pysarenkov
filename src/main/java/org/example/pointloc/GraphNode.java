package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.LinkedList;

/**
 * The GraphNode class represents a graph node which has such attributes as its
 * location on 2D plane, pointers to incoming and outgoing edges
 */
public class GraphNode {
    private final Point2D.Float location;
    private final LinkedList<WeightedEdge> in;
    private final LinkedList<WeightedEdge> out;

    public GraphNode(Point2D.Float location) {
        this.location = location;
        this.in = new LinkedList<>();
        this.out = new LinkedList<>();
    }

    public Point2D.Float getLocation() {
        return location;
    }

    /**
     * Add edge to list of incoming to the node ones.
     * List is resorted to have edges ordered counterclockwise relative to this node
     *
     * @param edge edge to add to the list
     */
    public void addInEdge(WeightedEdge edge) {
        this.in.add(edge);
        this.in.sort(new EdgeDestXComparator());
    }

    /**
     * Add edge to list of outgoing from the node ones.
     * List is resorted to have edges ordered clockwise relative to this node
     *
     * @param edge edge to add to the list
     */
    public void addOutEdge(WeightedEdge edge) {
        this.out.add(edge);
        this.out.sort(new EdgeDestXComparator());
    }

    public LinkedList<WeightedEdge> getIn() {
        return in;
    }

    public LinkedList<WeightedEdge> getOut() {
        return out;
    }

    public float getX() {
        return location.x;
    }

    public float getY() {
        return location.y;
    }

//    public GraphNode copy() {
//        return new GraphNode()
//    }
}
