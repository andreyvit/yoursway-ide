package com.yoursway.ide.ui.railsview.presentation.misc;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

public interface IPopupOwner {
    
    IWorkbenchWindow getWorkbenchWindow();
    
    Shell getShell();
    
    Composite getParent();
    
    Point getSnapPosition(int snapPosition);
    
}
