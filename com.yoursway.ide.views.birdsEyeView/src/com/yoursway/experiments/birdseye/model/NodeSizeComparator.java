/**
 * 
 */
package com.yoursway.experiments.birdseye.model;

import java.util.Comparator;

public final class NodeSizeComparator implements Comparator<Node> {
    public int compare(Node o1, Node o2) {
        return -Double.compare(o1.size(), o2.size());
    }
}