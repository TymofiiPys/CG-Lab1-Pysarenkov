package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * The Edge class represents an unweighted directed edge.
 *
 *  Intended for extension by WeightedEdge class.
 */
public class Edge  {
    private final Point2D.Float src;
    private final Point2D.Float dest;

    public Edge(Point2D.Float src, Point2D.Float dest) {
        this.src = src;
        this.dest = dest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(src, edge.src) && Objects.equals(dest, edge.dest);
    }

    public Point2D.Float getSrc() {
        return src;
    }

    public Point2D.Float getDest() {
        return dest;
    }
}
