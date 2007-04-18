package com.yoursway.utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SWTUtils {

    public static void recursiveSetBackgroundColor(Control control, Color color) {
        control.setBackground(color);
        if (control instanceof Composite) {
            Control[] children = ((Composite) control).getChildren();
            for (int i = 0; i < children.length; i++) {
                recursiveSetBackgroundColor(children[i], color);
            }
        }
    }
    
}
