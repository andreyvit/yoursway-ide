package com.yoursway.ui.popup;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * A callback interface to be implemented by the party that displays the popup.
 * 
 * @author Andrey Tarantsov
 */
public interface IPopupHost {
    
    /**
     * Returns the shell over which the popup should pop up.
     */
    Shell getShell();
    
    /**
     * Returns the workbench window corresponding to the shell returned by
     * <code>getShell</code> (there's some redundancy here, huh?)
     */
    IWorkbenchWindow getWorkbenchWindow();
    
    /**
     * Returns the composite over which the popup is being displayed. This
     * information is used by the popup for two main purposes: for parenting and
     * manipulating SWT tracker and for obtaining color information.
     * 
     * Hopefully this method will go away in the future.
     * 
     * @return
     */
    Composite getParent();
    
}
