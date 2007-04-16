package com.yoursway.ide.ui.railsview;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.yoursway.ide.ui.railsview.presentation.ElementPresenterFactory;
import com.yoursway.ide.ui.railsview.presentation.IPresenterFactory;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.presentation.PresentersBasedProvider;
import com.yoursway.rails.model.IRailsProject;

public class RailsProjectTree implements IPresenterOwner {
    
    private final IPresenterFactory presenterFactory = new ElementPresenterFactory(this);
    
    private final PresentersBasedProvider infoProvider = new PresentersBasedProvider(presenterFactory);
    
    private final Tree tree;
    
    private final TreeViewer viewer;
    
    private IRailsProject currentRailsProject;
    
    private final MenuManager contextMenuManager;
    
    private final IRailsProjectTreeOwner owner;
    
    public RailsProjectTree(Composite parent, FormToolkit formToolkit, IRailsProjectTreeOwner owner) {
        this.owner = owner;
        tree = formToolkit.createTree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        viewer = new TreeViewer(tree);
        viewer.setContentProvider(infoProvider);
        viewer.setLabelProvider(infoProvider);
        
        contextMenuManager = new MenuManager("#PopupMenu");
        contextMenuManager.setRemoveAllWhenShown(true);
        contextMenuManager.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        tree.setMenu(contextMenuManager.createContextMenu(tree));
        
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                handleDoubleClick(event);
            }
        });
    }
    
    protected void handleDoubleClick(DoubleClickEvent event) {
        ISelection selection = viewer.getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj != null) {
            presenterFactory.createPresenter(obj).handleDoubleClick();
        }
    }
    
    protected void fillContextMenu(IMenuManager manager) {
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    public void setVisibleProject(IRailsProject railsProject) {
        currentRailsProject = railsProject;
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
    
}
