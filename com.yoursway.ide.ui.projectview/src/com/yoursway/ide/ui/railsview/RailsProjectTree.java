package com.yoursway.ide.ui.railsview;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;

import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.shit.IViewInfoProvider;
import com.yoursway.ide.ui.railsview.shit.ProjectPresentationProvider;
import com.yoursway.ide.ui.railsview.shit.rails.FakeProjectItem;
import com.yoursway.model.rails.IRailsApplicationProject;
import com.yoursway.model.repository.IConsumer;
import com.yoursway.model.repository.IResolver;

public class RailsProjectTree implements IPresenterOwner, IViewInfoProvider, IConsumer, IMenuListener {
    
    private final Tree tree;
    
    private final PublicMorozovTreeViewer viewer;
    
    private IRailsApplicationProject currentRailsApplicationProject;
    
    private final IRailsProjectTreeOwner owner;
    
    private String searchPatternString;
    
    private IResolver resolver;
    
    private MenuManager menuManager;
    
    private Menu contextMenu;
    
    private IPartListener fPartListener = new IPartListener() {
        public void partActivated(IWorkbenchPart part) {
            //            if (part instanceof IEditorPart)
            //                editorActivated((IEditorPart) part);
        }
        
        public void partBroughtToTop(IWorkbenchPart part) {
        }
        
        public void partClosed(IWorkbenchPart part) {
        }
        
        public void partDeactivated(IWorkbenchPart part) {
        }
        
        public void partOpened(IWorkbenchPart part) {
        }
    };
    
    public RailsProjectTree(Composite parent, IRailsProjectTreeOwner owner) {
        this.owner = owner;
        tree = new Tree(parent, SWT.BORDER | SWT.MULTI | SWT.VERTICAL | SWT.V_SCROLL);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        searchPatternString = "";
        
        viewer = new PublicMorozovTreeViewer(tree);
        
        menuManager = new MenuManager();
        contextMenu = menuManager.createContextMenu(tree);
        menuManager.setRemoveAllWhenShown(true);
        menuManager.addMenuListener(this);
        tree.setMenu(contextMenu);
        
        // Register viewer with site. This must be done before making the actions.
        IWorkbenchPartSite site = owner.getSite();
        site.registerContextMenu(menuManager, viewer);
        site.setSelectionProvider(viewer);
        site.getPage().addPartListener(fPartListener);
        
        ProjectPresentationProvider infoProvider = new ProjectPresentationProvider(this);
        infoProvider.install(viewer);
    }
    
    public void setVisibleProject(IRailsApplicationProject project) {
        currentRailsApplicationProject = project;
        viewer.setInput(new FakeProjectItem(currentRailsApplicationProject, this));
    }
    
    public void refresh() {
        viewer.refresh();
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
        if (text == null)
            throw new IllegalArgumentException();
        searchPatternString = text;
    }
    
    public String getPattern() {
        return searchPatternString;
    }
    
    public void consume(IResolver resolver) {
        this.resolver = resolver;
        resolver.dontKillForLaterAccess();
        viewer.refresh();
    }
    
    public IResolver getModelResolver() {
        return resolver;
    }
    
    public void menuAboutToShow(IMenuManager manager) {
        //TODO
        System.out.println("RailsProjectTree.menuAboutToShow()");
    }
    
}
