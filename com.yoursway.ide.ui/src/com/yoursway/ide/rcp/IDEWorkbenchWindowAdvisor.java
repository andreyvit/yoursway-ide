package com.yoursway.ide.rcp;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.ide.EditorAreaDropAdapter;
import org.eclipse.ui.internal.ide.IDEInternalPreferences;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.part.EditorInputTransfer;
import org.eclipse.ui.part.MarkerTransfer;
import org.eclipse.ui.part.ResourceTransfer;

import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.windowmodel.IRailsWindowModelListener;
import com.yoursway.rails.windowmodel.RailsWindowModel;
import com.yoursway.rails.windowmodel.RailsWindowModelChange;

/**
 * Window-level advisor for the IDE.
 */
public class IDEWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
    
    private static final String WELCOME_EDITOR_ID = "org.eclipse.ui.internal.ide.dialogs.WelcomeEditor"; //$NON-NLS-1$
    
    private final IDEWorkbenchAdvisor wbAdvisor;
    private boolean editorsAndIntrosOpened = false;
    private IEditorPart lastActiveEditor = null;
    private IPerspectiveDescriptor lastPerspective = null;
    
    private IWorkbenchPage lastActivePage;
    private final String lastEditorTitle = ""; //$NON-NLS-1$
    
    private final IPropertyListener editorPropertyListener = new IPropertyListener() {
        public void propertyChanged(Object source, int propId) {
            if (propId == IWorkbenchPartConstants.PROP_TITLE) {
                if (lastActiveEditor != null) {
                    String newTitle = lastActiveEditor.getTitle();
                    if (!lastEditorTitle.equals(newTitle)) {
                        recomputeTitle();
                    }
                }
            }
        }
    };
    
    private IAdaptable lastInput;
    
    /**
     * Crates a new IDE workbench window advisor.
     * 
     * @param wbAdvisor
     *            the workbench advisor
     * @param configurer
     *            the window configurer
     */
    public IDEWorkbenchWindowAdvisor(IDEWorkbenchAdvisor wbAdvisor, IWorkbenchWindowConfigurer configurer) {
        super(configurer);
        this.wbAdvisor = wbAdvisor;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(org.eclipse.ui.application.IActionBarConfigurer)
     */
    @Override
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new WorkbenchActionBuilder(configurer);
    }
    
    /**
     * Returns the workbench.
     * 
     * @return the workbench
     */
    private IWorkbench getWorkbench() {
        return getWindowConfigurer().getWorkbenchConfigurer().getWorkbench();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.WorkbenchAdvisor#preWindowShellClose
     */
    @Override
    public boolean preWindowShellClose() {
        if (getWorkbench().getWorkbenchWindowCount() > 1) {
            return true;
        }
        // the user has asked to close the last window, while will cause the
        // workbench to close in due course - prompt the user for confirmation
        IPreferenceStore store = IDEWorkbenchPlugin.getDefault().getPreferenceStore();
        boolean promptOnExit = store.getBoolean(IDEInternalPreferences.EXIT_PROMPT_ON_CLOSE_LAST_WINDOW);
        
        if (promptOnExit) {
            String message;
            
            String productName = null;
            IProduct product = Platform.getProduct();
            if (product != null) {
                productName = product.getName();
            }
            if (productName == null) {
                message = IDEWorkbenchMessages.PromptOnExitDialog_message0;
            } else {
                message = NLS.bind(IDEWorkbenchMessages.PromptOnExitDialog_message1, productName);
            }
            
            MessageDialogWithToggle dlg = MessageDialogWithToggle.openOkCancelConfirm(getWindowConfigurer()
                    .getWindow().getShell(), IDEWorkbenchMessages.PromptOnExitDialog_shellTitle, message,
                    IDEWorkbenchMessages.PromptOnExitDialog_choice, false, null, null);
            if (dlg.getReturnCode() != IDialogConstants.OK_ID) {
                return false;
            }
            if (dlg.getToggleState()) {
                store.setValue(IDEInternalPreferences.EXIT_PROMPT_ON_CLOSE_LAST_WINDOW, false);
                IDEWorkbenchPlugin.getDefault().savePluginPreferences();
            }
        }
        
        return true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.WorkbenchAdvisor#preWindowOpen
     */
    @Override
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        
        // show the shortcut bar and progress indicator, which are hidden by default
        configurer.setShowPerspectiveBar(true);
        configurer.setShowFastViewBars(true);
        configurer.setShowProgressIndicator(true);
        
        // add the drag and drop support for the editor area
        configurer.addEditorAreaTransfer(EditorInputTransfer.getInstance());
        configurer.addEditorAreaTransfer(ResourceTransfer.getInstance());
        configurer.addEditorAreaTransfer(MarkerTransfer.getInstance());
        configurer.configureEditorAreaDropListener(new EditorAreaDropAdapter(configurer.getWindow()));
        
        hookTitleUpdateListeners(configurer);
    }
    
    /**
     * Hooks the listeners needed on the window
     * 
     * @param configurer
     */
    private void hookTitleUpdateListeners(IWorkbenchWindowConfigurer configurer) {
        // hook up the listeners to update the window title
        final IWorkbenchWindow window = configurer.getWindow();
        window.addPageListener(new IPageListener() {
            public void pageActivated(IWorkbenchPage page) {
                updateTitle();
            }
            
            public void pageClosed(IWorkbenchPage page) {
                updateTitle();
            }
            
            public void pageOpened(IWorkbenchPage page) {
                // do nothing
            }
        });
        window.addPerspectiveListener(new PerspectiveAdapter() {
            @Override
            public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
                updateTitle();
            }
            
            @Override
            public void perspectiveSavedAs(IWorkbenchPage page, IPerspectiveDescriptor oldPerspective,
                    IPerspectiveDescriptor newPerspective) {
                updateTitle();
            }
            
            @Override
            public void perspectiveDeactivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
                updateTitle();
            }
        });
        window.getPartService().addPartListener(new IPartListener2() {
            public void partActivated(IWorkbenchPartReference ref) {
                if (ref instanceof IEditorReference) {
                    updateTitle();
                }
            }
            
            public void partBroughtToTop(IWorkbenchPartReference ref) {
                if (ref instanceof IEditorReference) {
                    updateTitle();
                }
            }
            
            public void partClosed(IWorkbenchPartReference ref) {
                updateTitle();
            }
            
            public void partDeactivated(IWorkbenchPartReference ref) {
                // do nothing
            }
            
            public void partOpened(IWorkbenchPartReference ref) {
                // do nothing
            }
            
            public void partHidden(IWorkbenchPartReference ref) {
                // do nothing
            }
            
            public void partVisible(IWorkbenchPartReference ref) {
                // do nothing
            }
            
            public void partInputChanged(IWorkbenchPartReference ref) {
                // do nothing
            }
        });
        RailsWindowModel.instance().addListener(new IRailsWindowModelListener() {
            
            public void mappingChanged(RailsWindowModelChange event) {
                if (event.getWindow() == window) {
                    updateTitle();
                }
            }
            
        });
    }
    
    private String computeTitle() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        IWorkbenchWindow currentWindow = configurer.getWindow();
        IWorkbenchPage currentPage = currentWindow.getActivePage();
        IEditorPart activeEditor = null;
        if (currentPage != null) {
            activeEditor = currentPage.getActiveEditor();
        }
        
        String title = null;
        IProduct product = Platform.getProduct();
        if (product != null) {
            title = product.getName();
        }
        if (title == null) {
            title = ""; //$NON-NLS-1$
        }
        
        //        if (currentPage != null) {
        //            if (activeEditor != null) {
        //                lastEditorTitle = activeEditor.getTitleToolTip();
        //                title = NLS.bind(IDEWorkbenchMessages.WorkbenchWindow_shellTitle, lastEditorTitle, title);
        //            }
        //            IPerspectiveDescriptor persp = currentPage.getPerspective();
        //            String label = ""; //$NON-NLS-1$
        //            if (persp != null) {
        //                label = persp.getLabel();
        //            }
        //            IAdaptable input = currentPage.getInput();
        //            if (input != null && !input.equals(wbAdvisor.getDefaultPageInput())) {
        //                label = currentPage.getLabel();
        //            }
        //            if (label != null && !label.equals("")) { //$NON-NLS-1$ 
        //                title = NLS.bind(IDEWorkbenchMessages.WorkbenchWindow_shellTitle, label, title);
        //            }
        //        }
        
        IRailsProject currentProject = RailsWindowModel.instance().getProject(currentWindow);
        if (currentProject != null) {
            title = NLS.bind("{0} - {1}", currentProject.getProject().getName(), title);
        }
        
        String workspaceLocation = wbAdvisor.getWorkspaceLocation();
        if (workspaceLocation != null) {
            title = NLS.bind(IDEWorkbenchMessages.WorkbenchWindow_shellTitle, title, workspaceLocation);
        }
        
        return title;
    }
    
    private void recomputeTitle() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        String oldTitle = configurer.getTitle();
        String newTitle = computeTitle();
        if (!newTitle.equals(oldTitle)) {
            configurer.setTitle(newTitle);
        }
    }
    
    /**
     * Updates the window title. Format will be: [pageInput -]
     * [currentPerspective -] [editorInput -] [workspaceLocation -] productName
     */
    private void updateTitle() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        IWorkbenchWindow window = configurer.getWindow();
        IEditorPart activeEditor = null;
        IWorkbenchPage currentPage = window.getActivePage();
        IPerspectiveDescriptor persp = null;
        IAdaptable input = null;
        
        if (currentPage != null) {
            activeEditor = currentPage.getActiveEditor();
            persp = currentPage.getPerspective();
            input = currentPage.getInput();
        }
        
        // Nothing to do if the editor hasn't changed
        //        if (activeEditor == lastActiveEditor && currentPage == lastActivePage && persp == lastPerspective
        //                && input == lastInput) {
        //            return;
        //        }
        
        if (lastActiveEditor != null) {
            lastActiveEditor.removePropertyListener(editorPropertyListener);
        }
        
        lastActiveEditor = activeEditor;
        lastActivePage = currentPage;
        lastPerspective = persp;
        lastInput = input;
        
        if (activeEditor != null) {
            activeEditor.addPropertyListener(editorPropertyListener);
        }
        
        recomputeTitle();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.WorkbenchAdvisor#postWindowRestore
     */
    @Override
    public void postWindowRestore() throws WorkbenchException {
    }
    
    @Override
    public void openIntro() {
        if (editorsAndIntrosOpened) {
            return;
        }
        
        editorsAndIntrosOpened = true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.WorkbenchAdvisor#createEmptyWindowContents(org.eclipse.ui.application.IWorkbenchWindowConfigurer,
     *      org.eclipse.swt.widgets.Composite)
     */
    @Override
    public Control createEmptyWindowContents(Composite parent) {
        final IWorkbenchWindow window = getWindowConfigurer().getWindow();
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        Display display = composite.getDisplay();
        Color bgCol = display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
        composite.setBackground(bgCol);
        Label label = new Label(composite, SWT.WRAP);
        label.setForeground(display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
        label.setBackground(bgCol);
        label.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));
        String msg = IDEWorkbenchMessages.IDEWorkbenchAdvisor_noPerspective;
        label.setText(msg);
        ToolBarManager toolBarManager = new ToolBarManager();
        // TODO: should obtain the open perspective action from ActionFactory
        IAction openPerspectiveAction = ActionFactory.OPEN_PERSPECTIVE_DIALOG.create(window);
        toolBarManager.add(openPerspectiveAction);
        ToolBar toolBar = toolBarManager.createControl(composite);
        toolBar.setBackground(bgCol);
        return composite;
    }
    
}
