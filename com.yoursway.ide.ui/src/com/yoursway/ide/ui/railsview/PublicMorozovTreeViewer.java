package com.yoursway.ide.ui.railsview;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

public class PublicMorozovTreeViewer extends TreeViewer {
    
    public PublicMorozovTreeViewer(Composite parent, int style) {
        super(parent, style);
    }
    
    public PublicMorozovTreeViewer(Composite parent) {
        super(parent);
    }
    
    public PublicMorozovTreeViewer(Tree tree) {
        super(tree);
    }
    
    public TreeItem getCorrespondingWidget(Object model) {
        Widget item = findItem(model);
        if (item instanceof TreeItem)
            return (TreeItem) item;
        return null;
    }
    
}
