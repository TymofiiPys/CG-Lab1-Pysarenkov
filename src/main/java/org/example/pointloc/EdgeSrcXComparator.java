package org.example.pointloc;

import java.util.Comparator;

public class EdgeSrcXComparator implements Comparator<Edge> {
    @Override
    public int compare(Edge o1, Edge o2) {
        GraphNode o1point = o1.getSrc();
        GraphNode o2point = o2.getSrc();
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
