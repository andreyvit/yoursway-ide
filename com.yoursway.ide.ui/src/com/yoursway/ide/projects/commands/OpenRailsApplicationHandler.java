package com.yoursway.ide.projects.commands;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.yoursway.ide.projects.YourSwayProjects;


public class OpenRailsApplicationHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        DirectoryDialog dialog = new DirectoryDialog(shell);
        dialog.setText("Open Rails Project");
        dialog.setMessage("Please choose any Rails application you wish to work on.");
        String reply = dialog.open();
        if (reply == null)
            return null;
        final File location = new File(reply);
        try {
            YourSwayProjects.openRailsApplication(location);
        } catch (NoRailsApplicationAtGivenLocation e) {
            // TODO: better user interface for this error
            MessageDialog.openError(shell, "Open Rails Project",
                    "The chosen directory does not contain a Rails application");
        }
        return null;
    }
}
