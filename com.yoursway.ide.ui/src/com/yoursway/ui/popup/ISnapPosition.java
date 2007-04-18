package com.yoursway.ui.popup;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public interface ISnapPosition {
    
    public abstract Point calculate(Point snappeeSize, Composite parentControl);
    
}