package com.yoursway.ide.application.view.impl;

import java.io.File;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.ide.application.view.ViewDefinitionFactory;
import com.yoursway.ide.application.view.application.ApplicationPresentation;
import com.yoursway.ide.application.view.application.ApplicationPresentationCallback;
import com.yoursway.ide.application.view.mainwindow.MainWindow;
import com.yoursway.ide.application.view.mainwindow.MainWindowCallback;
import com.yoursway.ide.application.view.mainwindow.MainWindowModel;
import com.yoursway.ide.platforms.api.GlobalMenuSupport;
import com.yoursway.ide.platforms.api.NativeGlobalAlerts;
import com.yoursway.ide.platforms.api.PlatformSupport;

public class ApplicationPresentationImpl implements ApplicationPresentation {
    
    private Display display;
    
    private final ApplicationPresentationCallback callback;
    
    private ViewDefinitionFactory viewDefinitions = new ViewDefinitionFactoryImpl();
    
    private final PlatformSupport platformSupport;
    
    private final ApplicationMenuFactory menuFactory;
    
    public ApplicationPresentationImpl(ApplicationPresentationCallback callback,
            PlatformSupport platformSupport, ApplicationMenuFactory menuFactory) {
        if (callback == null)
            throw new NullPointerException("callback is null");
        if (platformSupport == null)
            throw new NullPointerException("platformSupport is null");
        if (menuFactory == null)
            throw new NullPointerException("menuFactory is null");
        this.callback = callback;
        this.platformSupport = platformSupport;
        this.menuFactory = menuFactory;
        
        display = new Display();
        
        GlobalMenuSupport globalMenuSupport = platformSupport.globalMenuSupport();
        if (globalMenuSupport != null)
            globalMenuSupport.setGlobalApplicationMenu(display, menuFactory.createMenuFor(display, callback));
    }
    
    public void runEventLoop() {
        while (!display.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }
    
    public void terminateEventLoop() {
        display.dispose();
    }
    
    public MainWindow createWindow(MainWindowModel windowModel, MainWindowCallback callback) {
        return new MainWindowImpl(display, windowModel, callback, viewDefinitions, platformSupport
                .globalMenuSupport() == null ? menuFactory : null);
    }
    
    public Realm getDefaultBindingRealm() {
        return SWTObservables.getRealm(display);
    }
    
    public ViewDefinitionFactory viewDefinitions() {
        return viewDefinitions;
    }
    
    public File chooseProjectToOpen() {
        Shell fakeShell = new Shell();
        try {
            DirectoryDialog dialog = new DirectoryDialog(fakeShell, 0);
            dialog.setText("Open project");
            String choice = dialog.open();
            if (choice == null)
                return null;
            else
                return new File(choice);
        } finally {
            fakeShell.dispose();
        }
    }
    
    public void displayFailedToOpenProjectError(File file) {
        NativeGlobalAlerts nativeGlobalAlerts = platformSupport.nativeGlobalAlerts();
        String title = "Opening failed";
        String details = String.format("Could not read from file “%s”.", file.getPath());
        if (nativeGlobalAlerts != null)
            nativeGlobalAlerts.displayGlobalAlert(title, details);
        else {
            MessageDialog.openError(null, title, details);
        }
    }
    
}
