package com.yoursway.ide.application.view.application;

import java.io.File;

import org.eclipse.core.databinding.observable.Realm;

import com.yoursway.ide.application.view.ViewDefinitionFactory;
import com.yoursway.ide.application.view.mainwindow.MainWindowAreas;
import com.yoursway.ide.application.view.mainwindow.MainWindowFactory;

public interface ApplicationPresentation extends MainWindowFactory {
    
    Realm getDefaultBindingRealm();

    void runEventLoop();

    void terminateEventLoop();
    
    ViewDefinitionFactory viewDefinitions();
    
    File chooseProjectToOpen();

    void displayFailedToOpenProjectError(File file);
    
}
