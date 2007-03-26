package com.yoursway.utils;

import org.eclipse.core.resources.IFile;
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
    
}
