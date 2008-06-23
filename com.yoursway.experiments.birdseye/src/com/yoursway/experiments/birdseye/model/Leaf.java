package com.yoursway.experiments.birdseye.model;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

public class Leaf extends Node {
    
    private final double size;
    private final Color color;
    private final String label;
    
    public Leaf(double size, Color color, String label) {
        this.size = size;
        this.color = color;
        this.label = label;
    }
    
    @Override
    public double size() {
        return size;
    }
    
    public Color color() {
        return color;
    }
    
    public Leaf pick(Point point) {
        if (contains(point))
            return this;
        else
            return null;
    }

    public String label() {
        return label;
    }
    
}
