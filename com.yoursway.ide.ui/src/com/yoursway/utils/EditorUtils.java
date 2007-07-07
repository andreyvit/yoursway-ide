package com.yoursway.utils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.IModelElement;
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
        IFile file = folder.getFile(fileNameWithPath);
        try {
            file.create(new ByteArrayInputStream(body.getBytes(ResourcesPlugin.getWorkspace().getRoot()
                    .getDefaultCharset())), true, new NullProgressMonitor());
        } catch (UnsupportedEncodingException e) {
            Activator.unexpectedError(e);
            return null;
        } catch (CoreException e) {
            Activator.reportException(e, userActionFailedMessage);
            return null;
        }
        return file;
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
            suffix++;
        } while (file.exists());
        return fileName;
    }
    
}
