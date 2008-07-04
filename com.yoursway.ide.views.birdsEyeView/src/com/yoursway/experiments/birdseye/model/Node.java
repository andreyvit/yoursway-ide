package com.yoursway.experiments.birdseye.model;

import static com.yoursway.swt.additions.YsSwtGeometry.emptyRectangle;
import static com.yoursway.swt.additions.YsSwtGeometry.set;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public abstract class Node {
    
    private Rectangle rectangle = emptyRectangle();
    
    public abstract double size();
    
    protected boolean contains(Point point) {
        return rectangle.contains(point);
    }
    
    public void setRectangle(Rectangle rectangle) {
        set(this.rectangle, rectangle);
    }

    public abstract Leaf pick(Point point);
    
}
