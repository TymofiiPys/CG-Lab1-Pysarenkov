package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class EdgeDestXComparator implements Comparator<Edge> {
    @Override
    public int compare(Edge o1, Edge o2) {
        GraphNode o1point = o1.getDest();
        GraphNode o2point = o2.getDest();
        if(o1point.getX() > o2point.getX())
            return 1;
        if(o1point.getX() < o2point.getX())
            return -1;
        if(o1point.getY() > o2point.getY())
            return 1;
        if(o1point.getY() < o2point.getY())
            return -1;
        return 0;
    }
}
