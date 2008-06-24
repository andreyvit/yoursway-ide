package com.yoursway.ide.application.controllers.mainwindow;

import java.io.File;

import com.yoursway.databinding.IObservableValue;
import com.yoursway.databinding.WritableValue;
import com.yoursway.ide.application.model.projects.types.ProjectType;
import com.yoursway.ide.application.view.mainwindow.MainWindowModel;

public class MainWindowModelImpl implements MainWindowModel {
    
    public final WritableValue<File> projectLocation = WritableValue.withValueType(File.class);
    
    public final WritableValue<ProjectType> projectType = WritableValue.withValueType(ProjectType.class);

    public IObservableValue<File> projectLocation() {
        return projectLocation;
    }

    public IObservableValue<ProjectType> projectType() {
        return projectType;
    }
    
}
