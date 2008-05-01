package com.yoursway.ide.rcp.internal;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.dialogs.StartupPreferencePage;

/**
 * Extends the Startup and Shutdown preference page with IDE-specific settings.
 * 
 * Note: want IDE settings to appear in main Workbench preference page (via subclassing),
 *   however the superclass, StartupPreferencePage, is internal
 * @since 3.0
 */
public class IDEStartupPreferencePage extends StartupPreferencePage implements
        IWorkbenchPreferencePage {

    private Button refreshButton;

    private Button launchPromptButton;

    private Button exitPromptButton;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage
     */
    protected Control createContents(Composite parent) {

    	PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				IWorkbenchHelpContextIds.STARTUP_PREFERENCE_PAGE);

        Composite composite = createComposite(parent);

        createLaunchPromptPref(composite);
        createRefreshWorkspaceOnStartupPref(composite);
        createExitPromptPref(composite);

        Label space = new Label(composite,SWT.NONE);
		space.setLayoutData(new GridData());
        
        createEarlyStartupSelection(composite);

        return composite;
    }

    /**
     * The default button has been pressed.
     */
    protected void performDefaults() {
        IPreferenceStore store = getIDEPreferenceStore();

        launchPromptButton.setSelection(true);

        refreshButton
                .setSelection(store
                        .getDefaultBoolean(IDEInternalPreferences.REFRESH_WORKSPACE_ON_STARTUP));
        exitPromptButton
                .setSelection(store
                        .getDefaultBoolean(IDEInternalPreferences.EXIT_PROMPT_ON_CLOSE_LAST_WINDOW));

        super.performDefaults();
    }

    /**
     * The user has pressed Ok. Store/apply this page's values appropriately.
     */
    public boolean performOk() {
        IPreferenceStore store = getIDEPreferenceStore();

        // store the refresh workspace on startup setting
        store.setValue(IDEInternalPreferences.REFRESH_WORKSPACE_ON_STARTUP,
                refreshButton.getSelection());

        // TODO: This should get the value from the configuration preference
        //       area, but dj said we shouldn't use it yet; some final details are
        //       being worked out. Hopefully it will be available soon, at which time
        //       the entire recentWorkspaces.xml file can be removed. But until then,
        //       this preference reads/writes the file each time.
        ChooseWorkspaceData.setShowDialogValue(launchPromptButton
                .getSelection());

        // store the exit prompt on last window close setting
        store.setValue(IDEInternalPreferences.EXIT_PROMPT_ON_CLOSE_LAST_WINDOW,
                exitPromptButton.getSelection());

        Activator.getDefault().savePluginPreferences();

        return super.performOk();
    }

    protected void createRefreshWorkspaceOnStartupPref(Composite composite) {
        refreshButton = new Button(composite, SWT.CHECK);
        refreshButton.setText("&Refresh workspace on startup");
        refreshButton.setFont(composite.getFont());
        refreshButton.setSelection(getIDEPreferenceStore().getBoolean(
                IDEInternalPreferences.REFRESH_WORKSPACE_ON_STARTUP));
    }

    protected void createLaunchPromptPref(Composite composite) {
        launchPromptButton = new Button(composite, SWT.CHECK);
        launchPromptButton.setText("Prompt for &workspace on startup");
        launchPromptButton.setFont(composite.getFont());

        // TODO: This should get the value from the configuration preference
        //       area, but dj said we shouldn't use it yet; some final details are
        //       being worked out. Hopefully it will be available soon, at which time
        //       the entire recentWorkspaces.xml file can be removed. But until then,
        //       this preference reads/writes the file each time.
        launchPromptButton.setSelection(ChooseWorkspaceData
                .getShowDialogValue());
    }

    protected void createExitPromptPref(Composite composite) {
        exitPromptButton = new Button(composite, SWT.CHECK);
        exitPromptButton.setText("&Confirm exit when closing last window");
        exitPromptButton.setFont(composite.getFont());
        exitPromptButton.setSelection(getIDEPreferenceStore().getBoolean(
                IDEInternalPreferences.EXIT_PROMPT_ON_CLOSE_LAST_WINDOW));
    }

    /**
     * Returns the IDE preference store.
     */
    protected IPreferenceStore getIDEPreferenceStore() {
        return Activator.getDefault().getPreferenceStore();
    }
}
