package com.yoursway.ide.ui.railsview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.part.ViewPart;

import com.yoursway.ide.ui.Activator;
import com.yoursway.ide.ui.advisor.FormLayoutFactory;
import com.yoursway.rails.model.IRailsChangeListener;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.RailsCore;
import com.yoursway.rails.model.deltas.RailsChangeEvent;
import com.yoursway.rails.models.launch.ILaunchingModelListener;
import com.yoursway.rails.models.launch.IProjectLaunching;
import com.yoursway.rails.models.launch.RailsServersModel;
import com.yoursway.rails.models.launch.IProjectLaunching.PortNumberNotAvailable;
import com.yoursway.rails.windowmodel.RailsWindow;
import com.yoursway.rails.windowmodel.RailsWindowModel;
import com.yoursway.rails.windowmodel.RailsWindowModelListenerAdapter;
import com.yoursway.rails.windowmodel.RailsWindowModelProjectChange;

public class RailsView extends ViewPart implements IRailsProjectTreeOwner {
    
    public static final String ID = "com.yoursway.ide.ui.RailsView";
    
    private Action action1;
    
    static final Object[] NO_CHILDREN = new Object[0];
    
    class WindowModelListener extends RailsWindowModelListenerAdapter {
        
        public void install() {
            RailsWindowModel.instance().addListener(this);
        }
        
        public void uninstall() {
            RailsWindowModel.instance().removeListener(this);
        }
        
        @Override
        public void activeProjectChanged(RailsWindowModelProjectChange event) {
            if (event.getWindow() == getSite().getWorkbenchWindow())
                handleActiveProjectChanged();
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
                    handleElementChangedEvent(event);
                }
            });
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
    private List<IRailsProject> chooserProjects;
    
    private ExpandableComposite expander;
    
    private FormToolkit formToolkit;
    
    private Form form;
    
    private Composite expanderComposite;
    
    private RailsProjectTree projectTree;
    
    private Composite bottomComposite;
    
    private final ServerModelListener serverModelListener = new ServerModelListener();
    
    class NameSorter extends ViewerSorter {
    }
    
    public RailsView() {
    }
    
    public void handleActiveProjectChanged() {
        IRailsProject project = RailsWindowModel.instance().getWindow(getSite().getWorkbenchWindow())
                .getRailsProject();
        projectTree.setVisibleProject(project);
        updateProjectChooser();
        updateBottom();
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
    
    void handleElementChangedEvent(RailsChangeEvent event) {
        projectTree.refresh();
        updateProjectChooser();
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
        expander = formToolkit.createSection(formBody, ExpandableComposite.TWISTIE
                | ExpandableComposite.CLIENT_INDENT);
        expander.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        expanderComposite = formToolkit.createComposite(expander);
        expander.setClient(expanderComposite);
        
        updateProjectChooser();
        
        expanderComposite.setLayout(FormLayoutFactory.createSectionClientGridLayout(false, 1));
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
        
        IRailsProject activeProject = RailsWindowModel.instance().getWindow(getSite().getWorkbenchWindow())
                .getRailsProject();
        
        disposeChildren(expanderComposite);
        
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
                        RailsWindowModel.instance().getWindow(getSite().getWorkbenchWindow())
                                .setRailsProject(railsProject);
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
//        ((GridData) newWindowLabel.getLayoutData()).heightHint = 3 * newWindowLabel.computeSize(SWT.DEFAULT,
//                SWT.DEFAULT).y;
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
        
        expanderComposite.layout();
    }
    
    private void disposeChildren(final Composite parent) {
        Control[] children = parent.getChildren();
        for (Control control : children) {
            control.dispose();
        }
    }
    
    private void updateBottom() {
        disposeChildren(bottomComposite);
        
        RailsWindow w = RailsWindowModel.instance().getWindow(getSite().getWorkbenchWindow());
        IRailsProject railsProject = w.getRailsProject();
        
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
    
    private void createServerControls(IRailsProject railsProject, String markup) {
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
        IRailsProject railsProject = w.getRailsProject();
        IProjectLaunching projectLaunching = RailsServersModel.instance().get(railsProject);
        projectLaunching.startDefaultServer();
    }
    
    private void doStopServer() {
        RailsWindow w = RailsWindowModel.instance().getWindow(getSite().getWorkbenchWindow());
        IRailsProject railsProject = w.getRailsProject();
        IProjectLaunching projectLaunching = RailsServersModel.instance().get(railsProject);
        projectLaunching.stopServer();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        windowModelListener.uninstall();
        elementChangedListener.uninstall();
        serverModelListener.uninstall();
        boldFont.dispose();
    }
    
    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }
    
    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(action1);
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
                projectTree.refresh();
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
