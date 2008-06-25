package com.yoursway.ide.application.view.mainwindow;

public interface MainWindowFactory {

    MainWindow createWindow(MainWindowModel windowModel, MainWindowCallback callback);
    
}
