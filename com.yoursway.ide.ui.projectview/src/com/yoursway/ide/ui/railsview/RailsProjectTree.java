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
import com.yoursway.ide.ui.railsview.shit.FakeProjectItem;
import com.yoursway.ide.ui.railsview.shit.IViewInfoProvider;
import com.yoursway.ide.ui.railsview.shit.ProjectPresentationProvider;
import com.yoursway.model.rails.IRailsApplicationProject;
import com.yoursway.model.repository.IConsumer;
import com.yoursway.model.repository.IResolver;

public class RailsProjectTree implements IPresenterOwner, IViewInfoProvider, IConsumer {
    
    private final Tree tree;
    
    private final PublicMorozovTreeViewer viewer;
    
    private IRailsApplicationProject currentRailsApplicationProject;
    
    private final MenuManager contextMenuManager;
    
    private final IRailsProjectTreeOwner owner;
    
    private final PatternFilter patternFilter;

    private IResolver resolver;
    
    public RailsProjectTree(Composite parent, IRailsProjectTreeOwner owner) {
        this.owner = owner;
        tree = new Tree(parent, SWT.BORDER | SWT.MULTI | SWT.VERTICAL | SWT.V_SCROLL);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        patternFilter = new PatternFilter();
        viewer = new PublicMorozovTreeViewer(tree);
        
        ProjectPresentationProvider infoProvider = new ProjectPresentationProvider(this);
        infoProvider.install(viewer);
        
        contextMenuManager = null;
    }
    
    public void setVisibleProject(IRailsApplicationProject project) {
        currentRailsApplicationProject = project;
        viewer.setInput(new FakeProjectItem(currentRailsApplicationProject, this));
    }
    
    public void refresh() {
        if (currentRailsApplicationProject != null)
            viewer.refresh(currentRailsApplicationProject);
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

    public void consume(IResolver resolver) {
        this.resolver = resolver;
        viewer.refresh();
    }
    
    public IResolver getModelResolver() {
        return resolver;
    }
    
}
