package com.yoursway.ide.ui.railsview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IFile;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

import com.yoursway.ide.ui.Activator;
import com.yoursway.ide.ui.advisor.FormLayoutFactory;
import com.yoursway.rails.model.IRails;
import com.yoursway.rails.model.IRailsAction;
import com.yoursway.rails.model.IRailsBaseView;
import com.yoursway.rails.model.IRailsChangeListener;
import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsPartial;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.IRailsView;
import com.yoursway.rails.model.RailsCore;
import com.yoursway.rails.model.deltas.RailsChangeEvent;
import com.yoursway.rails.windowmodel.IRailsWindowModelListener;
import com.yoursway.rails.windowmodel.RailsWindowModel;
import com.yoursway.rails.windowmodel.RailsWindowModelChange;
import com.yoursway.utils.RailsNamingConventions;

public class RailsView extends ViewPart {
    
    public static final String ID = "com.yoursway.ide.ui.RailsView";
    
    private TreeViewer viewer;
    private Action action1;
    private Action doubleClickAction;
    
    private static final String MODEL_ICONS_PATH = "icons/rails_model/";
    public static final ImageDescriptor MODEL_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "model.gif");
    public static final ImageDescriptor PROJECT_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "project.gif");
    public static final ImageDescriptor CONTROLLER_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "controller.png");
    public static final ImageDescriptor VIEW_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "view.png");
    public static final ImageDescriptor PARTIAL_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "partial.png");
    public static final ImageDescriptor ACTION_ICON = Activator.getImageDescriptor(MODEL_ICONS_PATH
            + "base.png");
    
    public static final Image MODEL_ICON_IMG = MODEL_ICON.createImage();
    
    public static final Image PROJECT_ICON_IMG = PROJECT_ICON.createImage();
    
    public static final Image CONTROLLER_ICON_IMG = CONTROLLER_ICON.createImage();
    public static final Image VIEW_ICON_IMG = VIEW_ICON.createImage();
    public static final Image PARTIAL_ICON_IMG = PARTIAL_ICON.createImage();
    public static final Image ACTION_ICON_IMG = ACTION_ICON.createImage();
    
    private static final Object[] NO_CHILDREN = new Object[0];
    
    private IRails theModel;
    
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
            return null;
        }
        
        public Object[] getChildren(Object parent) {
            if (parent instanceof IRails) {
                IRails rails = (IRails) parent;
                Collection<Object> result = new ArrayList<Object>();
                result.addAll(rails.getProjectsCollection().getRailsProjects());
                return result.toArray();
            } else if (parent instanceof IRailsProject) {
                IRailsProject railsProject = (IRailsProject) parent;
                Collection<Object> children = new ArrayList<Object>();
                children.addAll(railsProject.getControllersCollection().getItems());
                return children.toArray();
            } else if (parent instanceof IRailsController) {
                IRailsController railsController = (IRailsController) parent;
                Collection<Object> children = new ArrayList<Object>();
                children.addAll(railsController.getActionsCollection().getActions());
                children.addAll(railsController.getViewsCollection().getItems());
                return children.toArray();
            }
            
            return NO_CHILDREN;
        }
        
        public boolean hasChildren(Object parent) {
            if (parent instanceof IRailsProject) {
                return !((IRailsProject) parent).getControllersCollection().isEmpty();
            } else if (parent instanceof IRailsController) {
                IRailsController railsController = (IRailsController) parent;
                return railsController.getActionsCollection().hasItems();
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
            } else if (element instanceof IRailsAction) {
                return ACTION_ICON_IMG;
            } else if (element instanceof IRailsView) {
                return VIEW_ICON_IMG;
            } else if (element instanceof IRailsPartial) {
                return PARTIAL_ICON_IMG;
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
                return railsProject.getProject().getName();
            } else if (element instanceof IRailsController) {
                IRailsController railsController = (IRailsController) element;
                String[] classNameComponents = railsController.getExpectedClassName();
                String className2 = RailsNamingConventions.joinNamespaces(classNameComponents);
                return className2;
            } else if (element instanceof IRailsAction) {
                IRailsAction railsAction = (IRailsAction) element;
                return railsAction.getName();
            } else if (element instanceof IRailsBaseView) {
                IRailsBaseView view = (IRailsBaseView) element;
                return view.getName() + " (" + view.getFormat().toString() + ")";
            }
            return "UNKNOWN " + className + " - " + element.toString();
        }
        
        public Font getFont(Object element) {
            if (false)
                return boldFont;
            return null;
        }
        
    }
    
    class WindowModelListener implements IRailsWindowModelListener {
        
        public void install() {
            RailsWindowModel.instance().addListener(this);
        }
        
        public void uninstall() {
            RailsWindowModel.instance().removeListener(this);
        }
        
        public void mappingChanged(RailsWindowModelChange event) {
            if (event.getWindow() == getSite().getWorkbenchWindow())
                activeProjectChanged();
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
    private final WindowModelListener windowModelListener = new WindowModelListener();
    private Font boldFont;
    private Object theInput;
    private List<IRailsProject> chooserProjects;
    private Combo projectChooser;
    
    private ExpandableComposite expander;
    
    private FormToolkit formToolkit;
    
    private Form form;
    
    private Composite expanderComposite;
    
    class NameSorter extends ViewerSorter {
    }
    
    public RailsView() {
    }
    
    public void activeProjectChanged() {
        IRailsProject project = RailsWindowModel.instance().getProject(getSite().getWorkbenchWindow());
        viewer.setInput(project);
        updateProjectChooser();
        if (project == null) {
            if (chooserProjects.isEmpty())
                expander.setText("Create or import a project");
            else
                expander.setText("Choose a project");
            expander.setExpanded(true);
        } else {
            expander.setText(project.getProject().getName());
        }
    }
    
    void addElementChangedEvent(RailsChangeEvent event) {
        viewer.refresh(theModel);
        updateProjectChooser();
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
    
    @Override
    public void createPartControl(Composite parent) {
        theModel = RailsCore.instance();
        theInput = new Object();
        
        //        Composite composite = new Composite(parent, SWT.NONE);
        //        composite.setLayout(new GridLayout(1, true));
        
        //        GridData chooserData = new GridData(SWT.FILL, SWT.TOP, true, false);
        //        GridData treeData = new GridData(SWT.FILL, SWT.FILL, true, true);
        //        
        formToolkit = new FormToolkit(getSite().getShell().getDisplay());
        form = formToolkit.createForm(parent);
        //        form.setLayoutData(treeData);
        Composite formBody = form.getBody();
        formBody.setLayout(FormLayoutFactory.createFormGridLayout(false, 1));
        
        expander = formToolkit.createSection(formBody, ExpandableComposite.TWISTIE
                | ExpandableComposite.CLIENT_INDENT);
        expander.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        expander.setText("Choose project");
        
        expanderComposite = formToolkit.createComposite(expander);
        expander.setClient(expanderComposite);
        
        updateProjectChooser();
        
        expanderComposite.setLayout(FormLayoutFactory.createSectionClientGridLayout(false, 1));
        
        //        projectChooser = new Combo(composite, SWT.NONE);
        
        Tree tree = formToolkit.createTree(formBody, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        viewer = new TreeViewer(tree);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        activeProjectChanged();
        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
        windowModelListener.install();
        elementChangedListener.install();
        
        Font font = viewer.getTree().getFont();
        FontData[] fontData = font.getFontData();
        fontData[0].setStyle(SWT.BOLD);
        boldFont = new Font(null, fontData[0]);
    }
    
    private TableWrapLayout createTableLayout() {
        TableWrapLayout layout = new TableWrapLayout();
        layout.verticalSpacing = 0;
        layout.topMargin = 10;
        layout.bottomMargin = 10;
        layout.leftMargin = 10;
        layout.rightMargin = 10;
        return layout;
    }
    
    private TableWrapData createLayoutData() {
        return new TableWrapData(TableWrapData.FILL_GRAB);
    }
    
    private TableWrapData createLayoutData2() {
        TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB);
        return data;
    }
    
    private void updateProjectChooser() {
        chooserProjects = new ArrayList<IRailsProject>();
        chooserProjects.addAll(RailsCore.instance().getProjectsCollection().getRailsProjects());
        Collections.sort(chooserProjects, new Comparator<IRailsProject>() {
            
            public int compare(IRailsProject o1, IRailsProject o2) {
                String n1 = o1.getProject().getName();
                String n2 = o2.getProject().getName();
                return n1.compareTo(n2);
            }
            
        });
        
        IRailsProject activeProject = RailsWindowModel.instance().getProject(getSite().getWorkbenchWindow());
        
        Control[] children = expanderComposite.getChildren();
        for (Control control : children) {
            control.dispose();
        }
        
        for (final IRailsProject railsProject : chooserProjects) {
            Control hyperlink;
            String name = railsProject.getProject().getName();
            if (railsProject.equals(activeProject)) {
                hyperlink = formToolkit.createLabel(expanderComposite, name);
            } else {
                Hyperlink hyperlink2 = formToolkit.createHyperlink(expanderComposite, name, SWT.NONE);
                hyperlink = hyperlink2;
                hyperlink2.addHyperlinkListener(new HyperlinkAdapter() {
                    
                    @Override
                    public void linkActivated(HyperlinkEvent e) {
                        RailsWindowModel.instance().setProject(getSite().getWorkbenchWindow(), railsProject);
                        expander.setExpanded(false);
                    }
                    
                });
            }
            hyperlink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        }
        
        Hyperlink newProjectLink = formToolkit.createHyperlink(expanderComposite,
                "» Create a brand-new Rails application", SWT.NONE);
        newProjectLink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        Hyperlink importProjectLink = formToolkit.createHyperlink(expanderComposite,
                "» Import an existing Rails application", SWT.NONE);
        importProjectLink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        Hyperlink sampleProjectLink = formToolkit.createHyperlink(expanderComposite,
                "» Import a sample Rails application", SWT.NONE);
        sampleProjectLink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        FormText newWindowLabel = formToolkit.createFormText(expanderComposite, true);
        newWindowLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        newWindowLabel.setText("<form><p><b>Tip:</b> To work on several projects at once, "
                + "<a href=\"new_win\">open a new window</a>.</p></form>", true, false);
        newWindowLabel.addHyperlinkListener(new HyperlinkAdapter() {
            
            @Override
            public void linkActivated(HyperlinkEvent e) {
                try {
                    PlatformUI.getWorkbench().openWorkbenchWindow(null);
                } catch (WorkbenchException ex) {
                    Activator.log(ex);
                }
            }
            
        });
        
        //        text.setLayoutData(createLayoutDate());
        
        //        form.layout();
        
        //        projectChooser.setItems(items.toArray(new String[items.size()]));
        //        projectChooser.setVisibleItemCount(items.size());
    }
    
    @Override
    public void dispose() {
        super.dispose();
        windowModelListener.uninstall();
        elementChangedListener.uninstall();
        boldFont.dispose();
    }
    
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                RailsView.this.fillContextMenu(manager);
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
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(action1);
        manager.add(new Separator());
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
                ISelection selection = viewer.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                IFile fileToOpen = null;
                if (obj instanceof IRailsController) {
                    IRailsController railsController = (IRailsController) obj;
                    fileToOpen = railsController.getFile();
                } else if (obj instanceof IRailsBaseView) {
                    IRailsBaseView railsBaseView = (IRailsBaseView) obj;
                    fileToOpen = railsBaseView.getCorrespondingFile();
                }
                if (fileToOpen != null) {
                    try {
                        IDE.openEditor(getSite().getPage(), fileToOpen);
                    } catch (PartInitException e) {
                        Activator.log(e);
                    }
                }
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