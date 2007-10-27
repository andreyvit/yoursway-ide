package com.yoursway.ide.ui.railsview;

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

import com.yoursway.model.rails.IRailsModelRoot;
import com.yoursway.model.rails.IRailsProject;
import com.yoursway.model.rails.impl.RailsModelRoot;
import com.yoursway.model.rails.impl.RogerRabbitResolver;
import com.yoursway.model.repository.DependencyRequestor;
import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.repository.IModelRootProvider;

public class RailsProjectView extends ViewPart implements IRailsProjectTreeOwner {
    
    public static final String ID = "com.yoursway.ide.ui.RailsProjectView";
    
    private static IRailsModelRoot railsModelRoot = new RailsModelRoot();
    
    private Text searchTextControl;
    private RailsProjectTree projectTree;
    private Composite body;
    
    private RailsProjectStateComposite projectStateComposite;
    
    private RogerRabbitResolver resolver;
    
    @Override
    public void createPartControl(final Composite parent) {
        body = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.makeColumnsEqualWidth = false;
        layout.verticalSpacing = 1;
        body.setLayout(layout);
        
        createSearchTextControl(body);
        
        resolver = new RogerRabbitResolver(new IModelRootProvider() {
            
            public <V extends IModelRoot> V obtainRoot(Class<V> rootHandleInterface) {
                return (V) railsModelRoot;
            }
            
        }, new DependencyRequestor() {
            
            public void dependency(IHandle<?> handle) {
            }
            
        });
        
        projectTree = new RailsProjectTree(body, this);
        projectTree.consume(resolver);
        
        IRailsModelRoot root = resolver.obtainRoot(IRailsModelRoot.class);
        ICollectionHandle<IRailsProject> projects = root.projects();
        Collection<IRailsProject> collection = resolver.get(projects);
        IRailsProject project = collection.iterator().next();
        
        projectTree.setVisibleProject(project);
        
        createBottomControls(body);
        
        contributeToActionBars();
    }
    
    private void createBottomControls(Composite parent) {
        projectStateComposite = new RailsProjectStateComposite(parent, SWT.NONE, new StoppedServerState());
        projectStateComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    }
    
    private void createSearchTextControl(Composite parent) {
        searchTextControl = new Text(parent, SWT.SEARCH | SWT.CANCEL);
        searchTextControl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL));
        searchTextControl.setText("");
        searchTextControl.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                if (e.getSource() == searchTextControl) {
                    Text text = (Text) e.getSource();
                    text.redraw();
                    projectTree.setFilteringPattern(text.getText());
                    projectTree.refresh();
                    projectTree.getTreeViewer().expandToLevel(TreeViewer.ALL_LEVELS);
                }
            }
            
        });
        searchTextControl.addPaintListener(new PaintListener() {
            
            public void paintControl(PaintEvent e) {
                if (e.getSource() == searchTextControl && searchTextControl.getText().length() == 0
                        && !searchTextControl.isFocusControl()) {
                    GC gc = e.gc;
                    Color oldForeground = gc.getForeground();
                    gc.setForeground(e.display.getSystemColor(SWT.COLOR_GRAY));
                    int y = (e.height - gc.getFontMetrics().getHeight()) / 2 - 3;
                    int x = e.height / 2;
                    gc.drawText("Pattern matching", x, y);
                    gc.setForeground(oldForeground);
                }
            }
            
        });
    }
    
    @Override
    public void setFocus() {
        // projectTree.setFocus();
    }
    
    public IWorkbenchPage getWorkbenchPage() {
        return getSite().getPage();
    }
    
    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }
    
    private void fillLocalPullDown(IMenuManager manager) {
    }
    
    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(new Action("More magic", null) { // TODO: add nornal icon
                
                    @Override
                    public void run() {
                        projectTree.consume(resolver);
                    }
                    
                });
    }
}
