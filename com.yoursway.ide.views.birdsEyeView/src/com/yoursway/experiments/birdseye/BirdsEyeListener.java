package com.yoursway.experiments.birdseye;

import org.eclipse.swt.widgets.Event;

import com.yoursway.experiments.birdseye.model.Leaf;

public interface BirdsEyeListener {
    
    void birdsEyeHovered(Leaf node, Event event);
    
}
