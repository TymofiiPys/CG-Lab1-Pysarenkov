package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.Objects;

public class Edge  {
    private final GraphNode src;
    private final GraphNode dest;

    public Edge(GraphNode src, GraphNode dest) {
        this.src = src;
        this.dest = dest;
        this.src.getOut().add(this);
        this.dest.getIn().add(this);
        this.src.getOut().sort();
    }

    public GraphNode getSrc() {
        return src;
    }

    public GraphNode getDest() {
        return dest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(src, edge.src) && Objects.equals(dest, edge.dest);
    }
}
