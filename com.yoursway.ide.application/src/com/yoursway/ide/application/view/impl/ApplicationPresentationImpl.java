package com.yoursway.ide.application.view.impl;

import org.eclipse.swt.widgets.Display;

import com.yoursway.databinding.Realm;
import com.yoursway.databinding.SWTObservables;
import com.yoursway.ide.application.view.application.ApplicationPresentation;
import com.yoursway.ide.application.view.application.ApplicationPresentationCallback;
import com.yoursway.ide.application.view.mainwindow.MainWindow;
import com.yoursway.ide.application.view.mainwindow.MainWindowCallback;
import com.yoursway.ide.application.view.mainwindow.MainWindowModel;

public class ApplicationPresentationImpl implements ApplicationPresentation {
    
    private Display display;
    private final ApplicationPresentationCallback callback;

    public ApplicationPresentationImpl(ApplicationPresentationCallback callback) {
        if (callback == null)
            throw new NullPointerException("callback is null");
        this.callback = callback;
        
        display = new Display();
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

    public MainWindow openWindow(MainWindowModel windowModel, MainWindowCallback callback) {
        return new MainWindowImpl(display, windowModel, callback);
    }

    public Realm getDefaultBindingRealm() {
        return SWTObservables.getRealm(display);
    }
    
}
