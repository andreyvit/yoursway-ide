package com.yoursway.ide.projects.editor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.yoursway.ide.ui.Activator;
import com.yoursway.ide.windowing.RailsWindowModel;
import com.yoursway.rails.core.projects.RailsProject;

public class ShowProjectEditorHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent arg0) throws ExecutionException {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        RailsProject project = RailsWindowModel.instance().getWindow(window).getRailsProject();
        if (project == null)
            return null;
        try {
            window.getActivePage().openEditor(new ProjectEditorInput(project), ProjectEditor.ID);
        } catch (PartInitException e) {
            Activator.reportException(e, "Cannot open Rails Project Editor");
        }
        return null;
    }
    
}
