//package com.yoursway.ide.rcp.internal;
//
//import org.eclipse.jface.dialogs.IDialogSettings;
//import org.eclipse.swt.widgets.Shell;
//
//import com.yoursway.ide.rcp.launcher.internal.ChooseWorkspaceData;
//import com.yoursway.ide.rcp.launcher.internal.ChooseWorkspaceDialog;
//
//public class ChooseWorkspaceDialogEx extends ChooseWorkspaceDialog {
//    
//    private static final String DIALOG_SETTINGS_SECTION = "ChooseWorkspaceDialogSettings"; //$NON-NLS-1$
//
//    public ChooseWorkspaceDialogEx(Shell parentShell, ChooseWorkspaceData launchData,
//            boolean suppressAskAgain, boolean centerOnMonitor) {
//        super(parentShell, launchData, suppressAskAgain, centerOnMonitor);
//    }
//    
//    protected IDialogSettings getDialogBoundsSettings() {
//        // If we were explicitly instructed to center on the monitor, then
//        // do not provide any settings for retrieving a different location or, worse,
//        // saving the centered location.
//        if (centerOnMonitor) {
//            return null;
//        }
//        
//        IDialogSettings settings = Activator.getDefault().getDialogSettings();
//        IDialogSettings section = settings.getSection(DIALOG_SETTINGS_SECTION);
//        if (section == null) {
//            section = settings.addNewSection(DIALOG_SETTINGS_SECTION);
//        } 
//        return section;
//    }
//
//}
