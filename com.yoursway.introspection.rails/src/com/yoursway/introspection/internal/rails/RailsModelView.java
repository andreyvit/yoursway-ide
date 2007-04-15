package com.yoursway.introspection.internal.rails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
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
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.yoursway.rails.model.IRails;
import com.yoursway.rails.model.IRailsAction;
import com.yoursway.rails.model.IRailsBaseView;
import com.yoursway.rails.model.IRailsChangeListener;
import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsField;
import com.yoursway.rails.model.IRailsModel;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.IRailsSchema;
import com.yoursway.rails.model.IRailsTable;
import com.yoursway.rails.model.RailsCore;
import com.yoursway.rails.model.deltas.RailsChangeEvent;
import com.yoursway.rails.model.deltas.project.RailsProjectDelta;
import com.yoursway.utils.RailsNamingConventions;

public class RailsModelView extends ViewPart {
    private TreeViewer viewer;
    private DrillDownAdapter drillDownAdapter;
    private Action action1;
    private Action doubleClickAction;
    
    private static final String MODEL_ICONS_PATH = "icons/rails_model/";
    public static final ImageDescriptor MODEL_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "model.gif");
    public static final ImageDescriptor PROJECT_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "project.gif");
    public static final ImageDescriptor CONTROLLER_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "controller.png");
    public static final ImageDescriptor CHANGE_ICON = Activator.getImageDescriptor("icons/" + "change.gif");
    
    public static final Image MODEL_ICON_IMG = MODEL_ICON.createImage();
    
    public static final Image PROJECT_ICON_IMG = PROJECT_ICON.createImage();
    
    public static final Image CONTROLLER_ICON_IMG = CONTROLLER_ICON.createImage();
    public static final Image CHANGE_ICON_IMG = CHANGE_ICON.createImage();
    
    private static final Object[] NO_CHILDREN = new Object[0];
    
    private final List<Change> recentChanges = new ArrayList<Change>();
    
    private final Set elementsChangedLastTime = new HashSet();
    
    private IRails theModel;
    
    private int nextChangeOrdinal = 1;
    
    class Change {
        
        private final RailsChangeEvent event;
        private final int ordinal;
        private final long timeMillis;
        
        public Change(RailsChangeEvent event, int ordinal, long timeMillis) {
            this.event = event;
            this.ordinal = ordinal;
            this.timeMillis = timeMillis;
        }
        
        public RailsChangeEvent getEvent() {
            return event;
        }
        
        public int getOrdinal() {
            return ordinal;
        }
        
        public long getTimeMillis() {
            return timeMillis;
        }
        
    }
    
    class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
        
        private Object input;
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            this.input = newInput;
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }
        
        public Object getParent(Object child) {
            if (child instanceof Change) {
                return theInput;
            }
            return null;
        }
        
        public Object[] getChildren(Object parent) {
            if (parent == input) {
                Collection<Object> result = new ArrayList<Object>();
                result.addAll(recentChanges);
                result.add(theModel);
                return result.toArray();
            } else if (parent instanceof IRails) {
                IRails rails = (IRails) parent;
                Collection<Object> result = new ArrayList<Object>();
                result.addAll(rails.getProjectsCollection().getRailsProjects());
                return result.toArray();
            } else if (parent instanceof IRailsProject) {
                IRailsProject railsProject = (IRailsProject) parent;
                Collection<Object> children = new ArrayList<Object>();
                children.addAll(railsProject.getControllersCollection().getItems());
                children.addAll(railsProject.getModelsCollection().getItems());
                children.add(railsProject.getSchema());
                return children.toArray();
            } else if (parent instanceof IRailsController) {
                IRailsController railsController = (IRailsController) parent;
                Collection<Object> children = new ArrayList<Object>();
                children.addAll(railsController.getActionsCollection().getActions());
                children.addAll(railsController.getViewsCollection().getItems());
                return children.toArray();
            } else if (parent instanceof IRailsSchema) {
                IRailsSchema railsSchema = (IRailsSchema) parent;
                Collection<Object> children = new ArrayList<Object>();
                children.addAll(railsSchema.getItems());
                return children.toArray();
            } else if (parent instanceof IRailsTable) {
                IRailsTable railsTable = (IRailsTable) parent;
                Collection<Object> children = new ArrayList<Object>();
                children.addAll(railsTable.getFields().getItems());
                return children.toArray();
            } else if (parent instanceof Change) {
                RailsChangeEvent event = ((Change) parent).getEvent();
                return event.getProjectDeltas();
            } else if (parent instanceof RailsProjectDelta) {
                RailsProjectDelta projectDelta = (RailsProjectDelta) parent;
                //                IModelElementDelta delta = event.getDelta();
                //                if (delta != null)
                //                    return new Object[] { delta };
                //            } else if (parent instanceof IModelElementDelta) {
                //                IModelElementDelta delta = (IModelElementDelta) parent;
                //                Collection result = new ArrayList();
                //                result.addAll(Arrays.asList(delta.getAddedChildren()));
                //                result.addAll(Arrays.asList(delta.getAffectedChildren()));
                //                return result.toArray();
            }
            
            return NO_CHILDREN;
        }
        
        public boolean hasChildren(Object parent) {
            if (parent instanceof IRailsProject) {
                return !((IRailsProject) parent).getControllersCollection().isEmpty();
            } else if (parent instanceof IRailsController) {
                IRailsController railsController = (IRailsController) parent;
                return railsController.getActionsCollection().hasItems();
            } else if (parent instanceof IRailsSchema) {
                IRailsSchema railsSchema = (IRailsSchema) parent;
                return railsSchema.hasItems();
            } else if (parent instanceof IRailsTable) {
                IRailsTable railsTable = (IRailsTable) parent;
                return railsTable.getFields().hasItems();
            }
            return getChildren(parent).length > 0;
        }
    }
    
    class ViewLabelProvider extends LabelProvider implements IFontProvider {
        
        @Override
        public Image getImage(Object element) {
            if (element instanceof IRails) {
                return MODEL_ICON_IMG;
            } else if (element instanceof IRailsProject) {
                return PROJECT_ICON_IMG;
            } else if (element instanceof IRailsController) {
                return CONTROLLER_ICON_IMG;
            } else if (element instanceof Change) {
                return CHANGE_ICON_IMG;
            }
            return null;
        }
        
        @Override
        public String getText(Object element) {
            String className = element.getClass().getSimpleName();
            if (element instanceof IRails) {
                return className;
            } else if (element instanceof IRailsProject) {
                IRailsProject railsProject = (IRailsProject) element;
                return className + ": " + railsProject.getProject().getName();
            } else if (element instanceof IRailsController) {
                IRailsController railsController = (IRailsController) element;
                String[] classNameComponents = railsController.getExpectedClassName();
                String className2 = RailsNamingConventions.joinNamespaces(classNameComponents);
                return className + ": " + className2 + " - "
                        + railsController.getFile().getProjectRelativePath();
            } else if (element instanceof IRailsAction) {
                IRailsAction railsAction = (IRailsAction) element;
                return className + ": " + railsAction.getName() + " - " + railsAction.getMethod().getName();
            } else if (element instanceof IRailsBaseView) {
                IRailsBaseView view = (IRailsBaseView) element;
                return className + ": " + view.getName() + " " + view.getFormat().toString();
            } else if (element instanceof IRailsModel) {
                IRailsModel railsModel = (IRailsModel) element;
                String[] classNameComponents = railsModel.getExpectedClassName();
                String className2 = RailsNamingConventions.joinNamespaces(classNameComponents);
                return className + ": " + className2 + " - "
                        + railsModel.getCorrespondingFile().getProjectRelativePath() + ", table "
                        + railsModel.getTableName();
            } else if (element instanceof IRailsSchema) {
                IRailsSchema railsSchema = (IRailsSchema) element;
                return className + ": version " + railsSchema.getVersion();
            } else if (element instanceof IRailsTable) {
                IRailsTable railsTable = (IRailsTable) element;
                return className + ": " + railsTable.getName();
            } else if (element instanceof IRailsField) {
                IRailsField railsField = (IRailsField) element;
                return className + ": " + railsField.getName() + " - " + railsField.getType();
            } else if (element instanceof Change) {
                Change change = (Change) element;
                RailsChangeEvent event = change.getEvent();
                return change.getOrdinal() + ") " + className + " at " + change.getTimeMillis();
                //            } else if (element instanceof IModelElementDelta) {
                //                IModelElementDelta delta = (IModelElementDelta) element;
                //                IModelElement modelElement = delta.getElement();
                //                StringBuilder result = new StringBuilder();
                //                result.append(deltaKindToString(delta)).append(' ').append(
                //                        modelElement.getClass().getSimpleName()).append(' ').append(
                //                        modelElement.getElementName());
                //                appendDeltaFlags(result, delta);
                //                return result.toString();
            } else if (element instanceof RailsProjectDelta) {
                RailsProjectDelta projectDelta = (RailsProjectDelta) element;
                String name = projectDelta.getRailsProject().getProject().getName();
                return className + ": " + name;
            }
            return className + " - " + element.toString();
        }
        
        public Font getFont(Object element) {
            if (elementsChangedLastTime.contains(element))
                return boldFont;
            return null;
        }
        
    }
    
    class ElementChangedListener implements IRailsChangeListener {
        
        public void install() {
            RailsCore.instance().addChangeListener(this);
        }
        
        public void uninstall() {
            RailsCore.instance().removeChangeListener(this);
        }
        
        public void railsModelChanged(final RailsChangeEvent event) {
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    addElementChangedEvent(event);
                }
            });
        }
        
    }
    
    private final ElementChangedListener elementChangedListener = new ElementChangedListener();
    private Font boldFont;
    private Object theInput;
    
    class NameSorter extends ViewerSorter {
    }
    
    /**
     * The constructor.
     */
    public RailsModelView() {
    }
    
    void addElementChangedEvent(RailsChangeEvent event) {
        while (recentChanges.size() > 4) {
            Object old = recentChanges.remove(recentChanges.size() - 1);
            viewer.remove(old);
        }
        Change change = new Change(event, nextChangeOrdinal++, System.currentTimeMillis());
        recentChanges.add(0, change);
        elementsChangedLastTime.clear();
        //        visit(event.getDelta());
        viewer.refresh(theModel);
        //        for (Iterator iterator = elementsChangedLastTime.iterator(); iterator.hasNext();) {
        //            IModelElement el = (IModelElement) iterator.next();
        //            viewer.expandToLevel(el, 0);
        //        }
        viewer.insert(theInput, change, 0);
    }
    
    //    private void visit(IModelElementDelta delta) {
    //        elementsChangedLastTime.add(delta.getElement());
    //        visit(delta.getAddedChildren());
    //        visit(delta.getAddedChildren());
    //    }
    //    
    //    private void visit(IModelElementDelta[] addedChildren) {
    //        for (int i = 0; i < addedChildren.length; i++) {
    //            visit(addedChildren[i]);
    //        }
    //    }
    
    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl(Composite parent) {
        theModel = RailsCore.instance();
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
    
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                RailsModelView.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }
    
    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }
    
    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(action1);
        //        manager.add(new Separator());
    }
    
    private void fillContextMenu(IMenuManager manager) {
        manager.add(action1);
        manager.add(new Separator());
        drillDownAdapter.addNavigationActions(manager);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(action1);
        manager.add(new Separator());
        drillDownAdapter.addNavigationActions(manager);
    }
    
    private void makeActions() {
        action1 = new Action() {
            @Override
            public void run() {
                RailsCore.instance().refresh();
                viewer.refresh(theModel);
            }
        };
        action1.setText("Refresh");
        action1.setToolTipText("Rebuild the entire Rails model");
        action1.setImageDescriptor(Activator.getImageDescriptor("icons/rails_model_view.gif"));
        
        doubleClickAction = new Action() {
            @Override
            public void run() {
                //                ISelection selection = viewer.getSelection();
                //                Object obj = ((IStructuredSelection) selection).getFirstElement();
                //                showMessage("Double-click detected on " + obj.toString());
            }
        };
    }
    
    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                doubleClickAction.run();
            }
        });
    }
    
    private void showMessage(String message) {
        MessageDialog.openInformation(viewer.getControl().getShell(), "Rails Model", message);
    }
    
    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }
    
}