package com.yoursway.ide.projects.editor;

import org.eclipse.core.databinding.observable.value.IObservableValue;

public class ViewModel {
    
    private final IObservableValue projectName;
    private final IObservableValue projectLocation;
    
    public ViewModel(IObservableValue projectName, IObservableValue projectLocation) {
        this.projectName = projectName;
        this.projectLocation = projectLocation;
    }
    
    public IObservableValue getProjectName() {
        return projectName;
    }
    
    public IObservableValue getProjectLocation() {
        return projectLocation;
    }
    
}
