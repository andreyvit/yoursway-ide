package com.yoursway.experiments.birdseye.model;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.binarySearch;
import static java.util.Collections.sort;

import java.util.Collection;
import java.util.List;

import org.eclipse.swt.graphics.Point;

public class Container extends Node {
    
    private final Node child;
    
    public Container(Collection<Node> children) {
        this.child = translateIntoCompartments(children);
    }

    private Node translateIntoCompartments(Collection<Node> children) {
        List<Node> elements = newArrayList(children);
        NodeSizeComparator comparator = new NodeSizeComparator();
        sort(elements, comparator);
        
        while(elements.size() > 1) {
            Node last = elements.remove(elements.size() - 1);
            Node prev = elements.remove(elements.size() - 1);
            Compartment compartment = new Compartment(prev, last);
            int index = binarySearch(elements, compartment, comparator);
            if (index < 0)
                index = -(index + 1);
            elements.add(index, compartment);
        }
        
        Node child = elements.get(0);
        if (child == null)
            throw new AssertionError("child is null");
        return child;
    }
    
    public Node child() {
        return child;
    }
    
    @Override
    public double size() {
        return child.size();
    }
    
    public Leaf pick(Point point) {
        return child.pick(point);
    }
    
}
