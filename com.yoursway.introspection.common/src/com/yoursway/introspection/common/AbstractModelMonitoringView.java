package com.yoursway.introspection.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

public abstract class AbstractModelMonitoringView extends ViewPart {
    
    public class AbstractChange {
        
        private final int ordinal;
        private final long timeMillis;
        
        public AbstractChange() {
            this.ordinal = nextChangeOrdinal++;
            this.timeMillis = System.currentTimeMillis();
        }
        
        public int getOrdinal() {
            return ordinal;
        }
        
        public long getTimeMillis() {
            return timeMillis;
        }
        
    }
    
    public abstract class BaseLabelProvider extends LabelProvider implements IFontProvider {
        
        public Font getFont(Object element) {
            if (elementsChangedLastTime.contains(element))
                return boldFont;
            return null;
        }
        
    }
    
    protected static final Object[] NO_CHILDREN = new Object[0];
    
    private TreeViewer viewer;
    private DrillDownAdapter drillDownAdapter;
    private Action action1;
    private Action doubleClickAction;
    private final List<AbstractChange> recentChanges = new ArrayList<AbstractChange>();
    private final Set<Object> elementsChangedLastTime = new HashSet<Object>();
    private int nextChangeOrdinal = 1;
    
    private Font boldFont;
    
    protected Object theInput;
    
    public void addElementChangedEvent(final AbstractChange change) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                addElementChangedEvent2(change);
            }
        });
    }
    
    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl(Composite parent) {
        theInput = new Object();
        connectToModel();
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        drillDownAdapter = new DrillDownAdapter(viewer);
        setupTreeViewer(viewer);
        //        viewer.setSorter(new NameSorter());
        viewer.setInput(theInput);
        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
        Font font = viewer.getTree().getFont();
        FontData[] fontData = font.getFontData();
        fontData[0].setStyle(SWT.BOLD);
        boldFont = new Font(null, fontData[0]);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        boldFont.dispose();
    }
    
    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }
    
    private void addElementChangedEvent2(AbstractChange change) {
        while (recentChanges.size() > 4) {
            Object old = recentChanges.remove(recentChanges.size() - 1);
            viewer.remove(old);
        }
        recentChanges.add(0, change);
        elementsChangedLastTime.clear();
        findChangedItems(change);
        viewer.refresh(getModelRoot());
        for (Iterator<Object> iterator = elementsChangedLastTime.iterator(); iterator.hasNext();) {
            Object el = iterator.next();
            viewer.expandToLevel(el, 0);
        }
        viewer.insert(theInput, change, 0);
        if (recentChanges.size() > 2) {
            int size = recentChanges.size();
            for (int i = 2; i < size; i++)
                viewer.collapseToLevel(recentChanges.get(i), TreeViewer.ALL_LEVELS);
        }
        viewer.expandToLevel(change, TreeViewer.ALL_LEVELS);
    }
    
    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }
    
    private void fillContextMenu(IMenuManager manager) {
        manager.add(action1);
        manager.add(new Separator());
        drillDownAdapter.addNavigationActions(manager);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(action1);
    }
    
    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(action1);
        manager.add(new Separator());
        drillDownAdapter.addNavigationActions(manager);
    }
    
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }
    
    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                doubleClickAction.run();
            }
        });
    }
    
    private void makeActions() {
        action1 = new Action() {
            @Override
            public void run() {
                refreshModel();
                viewer.refresh(getModelRoot());
            }
        };
        action1.setText("Refresh");
        action1.setToolTipText("Rebuilds the model, if possible");
        action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
                ISharedImages.IMG_OBJS_INFO_TSK));
        doubleClickAction = new Action() {
            @Override
            public void run() {
                IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                if (selection != null) {
                    Object firstElement = selection.getFirstElement();
                    handleDoubleClick(firstElement);
                }
            }
        };
    }
    
    protected void addRecentChangesTo(Collection<Object> result) {
        result.addAll(recentChanges);
    }
    
    protected abstract void connectToModel();
    
    protected abstract void findChangedItems(AbstractChange change);
    
    protected abstract Object getModelRoot();
    
    protected abstract void handleDoubleClick(Object firstElement);
    
    protected abstract void refreshModel();
    
    protected void reportChangedElement(Object element) {
        elementsChangedLastTime.add(element);
    }
    
    protected abstract void setupTreeViewer(TreeViewer viewer);
    
}
