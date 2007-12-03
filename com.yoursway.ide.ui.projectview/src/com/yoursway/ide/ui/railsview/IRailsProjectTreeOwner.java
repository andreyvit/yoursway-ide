package com.yoursway.ide.ui.railsview;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;

public interface IRailsProjectTreeOwner {
    
    IWorkbenchPage getWorkbenchPage();
    
    IWorkbenchPartSite getSite();
    
}
