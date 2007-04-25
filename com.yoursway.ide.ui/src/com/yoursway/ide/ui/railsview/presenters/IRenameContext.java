package com.yoursway.ide.ui.railsview.presenters;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;

public interface IRenameContext {
    
    IWorkbenchPage getWorkbenchPage();
    
    TreeViewer getTreeViewer();
    
    Tree getTree();
    
    TreeItem getTreeItem();
    
    String getInitialValue();
    
    boolean isValidValue(String value);
    
    void setValue(String value);
    
}
