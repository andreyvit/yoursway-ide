package com.yoursway.ide.application.controllers.mainwindow;

import java.io.File;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class FileContentProvider implements ITreeContentProvider {
    
    public void dispose() {
    }
    
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public Object[] getChildren(Object element) {
        File file = (File) element;
        File[] children = file.listFiles();
        if (children == null)
            return new Object[0];
        return children;
    }

    public Object getParent(Object element) {
        File file = (File) element;
        return file.getParentFile();
    }

    public boolean hasChildren(Object element) {
        File file = (File) element;
        File[] children = file.listFiles();
        return children != null && children.length > 0;
    }

    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof File)
            return getChildren(inputElement);
        return null;
    }
    
}
