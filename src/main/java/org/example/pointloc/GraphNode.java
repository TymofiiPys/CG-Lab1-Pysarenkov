package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

public class GraphNode {
    private final Point2D.Float location;
    private final LinkedList<Edge> in;
    private final LinkedList<Edge> out;

    public GraphNode(Point2D.Float location) {
        this.location = location;
        this.in = new LinkedList<>();
        this.out = new LinkedList<>();
    }

    public Point2D.Float getLocation() {
        return location;
    }

    public LinkedList<Edge> getIn() {
        return in;
    }

    public LinkedList<Edge> getOut() {
        return out;
    }

    public float getX() {
        return location.x;
    }

    public float getY() {
        return location.y;
    }
}
