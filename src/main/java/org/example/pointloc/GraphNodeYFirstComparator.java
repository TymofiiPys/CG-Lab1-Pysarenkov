package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class GraphNodeYFirstComparator implements Comparator<GraphNode> {

    @Override
    public int compare(GraphNode o1, GraphNode o2) {
        if(o1.getY() > o2.getY())
            return 1;
        if(o1.getY() < o2.getY())
            return -1;
        if(o1.getX() > o2.getX())
            return 1;
        if(o1.getX() < o2.getX())
            return -1;
        return 0;
    }
}
