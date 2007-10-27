package com.yoursway.ide.ui.railsview.presenters;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;

import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.presentation.IProvidesTreeItem;

public abstract class RenameContextAdapter implements IRenameContext {
    
    private final IPresenterOwner presenterOwner;
    private final IProvidesTreeItem contextMenuContext;
    
    public RenameContextAdapter(IPresenterOwner presenterOwner, IProvidesTreeItem contextMenuContext) {
        this.presenterOwner = presenterOwner;
        this.contextMenuContext = contextMenuContext;
    }
    
    public Tree getTree() {
        return presenterOwner.getTree();
    }
    
    public TreeViewer getTreeViewer() {
        return presenterOwner.getTreeViewer();
    }
    
    public IWorkbenchPage getWorkbenchPage() {
        return presenterOwner.getWorkbenchPage();
    }
    
    public TreeItem getTreeItem() {
        return contextMenuContext.getTreeItem();
    }
    
}
