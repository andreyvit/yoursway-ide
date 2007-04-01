package com.yoursway.introspection.internal.dltk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.ElementChangedEvent;
import org.eclipse.dltk.core.IElementChangedListener;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelElementDelta;
import org.eclipse.dltk.core.IParent;
import org.eclipse.dltk.core.IScriptModel;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.yoursway.utils.StringListBuilder;

public class DLTKModelView extends ViewPart {
    class Change {
        
        private final ElementChangedEvent event;
        private final int ordinal;
        private final long timeMillis;
        
        public Change(ElementChangedEvent event, int ordinal, long timeMillis) {
            this.event = event;
            this.ordinal = ordinal;
            this.timeMillis = timeMillis;
        }
        
        public ElementChangedEvent getEvent() {
            return event;
        }
        
        public int getOrdinal() {
            return ordinal;
        }
        
        public long getTimeMillis() {
            return timeMillis;
        }
        
    }
    
    class ElementChangedListener implements IElementChangedListener {
        
        public void elementChanged(final ElementChangedEvent event) {
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    addElementChangedEvent(event);
                }
            });
        }
        
        public void install() {
            DLTKCore.addElementChangedListener(this);
        }
        
        public void uninstall() {
            DLTKCore.removeElementChangedListener(this);
        }
        
    }
    
    class NameSorter extends ViewerSorter {
    }
    
    class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
        
        private Object input;
        
        public void dispose() {
        }
        
        public Object[] getChildren(Object parent) {
            if (parent == input) {
                Collection result = new ArrayList();
                result.addAll(recentChanges);
                result.add(theModel);
                return result.toArray();
            } else if (parent instanceof IParent) {
                IParent p = (IParent) parent;
                try {
                    IModelElement[] children = p.getChildren();
                    for (int i = 0; i < children.length; i++) {
                        IModelElement child = children[i];
                        if (child == null)
                            System.out.println("ViewContentProvider.getChildren()");
                    }
                    return children;
                } catch (ModelException e) {
                    Activator.log(e);
                }
            } else if (parent instanceof Change) {
                ElementChangedEvent event = ((Change) parent).getEvent();
                IModelElementDelta delta = event.getDelta();
                if (delta != null)
                    return new Object[] { delta };
            } else if (parent instanceof IModelElementDelta) {
                IModelElementDelta delta = (IModelElementDelta) parent;
                Collection result = new ArrayList();
                result.addAll(Arrays.asList(delta.getAddedChildren()));
                result.addAll(Arrays.asList(delta.getAffectedChildren()));
                return result.toArray();
            }
            
            return NO_CHILDREN;
        }
        
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }
        
        public Object getParent(Object child) {
            if (child instanceof IModelElement) {
                IModelElement modelElement = (IModelElement) child;
                return modelElement.getParent();
            } else if (child instanceof Change) {
                return theInput;
            }
            return null;
        }
        
        public boolean hasChildren(Object parent) {
            if (parent instanceof IParent) {
                IParent p = (IParent) parent;
                try {
                    return p.hasChildren();
                } catch (ModelException e) {
                    Activator.log(e);
                }
            } else if (parent instanceof Change || parent instanceof IModelElementDelta) {
                return getChildren(parent).length > 0;
            }
            return false;
        }
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            this.input = newInput;
        }
    }
    
    class ViewLabelProvider extends LabelProvider implements IFontProvider {
        
        public Font getFont(Object element) {
            if (elementsChangedLastTime.contains(element))
                return boldFont;
            return null;
        }
        
        @Override
        public Image getImage(Object element) {
            return null;
        }
        
        @Override
        public String getText(Object element) {
            if (element instanceof IModelElement) {
                IModelElement el = (IModelElement) element;
                String elName = el.getElementName();
                int type = el.getElementType();
                switch (type) {
                case IModelElement.SOURCE_MODULE:
                    if (((ISourceModule) el).isWorkingCopy())
                        elName = elName + " [WC]";
                    break;
                }
                return el.getClass().getSimpleName() + ": " + elName;
            } else if (element instanceof Change) {
                Change change = (Change) element;
                ElementChangedEvent event = change.getEvent();
                int type = event.getType();
                StringListBuilder builder = new StringListBuilder(StringListBuilder.SPACE);
                if ((type & ElementChangedEvent.POST_CHANGE) != 0)
                    builder.append("POST_CHANGE");
                if ((type & ElementChangedEvent.POST_RECONCILE) != 0)
                    builder.append("POST_RECONCILE");
                return change.getOrdinal() + ") " + event.getClass().getSimpleName() + ": "
                        + builder.toString() + " at " + change.getTimeMillis();
            } else if (element instanceof IModelElementDelta) {
                IModelElementDelta delta = (IModelElementDelta) element;
                IModelElement modelElement = delta.getElement();
                StringBuilder result = new StringBuilder();
                result.append(DLTKModelPresentationUtils.deltaKindToString(delta)).append(' ').append(
                        modelElement.getClass().getSimpleName()).append(' ').append(
                        modelElement.getElementName());
                DLTKModelPresentationUtils.appendDeltaFlags(result, delta);
                return result.toString();
            }
            return element.getClass().getSimpleName() + " - " + element.toString();
        }
        
    }
    
    private static final Object[] NO_CHILDREN = new Object[0];
    
    private TreeViewer viewer;
    
    private DrillDownAdapter drillDownAdapter;
    
    private Action action1;
    
    private Action action2;
    
    private Action doubleClickAction;
    
    private final List recentChanges = new ArrayList();
    
    private final Set elementsChangedLastTime = new HashSet();
    
    private IScriptModel theModel;
    
    private int nextChangeOrdinal = 1;
    private final ElementChangedListener elementChangedListener = new ElementChangedListener();
    private Font boldFont;
    
    private Object theInput;
    
    /**
     * The constructor.
     */
    public DLTKModelView() {
    }
    
    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl(Composite parent) {
        theModel = DLTKCore.create(ResourcesPlugin.getWorkspace().getRoot());
        theInput = new Object();
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        drillDownAdapter = new DrillDownAdapter(viewer);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        //        viewer.setSorter(new NameSorter());
        viewer.setInput(theInput);
        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
        elementChangedListener.install();
        Font font = viewer.getTree().getFont();
        FontData[] fontData = font.getFontData();
        fontData[0].setStyle(SWT.BOLD);
        boldFont = new Font(null, fontData[0]);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        elementChangedListener.uninstall();
        boldFont.dispose();
    }
    
    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }
    
    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }
    
    private void fillContextMenu(IMenuManager manager) {
        manager.add(action1);
        manager.add(action2);
        manager.add(new Separator());
        drillDownAdapter.addNavigationActions(manager);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(action1);
        manager.add(new Separator());
        manager.add(action2);
    }
    
    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(action1);
        manager.add(action2);
        manager.add(new Separator());
        drillDownAdapter.addNavigationActions(manager);
    }
    
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                DLTKModelView.this.fillContextMenu(manager);
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
                showMessage("Action 1 executed");
            }
        };
        action1.setText("Action 1");
        action1.setToolTipText("Action 1 tooltip");
        action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
                ISharedImages.IMG_OBJS_INFO_TSK));
        
        action2 = new Action() {
            @Override
            public void run() {
                showMessage("Action 2 executed");
            }
        };
        action2.setText("Action 2");
        action2.setToolTipText("Action 2 tooltip");
        action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
                ISharedImages.IMG_OBJS_INFO_TSK));
        doubleClickAction = new Action() {
            @Override
            public void run() {
                ISelection selection = viewer.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                showMessage("Double-click detected on " + obj.toString());
            }
        };
    }
    
    private void showMessage(String message) {
        MessageDialog.openInformation(viewer.getControl().getShell(), "DLTK Model", message);
    }
    
    private void visit(IModelElementDelta delta) {
        elementsChangedLastTime.add(delta.getElement());
        visit(delta.getAddedChildren());
        visit(delta.getAddedChildren());
    }
    
    private void visit(IModelElementDelta[] addedChildren) {
        for (int i = 0; i < addedChildren.length; i++) {
            visit(addedChildren[i]);
        }
    }
    
    void addElementChangedEvent(ElementChangedEvent event) {
        while (recentChanges.size() > 4) {
            Object old = recentChanges.remove(recentChanges.size() - 1);
            viewer.remove(old);
        }
        Change change = new Change(event, nextChangeOrdinal++, System.currentTimeMillis());
        recentChanges.add(0, change);
        elementsChangedLastTime.clear();
        visit(event.getDelta());
        viewer.refresh(theModel);
        for (Iterator iterator = elementsChangedLastTime.iterator(); iterator.hasNext();) {
            IModelElement el = (IModelElement) iterator.next();
            viewer.expandToLevel(el, 0);
        }
        viewer.insert(theInput, change, 0);
    }
}