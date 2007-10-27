package com.yoursway.ide.ui.railsview.presentation;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPage;

public interface IPresenterOwner {
    
    IWorkbenchPage getWorkbenchPage();
    
    TreeViewer getTreeViewer();
    
    Tree getTree();
    
}
