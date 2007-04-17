package com.yoursway.ide.ui.railsview;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.yoursway.ide.ui.railsview.presentation.ElementPresenterFactory;
import com.yoursway.ide.ui.railsview.presentation.IElementPresenter;
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
    
    private final TreeEditor treeEditor;
    
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
        
        tree.addKeyListener(new KeyListener() {
            
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.F2 && e.stateMask == 0)
                    doRename(((StructuredSelection) viewer.getSelection()).getFirstElement());
            }
            
            public void keyReleased(KeyEvent e) {
            }
            
        });
        
        treeEditor = new TreeEditor(tree);
        treeEditor.horizontalAlignment = SWT.LEFT;
        treeEditor.verticalAlignment = SWT.TOP;
        treeEditor.grabHorizontal = true;
        treeEditor.minimumWidth = 100;
    }
    
    protected void handleDoubleClick(DoubleClickEvent event) {
        ISelection selection = viewer.getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj != null) {
            presenterFactory.createPresenter(obj).handleDoubleClick();
        }
    }
    
    protected void fillContextMenu(IMenuManager manager) {
        ISelection selection = viewer.getSelection();
        final Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj != null) {
            final IElementPresenter presenter = presenterFactory.createPresenter(obj);
            manager.add(new Action("Rename") {
                
                @Override
                public void run() {
                    doRename(presenter);
                }
                
            });
        }
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
    
    private void doRename(Object obj) {
        final IElementPresenter presenter = presenterFactory.createPresenter(obj);
        if (treeEditor.getEditor() != null)
            treeEditor.getEditor().dispose();
        
        TreeItem[] selItems = tree.getSelection();
        TreeItem treeItem = selItems[0];
        
        Composite editor = new Composite(tree, SWT.NONE);
        //editor.setBackground(new Color(null, 0, 0, 0));
        editor.setLayout(new GridLayout(1, true));
        Label cap = new Label(editor, SWT.NONE);
        cap.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        cap.setText("Rename " + presenter.getCaption() + " to:");
        Text text = new Text(editor, SWT.NONE);
        text.setText(presenter.getCaption());
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        Label info = new Label(editor, SWT.NONE);
        info.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        info.setText("ENTER to rename");
        text.selectAll();
        text.addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent e) {
                treeEditor.getEditor().dispose();
                treeEditor.setEditor(null);
                tree.setFocus();
            }
            
            public void widgetSelected(SelectionEvent e) {
            }
            
        });
        text.addKeyListener(new KeyListener() {
            
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.ESC) {
                    treeEditor.getEditor().dispose();
                    treeEditor.setEditor(null);
                    tree.setFocus();
                }
                
                System.out.println("keyPressed(" + e.keyCode + ")");
            }
            
            public void keyReleased(KeyEvent e) {
            }
            
        });
        text.addFocusListener(new FocusListener() {
            
            public void focusGained(FocusEvent e) {
            }
            
            public void focusLost(FocusEvent e) {
                treeEditor.getEditor().dispose();
                treeEditor.setEditor(null);
            }
            
        });
        final Point minSize = editor.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        treeEditor.minimumWidth = Math.max(100, minSize.x);
        treeEditor.minimumHeight = minSize.y;
        treeEditor.setEditor(editor, treeItem);
        editor.setFocus();
    }
    
}
