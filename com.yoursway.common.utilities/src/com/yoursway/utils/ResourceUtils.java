package com.yoursway.utils;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class ResourceUtils {
    
    /**
     * Returns the given subfolder of the given container, creating it if
     * necessary. The container must already exist.
     * 
     * @param container
     * @param path
     * @return the given subfolder
     * @throws CoreException
     *             if the creation failed.
     */
    public static IFolder lookupOrCreateSubfolder(IContainer container, IPath path) throws CoreException {
        final org.eclipse.core.resources.IFolder controllersFolder;
        controllersFolder = container.getFolder(path);
        if (!controllersFolder.exists()) {
            if (path.segmentCount() > 1)
                lookupOrCreateSubfolder(container, path.removeLastSegments(1));
            controllersFolder.create(true, true, null);
        }
        return controllersFolder;
    }
    
    /**
     * Checks if the given delta contains any changes affecting the given child
     * (or grandchild) of the resouce represented by the delta.
     * 
     * @param delta
     * @param filePath
     *            a path relative to the <code>delta</code>
     * @return <code>true</code> if the resource at the given path has
     *         changed, or any of its parents were added or removed;
     *         <code>false</code> otherwise.
     */
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
    
    public static boolean isNotFoundOrOutOfSync(CoreException e) {
        int code = e.getStatus().getCode();
        return code == IResourceStatus.NOT_FOUND_LOCAL || code == IResourceStatus.NO_LOCATION_LOCAL
                || code == IResourceStatus.OUT_OF_SYNC_LOCAL;
    }
    
}
