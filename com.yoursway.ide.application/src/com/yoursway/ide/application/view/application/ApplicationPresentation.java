package com.yoursway.ide.application.view.application;

import com.yoursway.databinding.Realm;
import com.yoursway.ide.application.view.ViewDefinitionFactory;
import com.yoursway.ide.application.view.mainwindow.MainWindowAreas;
import com.yoursway.ide.application.view.mainwindow.MainWindowFactory;

public interface ApplicationPresentation extends MainWindowFactory {
    
    Realm getDefaultBindingRealm();

    void runEventLoop();

    void terminateEventLoop();
    
    ViewDefinitionFactory viewDefinitions();
    
    MainWindowAreas mainWindowAreas();
    
}
