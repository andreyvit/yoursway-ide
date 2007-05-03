package com.yoursway.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;

public abstract class ResourceSwitch implements IResourceVisitor {
    
    public final boolean visit(IResource resource) throws CoreException {
        switch (resource.getType()) {
        case IResource.FILE:
            return visitFile((IFile) resource);
        case IResource.FOLDER:
            return visitFolder((IFolder) resource);
        case IResource.PROJECT:
            return visitProject((IProject) resource);
        case IResource.ROOT:
            return visitRoot((IWorkspaceRoot) resource);
        }
        throw new AssertionError("Unreachable code");
    }
    
    protected abstract boolean visitRoot(IWorkspaceRoot resource);
    
    protected abstract boolean visitProject(IProject resource);
    
    protected abstract boolean visitFolder(IFolder resource);
    
    protected abstract boolean visitFile(IFile resource);
    
}
