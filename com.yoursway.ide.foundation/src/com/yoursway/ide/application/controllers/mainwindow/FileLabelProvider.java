package com.yoursway.ide.application.controllers.mainwindow;

import java.io.File;

import org.eclipse.jface.viewers.IViewerLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ViewerLabel;

public class FileLabelProvider extends LabelProvider implements IViewerLabelProvider {
    
    public void updateLabel(ViewerLabel label, Object element) {
        File file = (File) element;
        String name = file.getName();
        if (file.isDirectory())
            label.setText(name + "/");
        else
            label.setText(name);
    }
    
}
