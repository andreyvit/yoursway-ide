package com.yoursway.common.resources;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

public class PathUtils {
    
    private PathUtils() {
        throw new AssertionError("Cannot instantiate utility class");
    }
    
    public static boolean startsWith(IPath path, String[] segments) {
        if (segments.length > path.segmentCount())
            return false;
        for (int i = 0; i < segments.length; i++) {
            if (!segments[i].equals(path.segment(i)))
                return false;
        }
        return true;
    }
    
    public static String getBaseNameWithoutExtension(IFile file) {
        String name = file.getName();
        String ext = file.getFileExtension();
        if (ext == null)
            return name;
        int pos = name.lastIndexOf("." + ext);
        return (pos >= 0 ? name.substring(0, pos) : name);
    }
    
    public static String[] determinePathComponents(IFolder referenceFolder, IFile file) {
        Collection<IFolder> folders = new ArrayList<IFolder>();
        if (referenceFolder != null) {
            IContainer parentFolder = file.getParent();
            while (parentFolder != null && !parentFolder.equals(referenceFolder)) {
                if (parentFolder instanceof IFolder) {
                    IFolder folder = (IFolder) parentFolder;
                    folders.add(folder);
                }
                parentFolder = parentFolder.getParent();
            }
        }
        
        String[] pathComponents = new String[folders.size() + 1];
        int index = folders.size() - 1;
        for (IFolder folder : folders) {
            pathComponents[index--] = folder.getName();
        }
        pathComponents[folders.size()] = getBaseNameWithoutExtension(file);
        return pathComponents;
    }
    
    public static IFolder getFolder(IContainer container, String path) {
        IResource member = container.findMember(path);
        if (member instanceof IFolder)
            return (IFolder) member;
        return null;
    }
    
    public static IFile getFile(IContainer container, String path) {
        IResource member = container.findMember(path);
        if (member instanceof IFile)
            return (IFile) member;
        return null;
    }
    
    public static IFile getFile(IContainer container, IPath path) {
        IResource member = container.findMember(path);
        if (member instanceof IFile)
            return (IFile) member;
        return null;
    }
    
    public static boolean isIgnoredResourceOrNoExtension(IResource resource) {
        String extension = resource.getFileExtension();
        if (extension == null || extension.length() == 0)
            return false;
        return isIgnoredResource(resource);
    }
    
    public static boolean isIgnoredResource(IResource resource) {
        String name = resource.getName();
        if (name.length() == 0 || name.charAt(0) == '.' || "CVS".equals(name) || "_darcs".equals(name))
            return true;
        return false;
    }
    
}
