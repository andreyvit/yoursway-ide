package com.yoursway.experiments.birdseye.model;

import org.eclipse.swt.graphics.Point;

public class Compartment extends Node {
    
    private final Node first;
    private final Node second;
    private double size;

    public Compartment(Node first, Node second) {
        if (first == null)
            throw new NullPointerException("first is null");
        if (second == null)
            throw new NullPointerException("second is null");
        this.first = first;
        this.second = second;
        this.size = first.size() + second.size();
    }
    
    public Node first() {
        return first;
    }
    
    public Node second() {
        return second;
    }

    @Override
    public Leaf pick(Point point) {
        if (!contains(point))
            return null;
        Leaf result = first.pick(point);
        if (result != null)
            return result;
        result = second.pick(point);
        if (result != null)
            return result;
        throw new AssertionError("Container contains " + point + ", but no children contain it.");
    }

    @Override
    public double size() {
        return size;
    }
    
}
