package com.yoursway.ide.ui.railsview;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import com.yoursway.ide.ui.Activator;
import com.yoursway.ide.ui.advisor.FormLayoutFactory;
import com.yoursway.ide.windowing.RailsWindow;
import com.yoursway.ide.windowing.RailsWindowModel;
import com.yoursway.ide.windowing.RailsWindowModelListenerAdapter;
import com.yoursway.ide.windowing.RailsWindowModelProjectChange;
import com.yoursway.rails.core.controllers.IRailsControllersListener;
import com.yoursway.rails.core.controllers.RailsController;
import com.yoursway.rails.core.controllers.RailsControllersCollection;
import com.yoursway.rails.core.dbschema.DbTable;
import com.yoursway.rails.core.dbschema.DbTablesCollection;
import com.yoursway.rails.core.dbschema.IDbSchemaListener;
import com.yoursway.rails.core.models.IModelsListener;
import com.yoursway.rails.core.models.RailsModel;
import com.yoursway.rails.core.models.RailsModelsCollection;
import com.yoursway.rails.core.projects.IProjectsListener;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.rails.core.projects.RailsProjectsCollection;
import com.yoursway.rails.launching.ILaunchingModelListener;
import com.yoursway.rails.launching.IProjectLaunching;
import com.yoursway.rails.launching.RailsServersModel;
import com.yoursway.rails.launching.IProjectLaunching.PortNumberNotAvailable;

public class RailsView extends ViewPart implements IRailsProjectTreeOwner {
    
    public static final String ID = "com.yoursway.ide.ui.RailsView";
    
    private Action action1;
    
    static final Object[] NO_CHILDREN = new Object[0];
    
    private final static class DummyMenuCreator implements IMenuCreator {
        private Menu menu;
        private final String[] actions;
        
        public DummyMenuCreator(String[] actions) {
            this.actions = actions;
        }
        
        public void dispose() {
            if (menu != null)
                menu.dispose();
        }
        
        public Menu getMenu(Control parent) {
            if (menu != null)
                menu.dispose();
            menu = new Menu(parent);
            fillMenu();
            return menu;
        }
        
        public Menu getMenu(Menu parent) {
            if (menu != null)
                menu.dispose();
            menu = new Menu(parent);
            fillMenu();
            return menu;
        }
        
        private void fillMenu() {
            for (String actionName : actions) {
                if (actionName.equals("-"))
                    new Separator().fill(menu, -1);
                else
                    new ActionContributionItem(new DummyAction(actionName)).fill(menu, -1);
            }
        }
    }
    
    private final class XMouseListener implements MouseListener {
        private final FormText text;
        private final IMenuCreator menuCreator;
        
        private XMouseListener(FormText text, IMenuCreator menuCreator) {
            this.text = text;
            this.menuCreator = menuCreator;
        }
        
        public void mouseDoubleClick(MouseEvent e) {
        }
        
        public void mouseDown(MouseEvent e) {
            FormText ti = text;
            Menu m = menuCreator.getMenu(ti);
            if (m != null) {
                // position the menu below the drop down item
                Rectangle b = ti.getBounds();
                Point p = ti.getParent().toDisplay(new Point(b.x, b.y + b.height));
                m.setLocation(p.x, p.y); // waiting for SWT
                // 0.42
                m.setVisible(true);
                
                // necessary to display the menu immediately under OS X
                Display.getDefault().readAndDispatch();
            }
        }
        
        public void mouseUp(MouseEvent e) {
        }
    }
    
    class WindowModelListener extends RailsWindowModelListenerAdapter {
        
        public void install() {
            RailsWindowModel.instance().addListener(this);
        }
        
        public void uninstall() {
            RailsWindowModel.instance().removeListener(this);
        }
        
        @Override
        public void activeProjectChanged(RailsWindowModelProjectChange event) {
            if (event.getWindow() == getSite().getWorkbenchWindow()) {
                Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                        handleActiveProjectChanged();
                    }
                });
            }
        }
        
    }
    
    class ElementChangedListener implements IProjectsListener, IRailsControllersListener, IModelsListener,
            IDbSchemaListener {
        
        public void install() {
            RailsProjectsCollection.getInstance().addListener(this);
            RailsControllersCollection.getInstance().addListener(this);
            RailsModelsCollection.getInstance().addListener(this);
            DbTablesCollection.getInstance().addListener(this);
        }
        
        public void uninstall() {
            RailsProjectsCollection.getInstance().removeListener(this);
            RailsControllersCollection.getInstance().removeListener(this);
            RailsModelsCollection.getInstance().removeListener(this);
            DbTablesCollection.getInstance().removeListener(this);
        }
        
        public void projectAdded(RailsProject railsProject) {
            refreshEverything();
        }
        
        public void projectRemoved(RailsProject railsProject) {
            refreshEverything();
        }
        
        public void reconcile(RailsProject railsProject, IResourceDelta resourceDelta) {
        }
        
        public void controllerAdded(RailsController railsController) {
            refreshEverything();
        }
        
        public void controllerRemoved(RailsController railsController) {
            refreshEverything();
        }
        
        public void reconcile(RailsController railsController) {
        }
        
        public void modelAdded(RailsModel railsModel) {
            refreshEverything();
        }
        
        public void modelContentChanged(RailsModel railsModel) {
        }
        
        public void modelRemoved(RailsModel railsModel) {
            refreshEverything();
        }
        
        public void tableAdded(DbTable dbTable) {
            refreshEverything();
        }
        
        public void tableChanged(DbTable dbTable) {
            refreshEverything();
        }
        
        public void tableRemoved(DbTable dbTable) {
            refreshEverything();
        }
        
    }
    
    class ServerModelListener implements ILaunchingModelListener {
        
        public void install() {
            RailsServersModel.instance().addListener(this);
        }
        
        public void uninstall() {
            RailsServersModel.instance().removeListener(this);
        }
        
        public void projectStateChanged(IProjectLaunching launching) {
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    updateBottom();
                }
            });
        }
        
    }
    
    private final ElementChangedListener elementChangedListener = new ElementChangedListener();
    private final WindowModelListener windowModelListener = new WindowModelListener();
    private Font boldFont;
    //    private List<IRailsProject> chooserProjects;
    
    //    private ExpandableComposite expander;
    
    private FormToolkit formToolkit;
    
    private Form form;
    
    //    private Composite expanderComposite;
    
    private RailsProjectTree projectTree;
    
    private Composite bottomComposite;
    
    private final ServerModelListener serverModelListener = new ServerModelListener();
    
    class NameSorter extends ViewerSorter {
    }
    
    public RailsView() {
    }
    
    public void handleActiveProjectChanged() {
        RailsProject project = RailsWindowModel.instance().getWindow(getSite().getWorkbenchWindow())
                .getRailsProject();
        projectTree.setVisibleProject(project);
        //        updateProjectChooser();
        updateBottom();
        //        if (project == null) {
        //            if (chooserProjects.isEmpty())
        //                expander.setText("Create or import a project");
        //            else
        //                expander.setText("Choose a project");
        //            expander.setExpanded(true);
        //        } else {
        //            expander.setText(project.getProject().getName());
        //        }
    }
    
    private void refreshEverything() {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                projectTree.refresh();
            }
        });
    }
    
    @Override
    public void createPartControl(Composite parent) {
        formToolkit = new FormToolkit(getSite().getShell().getDisplay());
        form = formToolkit.createForm(parent);
        //        form.setLayoutData(treeData);
        Composite formBody = form.getBody();
        final GridLayout layout = FormLayoutFactory.createFormGridLayout(false, 1);
        layout.verticalSpacing = 5;
        formBody.setLayout(layout);
        
        createTopControls(formBody);
        
        projectTree = new RailsProjectTree(formBody, formToolkit, this);
        getSite()
                .registerContextMenu(projectTree.getContextMenuManager(), projectTree.getSelectionProvider());
        createBottomControls(formBody);
        
        handleActiveProjectChanged();
        makeActions();
        
        contributeToActionBars();
        windowModelListener.install();
        elementChangedListener.install();
        serverModelListener.install();
        
        //        Font font = viewer.getTree().getFont();
        //        FontData[] fontData = font.getFontData();
        //        fontData[0].setStyle(SWT.BOLD);
        //        boldFont = new Font(null, fontData[0]);
    }
    
    private void createTopControls(Composite formBody) {
        //        expander = formToolkit.createSection(formBody, ExpandableComposite.TWISTIE
        //                | ExpandableComposite.CLIENT_INDENT);
        //        expander.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        //        
        //        expanderComposite = formToolkit.createComposite(expander);
        //        expander.setClient(expanderComposite);
        //        
        //        updateProjectChooser();
        
        //        expanderComposite.setLayout(FormLayoutFactory.createSectionClientGridLayout(false, 1));
    }
    
    private void createBottomControls(Composite formBody) {
        //        expander = formToolkit.createSection(formBody, ExpandableComposite.TWISTIE
        //                | ExpandableComposite.CLIENT_INDENT);
        //        expander.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        //        
        bottomComposite = formToolkit.createComposite(formBody);
        //        expander.setClient(expanderComposite);
        final GridLayout layout = FormLayoutFactory.createSectionClientGridLayout(false, 1);
        layout.marginHeight = 0;
        layout.marginTop = 0;
        layout.verticalSpacing = 0;
        bottomComposite.setLayout(layout);
        
        updateBottom();
    }
    
    //    private void updateProjectChooser() {
    //        chooserProjects = new ArrayList<IRailsProject>();
    //        chooserProjects.addAll(RailsCore.instance().getProjectsCollection().getRailsProjects());
    //        Collections.sort(chooserProjects, new Comparator<IRailsProject>() {
    //            
    //            public int compare(IRailsProject o1, IRailsProject o2) {
    //                String n1 = o1.getProject().getName();
    //                String n2 = o2.getProject().getName();
    //                return n1.compareTo(n2);
    //            }
    //            
    //        });
    //        
    //        IRailsProject activeProject = RailsWindowModel.instance().getWindow(getSite().getWorkbenchWindow())
    //                .getRailsProject();
    //        
    //        disposeChildren(expanderComposite);
    //        
    //        for (final IRailsProject railsProject : chooserProjects) {
    //            Control hyperlink;
    //            String name = railsProject.getProject().getName();
    //            if (railsProject.equals(activeProject)) {
    //                hyperlink = formToolkit.createLabel(expanderComposite, name);
    //            } else {
    //                Hyperlink hyperlink2 = formToolkit.createHyperlink(expanderComposite, name, SWT.NONE);
    //                hyperlink = hyperlink2;
    //                hyperlink2.addHyperlinkListener(new HyperlinkAdapter() {
    //                    
    //                    @Override
    //                    public void linkActivated(HyperlinkEvent e) {
    //                        RailsWindowModel.instance().getWindow(getSite().getWorkbenchWindow())
    //                                .setRailsProject(railsProject);
    //                        expander.setExpanded(false);
    //                    }
    //                    
    //                });
    //            }
    //            hyperlink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    //        }
    //        
    //        Hyperlink newProjectLink = formToolkit.createHyperlink(expanderComposite,
    //                "» Create a brand-new Rails application", SWT.NONE);
    //        newProjectLink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    //        
    //        Hyperlink importProjectLink = formToolkit.createHyperlink(expanderComposite,
    //                "» Import an existing Rails application", SWT.NONE);
    //        importProjectLink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    //        
    //        Hyperlink sampleProjectLink = formToolkit.createHyperlink(expanderComposite,
    //                "» Import a sample Rails application", SWT.NONE);
    //        sampleProjectLink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    //        
    //        FormText newWindowLabel = formToolkit.createFormText(expanderComposite, true);
    //        newWindowLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    //        newWindowLabel.setText("<form><p><b>Tip:</b> To work on several projects at once, "
    //                + "<a href=\"new_win\">open a new window</a>.</p></form>", true, false);
    //        //        ((GridData) newWindowLabel.getLayoutData()).heightHint = 3 * newWindowLabel.computeSize(SWT.DEFAULT,
    //        //                SWT.DEFAULT).y;
    //        newWindowLabel.addHyperlinkListener(new HyperlinkAdapter() {
    //            
    //            @Override
    //            public void linkActivated(HyperlinkEvent e) {
    //                try {
    //                    PlatformUI.getWorkbench().openWorkbenchWindow(null);
    //                } catch (WorkbenchException ex) {
    //                    Activator.unexpectedError(ex);
    //                }
    //            }
    //            
    //        });
    //        
    //        expanderComposite.layout();
    //    }
    
    private void disposeChildren(final Composite parent) {
        Control[] children = parent.getChildren();
        for (Control control : children) {
            control.dispose();
        }
    }
    
    private void updateBottom() {
        disposeChildren(bottomComposite);
        
        RailsWindow w = RailsWindowModel.instance().getWindow(getSite().getWorkbenchWindow());
        RailsProject railsProject = w.getRailsProject();
        
        if (railsProject != null) {
            IProjectLaunching launching = RailsServersModel.instance().get(railsProject);
            int port;
            try {
                port = launching.getPortNumber();
            } catch (PortNumberNotAvailable e) {
                port = 0;
            }
            switch (launching.getState()) {
            case NOT_RUNNING:
                createServerControls(railsProject, "<form><p><a href=\"start\">Start "
                        + railsProject.getProject().getName() + "</a></p></form>");
                break;
            case RUNNING:
                if (port == 0)
                    createServerControls(railsProject, "<form><p>Server is running. "
                            + "<a href=\"stop\">Stop</a></p></form>");
                else
                    createServerControls(railsProject, "<form><p>Server is running on port " + port + ". "
                            + "<a href=\"stop\">Stop</a></p></form>");
                break;
            case LAUNCHING:
                createServerControls(railsProject, "<form><p>Launching server... "
                        + "<a href=\"cancel_start\">Cancel</a></p></form>");
                break;
            case STOPPING:
                createServerControls(railsProject, "<form><p>Stopping server...</p></form>");
                break;
            case FAILED:
                createServerControls(railsProject, "<form><p>Server failed to start. "
                        + "<a href=\"start\">Retry</a>.</p></form>");
                break;
            default:
                assert false;
            }
        }
        
        bottomComposite.layout();
        form.getBody().layout();
    }
    
    private void createServerControls(RailsProject railsProject, String markup) {
        FormText newWindowLabel = formToolkit.createFormText(bottomComposite, true);
        newWindowLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        newWindowLabel.setText(markup, true, false);
        //        ((GridData) newWindowLabel.getLayoutData()).heightHint = 3 * newWindowLabel.computeSize(SWT.DEFAULT,
        //                SWT.DEFAULT).y;
        newWindowLabel.addHyperlinkListener(new HyperlinkAdapter() {
            
            @Override
            public void linkActivated(HyperlinkEvent e) {
                String href = (String) e.getHref();
                if (href.equals("start"))
                    doStartServer();
                else if (href.equals("stop"))
                    doStopServer();
                else if (href.equals("cancel_start"))
                    doStopServer();
            }
            
        });
    }
    
    private void doStartServer() {
        RailsWindow w = RailsWindowModel.instance().getWindow(getSite().getWorkbenchWindow());
        RailsProject railsProject = w.getRailsProject();
        IProjectLaunching projectLaunching = RailsServersModel.instance().get(railsProject);
        projectLaunching.startDefaultServer();
    }
    
    private void doStopServer() {
        RailsWindow w = RailsWindowModel.instance().getWindow(getSite().getWorkbenchWindow());
        RailsProject railsProject = w.getRailsProject();
        IProjectLaunching projectLaunching = RailsServersModel.instance().get(railsProject);
        projectLaunching.stopServer();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        windowModelListener.uninstall();
        elementChangedListener.uninstall();
        serverModelListener.uninstall();
    }
    
    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }
    
    private void fillLocalPullDown(IMenuManager manager) {
        //        manager.add(action1);
    }
    
    private void fillLocalToolBar(IToolBarManager manager) {
        //        manager.add(action1);
        //        manager.add(new Separator());
        final Action action = new Action("New (" + KeyStroke.getInstance(SWT.COMMAND, SWT.F2).format() + ")",
                IAction.AS_DROP_DOWN_MENU) {
            
            @Override
            public void runWithEvent(Event event) {
                ToolItem ti = (ToolItem) event.widget;
                IMenuCreator mc = getMenuCreator();
                Menu m = mc.getMenu(ti.getParent());
                if (m != null) {
                    // position the menu below the drop down item
                    Rectangle b = ti.getBounds();
                    Point p = ti.getParent().toDisplay(new Point(b.x, b.y + b.height));
                    m.setLocation(p.x, p.y); // waiting for SWT
                    // 0.42
                    m.setVisible(true);
                }
            }
            
        };
        //        manager.add(action);
        action.setMenuCreator(new DummyMenuCreator(new String[] { "Check Out" }));
        ControlContribution controlContribution = new ControlContribution("foo") {
            
            @Override
            protected Control createControl(Composite parent) {
                Composite controls = new Composite(parent, SWT.NONE);
                
                final GridLayout gridLayout = new GridLayout(3, false);
                gridLayout.marginTop = 0;
                gridLayout.marginHeight = 0;
                gridLayout.horizontalSpacing = 8;
                controls.setLayout(gridLayout);
                
                addSubmenu(controls, "New", KeyStroke.getInstance(SWT.COMMAND, 'N').format(), new String[] {
                        "New Project", "Import Existing Project", "-", "New Controller" });
                
                addSubmenu(controls, "CVS", KeyStroke.getInstance(SWT.CTRL, 'C').format(), new String[] {
                        "Check Out Project From CVS", "-", "Update Project", "Update selected_controller.rb",
                        "-", "Commit All Changes", "Commit Changes in selected_controller.rb", "-",
                        "More CVS Options >" });
                
                Text search = new Text(controls, SWT.SEARCH);
                search.setText("Live search...");
                
                return controls;
            }
            
            private void addSubmenu(Composite parent, final String label, final String shortcut,
                    final String[] submenu) {
                FormText text = new FormText(parent, SWT.NONE);
                text.setText("<form><p><b>" + label + "</b> (" + shortcut + ") ▾</p></form>", true, false);
                text.addMouseListener(new XMouseListener(text, new DummyMenuCreator(submenu)));
            }
            
        };
        manager.add(controlContribution);
    }
    
    private static class DummyAction extends Action {
        
        public DummyAction(String text) {
            super(text);
        }
        
    }
    
    private void makeActions() {
        action1 = new Action() {
            @Override
            public void run() {
                refreshEverything();
            }
        };
        action1.setText("Refresh");
        action1.setToolTipText("Rebuild the entire Rails model");
        action1.setImageDescriptor(Activator.getImageDescriptor("icons/rails_model_view.gif"));
        
    }
    
    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        projectTree.setFocus();
    }
    
    public IWorkbenchPage getWorkbenchPage() {
        return getSite().getPage();
    }
    
}
