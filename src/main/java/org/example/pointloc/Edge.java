package org.example.pointloc;

import java.awt.geom.Point2D;

public class Edge {
    private final Point2D.Float src;
    private final Point2D.Float dest;

    public Edge(Point2D.Float src, Point2D.Float dest) {
        this.src = src;
        this.dest = dest;
    }

    public Point2D.Float getSrc() {
        return src;
    }

    public Point2D.Float getDest() {
        return dest;
    }
}
