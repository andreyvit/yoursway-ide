package com.yoursway.utils;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class ResourceUtils {

    public static IFolder lookupOrCreateSubfolder(final IContainer container, final Path path)
            throws CoreException {
        final org.eclipse.core.resources.IFolder controllersFolder;
        controllersFolder = container.getFolder(path);
        if (!controllersFolder.exists())
            controllersFolder.create(true, true, null);
        return controllersFolder;
    }
    
}
