package com.yoursway.utils;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
    
    public static boolean changedInDelta(IResourceDelta delta, IPath filePath) {
        Assert.isNotNull(delta);
        Assert.isNotNull(filePath);
        final int segmentCount = filePath.segmentCount();
        Assert.isTrue(segmentCount > 0);
        IResourceDelta currentDelta = delta;
        for (int i = 0; i < segmentCount; i++) {
            String segment = filePath.segment(i);
            currentDelta = currentDelta.findMember(new Path(segment));
            if (currentDelta == null)
                return false;
            int kind = currentDelta.getKind();
            if (kind == IResourceDelta.ADDED || kind == IResourceDelta.REMOVED)
                return true;
        }
        Assert.isTrue(currentDelta.getKind() == IResourceDelta.CHANGED);
        return true;
    }
    
}
