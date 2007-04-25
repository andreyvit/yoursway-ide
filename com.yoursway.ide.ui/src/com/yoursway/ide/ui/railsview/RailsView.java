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
    
    private final ElementChangedListener elementChangedListener = new ElementChangedListener();
    private final WindowModelListener windowModelListener = new WindowModelListener();
    private Font boldFont;
    private List<IRailsProject> chooserProjects;
    
    private ExpandableComposite expander;
    
    private FormToolkit formToolkit;
    
    private Form form;
    
    private Composite expanderComposite;
    
    private RailsProjectTree projectTree;
    
    class NameSorter extends ViewerSorter {
    }
    
    public RailsView() {
    }
    
    public void handleActiveProjectChanged() {
        IRailsProject project = RailsWindowModel.instance().getWindow(getSite().getWorkbenchWindow())
                .getRailsProject();
        projectTree.setVisibleProject(project);
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
        formBody.setLayout(FormLayoutFactory.createFormGridLayout(false, 1));
        
        expander = formToolkit.createSection(formBody, ExpandableComposite.TWISTIE
                | ExpandableComposite.CLIENT_INDENT);
        expander.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        expanderComposite = formToolkit.createComposite(expander);
        expander.setClient(expanderComposite);
        
        updateProjectChooser();
        
        expanderComposite.setLayout(FormLayoutFactory.createSectionClientGridLayout(false, 1));
        
        projectTree = new RailsProjectTree(formBody, formToolkit, this);
        getSite()
                .registerContextMenu(projectTree.getContextMenuManager(), projectTree.getSelectionProvider());
        
        handleActiveProjectChanged();
        makeActions();
        
        contributeToActionBars();
        windowModelListener.install();
        elementChangedListener.install();
        
        //        Font font = viewer.getTree().getFont();
        //        FontData[] fontData = font.getFontData();
        //        fontData[0].setStyle(SWT.BOLD);
        //        boldFont = new Font(null, fontData[0]);
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
        
    }
    
    @Override
    public void dispose() {
        super.dispose();
        windowModelListener.uninstall();
        elementChangedListener.uninstall();
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