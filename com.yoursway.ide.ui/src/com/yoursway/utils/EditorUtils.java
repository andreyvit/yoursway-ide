package com.yoursway.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.ui.editor.EditorUtility;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.yoursway.ide.ui.Activator;

public class EditorUtils {

    public static IEditorPart openEditor(IFile file) {
        try {
            return IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file);
        } catch (PartInitException e) {
            Activator.unexpectedError(e);
        }
        return null;
    }

    public static IEditorPart openEditor(IModelElement element) {
        try {
            IEditorPart part = EditorUtility.openInEditor(element, true);
            if (element instanceof IModelElement)
                EditorUtility.revealInEditor(part, element);
            return part;
        } catch (PartInitException e) {
            Activator.unexpectedError(e);
        } catch (ModelException e) {
            Activator.unexpectedError(e);
        }
        return null;
    }

    public static IFile createFile(String body, IFolder folder, String fileNameWithPath,
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

    public static String chooseUniqueFileName(final org.eclipse.core.resources.IFolder folder,
            final String namePrefix, final String nameSuffix) {
        String fileName;
        IFile file;
        int suffix = 1;
        do {
            String stringSuffix = (suffix == 1 ? "" : "_" + suffix);
            fileName = namePrefix + stringSuffix + nameSuffix;
            file = folder.getFile(fileName);
        } while (file.exists());
        return fileName;
    }
    
}
