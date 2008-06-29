package com.yoursway.ide.application.view.mainwindow;

import java.io.File;

import org.eclipse.core.databinding.observable.value.IObservableValue;

import com.yoursway.ide.application.model.projects.types.ProjectType;

public interface MainWindowModel {
    
    IObservableValue<File> projectLocation();
    
    IObservableValue<ProjectType> projectType();
    
}
