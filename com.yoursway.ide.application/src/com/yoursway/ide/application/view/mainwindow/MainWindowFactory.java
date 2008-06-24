package com.yoursway.ide.application.view.mainwindow;

public interface MainWindowFactory {

    MainWindow openWindow(MainWindowModel windowModel, MainWindowCallback callback);
    
}
