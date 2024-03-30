package org.example.pointloc;

import java.util.Objects;

/**
 * The Edge class represents an unweighted directed edge.
 *
 *  Intended for extension by WeightedEdge class.
 */
public class Edge  {
    protected final GraphNode src;
    protected final GraphNode dest;

    public Edge(GraphNode src, GraphNode dest) {
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

    public GraphNode getSrc() {
        return src;
    }

    public GraphNode getDest() {
        return dest;
    }
}
