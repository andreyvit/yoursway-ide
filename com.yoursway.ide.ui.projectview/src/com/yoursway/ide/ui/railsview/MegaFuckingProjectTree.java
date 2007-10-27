package com.yoursway.ide.ui.railsview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import com.yoursway.model.rails.impl.RailsProject;

public class MegaFuckingProjectTree {
    
    private final IRailsProjectTreeOwner owner;
    private final Tree tree;
    private final PublicMorozovTreeViewer viewer;
    private boolean showAll;
    
    public MegaFuckingProjectTree(Composite parent, IRailsProjectTreeOwner owner) {
        this.owner = owner;
        tree = new Tree(parent, SWT.BORDER | SWT.MULTI | SWT.VERTICAL | SWT.V_SCROLL);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        viewer = new PublicMorozovTreeViewer(tree);
        //        viewer.setContentProvider(infoProvider);
        //        viewer.setLabelProvider(infoProvider);
        viewer.expandToLevel(null, 1);
    }
    
    public void setFilteringPattern(String text) {
        // TODO Auto-generated method stub
        
    }
    
    public void setFocus() {
        // TODO Auto-generated method stub
        
    }
    
    public void setVisibleProject(RailsProject project) {
        // TODO Auto-generated method stub
        
    }
    
    public void refresh() {
        
    }
    
    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }
    
}
