package com.yoursway.ide.ui.railsview;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.dialogs.PatternFilter;

import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.shit.ISearchPatternProvider;
import com.yoursway.ide.ui.railsview.shit.ProjectPresentationProvider;
import com.yoursway.model.rails.impl.RailsProject;

public class RailsProjectTree implements IPresenterOwner, ISearchPatternProvider {
    
    private final Tree tree;
    
    private final PublicMorozovTreeViewer viewer;
    
    private RailsProject currentRailsProject;
    
    private final MenuManager contextMenuManager;
    
    private final IRailsProjectTreeOwner owner;
    
    private final PatternFilter patternFilter;
    
    public RailsProjectTree(Composite parent, IRailsProjectTreeOwner owner) {
        this.owner = owner;
        tree = new Tree(parent, SWT.BORDER | SWT.MULTI | SWT.VERTICAL | SWT.V_SCROLL);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        patternFilter = new PatternFilter();
        viewer = new PublicMorozovTreeViewer(tree);
        
        ProjectPresentationProvider infoProvider = new ProjectPresentationProvider(this);
        infoProvider.install(viewer);
        viewer.setInput("adsf");
        
        contextMenuManager = null;
        
    }
    
    public void setVisibleProject(RailsProject project) {
        currentRailsProject = project;
        viewer.setInput(currentRailsProject);
    }
    
    public void refresh() {
        if (currentRailsProject != null)
            viewer.refresh(currentRailsProject);
    }
    
    public MenuManager getContextMenuManager() {
        return contextMenuManager;
    }
    
    public ISelectionProvider getSelectionProvider() {
        return viewer;
    }
    
    public IWorkbenchPage getWorkbenchPage() {
        return owner.getWorkbenchPage();
    }
    
    public void setFocus() {
        tree.setFocus();
    }
    
    public Tree getTree() {
        return tree;
    }
    
    public TreeViewer getTreeViewer() {
        return viewer;
    }
    
    public void setFilteringPattern(String text) {
        patternFilter.setPattern(text);
    }
    
    public String getPattern() {
        return "";
    }
}
