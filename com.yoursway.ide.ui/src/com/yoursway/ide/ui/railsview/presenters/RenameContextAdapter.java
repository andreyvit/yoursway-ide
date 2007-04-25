package com.yoursway.ide.ui.railsview.presenters;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;

import com.yoursway.ide.ui.railsview.presentation.IContextMenuContext;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.rails.model.IRailsController;

public class RenameContextAdapter implements IRenameContext {
    
    private final IPresenterOwner presenterOwner;
    private final IContextMenuContext contextMenuContext;
    private final IRailsController railsController;
    
    public RenameContextAdapter(IPresenterOwner presenterOwner, IContextMenuContext contextMenuContext,
            IRailsController railsController) {
        this.presenterOwner = presenterOwner;
        this.contextMenuContext = contextMenuContext;
        this.railsController = railsController;
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
    
    public IRailsController getRailsController() {
        return railsController;
    }
    
    public TreeItem getTreeItem() {
        return contextMenuContext.getTreeItem();
    }
    
}
