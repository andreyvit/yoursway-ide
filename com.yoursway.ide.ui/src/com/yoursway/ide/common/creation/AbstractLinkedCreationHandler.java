package com.yoursway.ide.common.creation;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.yoursway.ide.ui.rubyeditor.HumaneRubyEditor;
import com.yoursway.rails.models.project.RailsProject;
import com.yoursway.rails.windowmodel.RailsWindowModel;
import com.yoursway.utils.EditorUtils;

public abstract class AbstractLinkedCreationHandler extends AbstractHandler {
    
    protected abstract LinkedCreationDescriptor createLinkedCreationDescriptor(RailsProject activeRailsProject)
            throws ExecutionException;
    
    @Override
    public Object execute(ExecutionEvent arg0) throws ExecutionException {
        final IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        RailsProject railsProject = RailsWindowModel.instance().getWindow(activeWindow).getRailsProject();
        
        final LinkedCreationDescriptor descriptor = createLinkedCreationDescriptor(railsProject);
        
        new Job(descriptor.getJobName()) {
            
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                final IFile file = EditorUtils.createFile(descriptor.getBody(), descriptor.getFolder(),
                        descriptor.getFileName(), descriptor.getFailureMessage());
                if (file != null) {
                    Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                            HumaneRubyEditor editor = (HumaneRubyEditor) EditorUtils.openEditor(file);
                            editor.enterLinkedMode(descriptor.createLinkedMode(editor));
                        }
                    });
                }
                return Status.OK_STATUS;
            }
            
        }.schedule();
        return null;
    }
}
