package com.yoursway.ide.ui.railsview.presenters;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;

import com.yoursway.rails.model.IRailsController;

public interface IRenameContext {
    
    IWorkbenchPage getWorkbenchPage();
    
    TreeViewer getTreeViewer();
    
    Tree getTree();
    
    TreeItem getTreeItem();
    
    IRailsController getRailsController();
    
}
