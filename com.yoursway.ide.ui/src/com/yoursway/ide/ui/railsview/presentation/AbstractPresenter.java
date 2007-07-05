package com.yoursway.ide.ui.railsview.presentation;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.undo.DeleteResourcesOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;

import com.yoursway.ide.ui.Activator;

public abstract class AbstractPresenter implements IElementPresenter {
    
    public final class DeleteFileAction extends Action {
        
        private final IFile file;
        
        public DeleteFileAction(IFile file) {
            super("Delete " + file.getProjectRelativePath());
            this.file = file;
        }
        
        @Override
        public void runWithEvent(Event event) {
            scheduleDeleteJob(new IResource[] { file });
        }
    }
    
    private final IPresenterOwner owner;
    
    public AbstractPresenter(IPresenterOwner owner) {
        this.owner = owner;
    }
    
    protected void openEditor(IFile file) {
        try {
            IDE.openEditor(owner.getWorkbenchPage(), file);
        } catch (PartInitException e) {
            Activator.log(e);
        }
    }
    
    protected void openEditor(IModelElement element) {
        try {
            IEditorPart part = EditorUtility.openInEditor(element, true);
            if (element instanceof IModelElement)
                EditorUtility.revealInEditor(part, element);
        } catch (PartInitException e) {
            Activator.log(e);
        } catch (ModelException e) {
            Activator.log(e);
        }
    }
    
    protected void addRenameResourceAction(MenuManager menuManager) {
        
    }
    
    public IPresenterOwner getOwner() {
        return owner;
    }
    
    /**
     * Schedule a job to delete the resources to delete.
     * 
     * @param resourcesToDelete
     */
    protected void scheduleDeleteJob(final IResource[] resourcesToDelete) {
        // use a non-workspace job with a runnable inside so we can avoid
        // periodic updates
        Job deleteJob = new Job("Deleting resources") {
            @Override
            public IStatus run(IProgressMonitor monitor) {
                try {
                    DeleteResourcesOperation op = new DeleteResourcesOperation(resourcesToDelete,
                            "Delete Resources", false);
                    return PlatformUI.getWorkbench().getOperationSupport().getOperationHistory().execute(
                            op,
                            monitor,
                            WorkspaceUndoUtil.getUIInfoAdapter(owner.getWorkbenchPage().getWorkbenchWindow()
                                    .getShell()));
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof CoreException) {
                        return ((CoreException) e.getCause()).getStatus();
                    }
                    return new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
                }
            }
            
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
             */
            @Override
            public boolean belongsTo(Object family) {
                if ("Deleting resources".equals(family)) {
                    return true;
                }
                return super.belongsTo(family);
            }
            
        };
        //        deleteJob.setUser(true);
        deleteJob.schedule();
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
    
    public void measureItem(TreeItem item, Object element, Event event) {
        //        Rectangle bounds = item.getBounds();
        //        event.width = bounds.width;
        //        event.height = bounds.height;
    }
    
    public void eraseItem(TreeItem item, Object element, Event event) {
    }
    
    public void paintItem(TreeItem item, Object element, Event event) {
    }
    
}
