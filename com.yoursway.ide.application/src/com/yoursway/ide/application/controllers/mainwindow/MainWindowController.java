package com.yoursway.ide.application.controllers.mainwindow;

import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.view.mainwindow.MainWindow;
import com.yoursway.ide.application.view.mainwindow.MainWindowCallback;
import com.yoursway.ide.application.view.mainwindow.MainWindowFactory;

public class MainWindowController implements MainWindowCallback {

    private MainWindow window;
    private MainWindowModelImpl windowModel;

    public MainWindowController(Project project, MainWindowFactory presentation) {
        this.windowModel = new MainWindowModelImpl();
        windowModel.projectLocation.setValue(project.getLocation());
        windowModel.projectType.setValue(project.getType());
        
        this.window = presentation.openWindow(windowModel, this);
    }
    
}
