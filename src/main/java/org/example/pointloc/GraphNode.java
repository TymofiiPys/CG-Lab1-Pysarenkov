package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

public class GraphNode {
    private final Point2D location;
    private final LinkedList<Edge> in;
    private final LinkedList<Edge> out;

    public GraphNode(Point2D location, LinkedList<Edge> in, LinkedList<Edge> out) {
        this.location = location;
        this.in = in;
        this.out = out;
    }

    public Point2D getLocation() {
        return location;
    }

    public LinkedList<Edge> getIn() {
        return in;
    }

    public LinkedList<Edge> getOut() {
        return out;
    }
}
