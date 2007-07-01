package com.yoursway.rails.controllers.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.ui.editor.EditorUtility;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.yoursway.ide.ui.Activator;
import com.yoursway.ide.ui.rubyeditor.HumaneRubyEditor;
import com.yoursway.rails.models.project.RailsProject;
import com.yoursway.rails.windowmodel.RailsWindowModel;
import com.yoursway.utils.RailsNamingConventions;

public class CreateControllerHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent arg0) throws ExecutionException {
        final IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        RailsProject railsProject = RailsWindowModel.instance().getWindow(activeWindow).getRailsProject();
        //        InputDialog dialog = new InputDialog(activeWindow.getShell(), "Create Controller",
        //                "Please, sir, will you be so kind to provide a name for the new controller", "", null);
        //        if (dialog.open() == Window.CANCEL)
        //            return null;
        //        String name = dialog.getValue();
        
        final org.eclipse.core.resources.IFolder controllersFolder = railsProject.getProject().getFolder(
                RailsNamingConventions.APP_CONTROLLERS_PATH);
        if (!controllersFolder.exists())
            try {
                controllersFolder.create(true, true, null);
            } catch (CoreException e) {
                Activator.unexpectedError(e);
                return null;
            }
        
        final String body = "\n" + "class YourCoolController < ApplicationController\n" + "end\n";
        String fileName;
        IFile file;
        int suffix = 1;
        do {
            String stringSuffix = (suffix == 1 ? "" : "_" + suffix);
            fileName = "(new_controller" + stringSuffix + ").rb";
            file = controllersFolder.getFile(fileName);
        } while (file.exists());
        
        final String finalFileName = fileName;
        
        new Job("Creating controller") {
            
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                final IFile file = createFile(body, controllersFolder, finalFileName,
                        "Controller creation failed");
                if (file != null) {
                    Display.getDefault().asyncExec(new Runnable() {
                        public void run() {
                            HumaneRubyEditor editor = (HumaneRubyEditor) openEditor(file);
                            editor.enterLinkedMode();
                        }
                    });
                }
                return Status.OK_STATUS;
            }
            
        }.schedule();
        return null;
    }
    
    private IEditorPart openEditor(IFile file) {
        try {
            return IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file);
        } catch (PartInitException e) {
            Activator.unexpectedError(e);
            return null;
        }
    }
    
    private void openEditor(IModelElement element) {
        try {
            IEditorPart part = EditorUtility.openInEditor(element, true);
            if (element instanceof IModelElement)
                EditorUtility.revealInEditor(part, element);
        } catch (PartInitException e) {
            Activator.unexpectedError(e);
        } catch (ModelException e) {
            Activator.unexpectedError(e);
        }
    }
    
    protected IFile createFile(String body, IFolder folder, String fileNameWithPath,
            String userActionFailedMessage) {
        if (fileNameWithPath.contains("/")) {
            Path path = new Path(fileNameWithPath);
            IPath purePath = path.removeLastSegments(1);
            IFolder folderToCreate = folder.getFolder(purePath);
            if (!folderToCreate.exists())
                try {
                    folderToCreate.create(true, true, null);
                } catch (CoreException e) {
                    Activator.reportException(e, userActionFailedMessage);
                    return null;
                }
            folder = folderToCreate;
            fileNameWithPath = path.lastSegment();
        }
        IModelElement modelElement = DLTKCore.create(folder);
        if (modelElement instanceof IProjectFragment) {
            IProjectFragment projectFragment = (IProjectFragment) modelElement;
            modelElement = projectFragment.getScriptFolder("");
        }
        if (modelElement instanceof IScriptFolder) {
            IScriptFolder scriptFolder = (IScriptFolder) modelElement;
            try {
                ISourceModule sourceModule = scriptFolder.createSourceModule(fileNameWithPath, body, true,
                        new NullProgressMonitor());
                IFile newFile = (IFile) sourceModule.getCorrespondingResource();
                return newFile;
            } catch (ModelException e) {
                Activator.reportException(e, userActionFailedMessage);
            }
        } else {
            System.out.println("Context.setValue() - modelElement is " + modelElement.getClass());
        }
        return null;
    }
}
