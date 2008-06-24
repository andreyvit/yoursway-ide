package com.yoursway.ide.application.controllers;

import com.yoursway.ide.application.view.mainwindow.MainWindow;
import com.yoursway.ide.application.view.mainwindow.MainWindowCallback;
import com.yoursway.ide.application.view.mainwindow.MainWindowFactory;

public class MainWindowController implements MainWindowCallback {

    private MainWindow window;

    public MainWindowController(MainWindowFactory presentation) {
        this.window = presentation.openWindow(this);
    }
    
}
