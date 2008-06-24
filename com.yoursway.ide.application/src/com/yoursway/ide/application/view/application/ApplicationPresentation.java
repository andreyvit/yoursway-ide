package com.yoursway.ide.application.view.application;

import com.yoursway.ide.application.view.mainwindow.MainWindowFactory;

public interface ApplicationPresentation extends MainWindowFactory {

    void runEventLoop();

    void terminateEventLoop();
    
}
