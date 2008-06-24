package com.yoursway.ide.application.view.mainwindow;

import java.io.File;

import com.yoursway.databinding.IObservableValue;
import com.yoursway.ide.application.model.projects.types.ProjectType;

public interface MainWindowModel {
    
    IObservableValue<File> projectLocation();
    
    IObservableValue<ProjectType> projectType();
    
}
