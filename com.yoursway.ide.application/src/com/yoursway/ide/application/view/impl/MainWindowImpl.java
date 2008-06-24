package com.yoursway.ide.application.view.impl;

import static com.yoursway.swt.additions.YsSwtUtils.centerShellOnNearestMonitor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.databinding.SwtUpdater;
import com.yoursway.ide.application.view.mainwindow.MainWindow;
import com.yoursway.ide.application.view.mainwindow.MainWindowCallback;
import com.yoursway.ide.application.view.mainwindow.MainWindowModel;

public class MainWindowImpl implements MainWindow {
    
    private final MainWindowCallback callback;
    private final MainWindowModel windowModel;
    private Shell shell;
    private Label helloLabel;
    
    public MainWindowImpl(Display display, final MainWindowModel windowModel, MainWindowCallback callback) {
        if (windowModel == null)
            throw new NullPointerException("windowModel is null");
        if (callback == null)
            throw new NullPointerException("callback is null");
        this.windowModel = windowModel;
        this.callback = callback;
        
        shell = new Shell(display);
        shell.setLayout(new GridLayout(1, false));
        
        helloLabel = new Label(shell, SWT.NONE);
        
        new SwtUpdater(shell) {
            protected void updateControl() {
                shell.setText(windowModel.projectLocation().getValue() + " - " + 
                        windowModel.projectType().getValue().getDescriptiveName());
                helloLabel.setText("Hello, world to " + windowModel.projectLocation().getValue().getName() + "!");
                
                // needed for the text to be visible 
                shell.layout();
            }
        };
        
        shell.setSize(600, 300);
        centerShellOnNearestMonitor(shell);
        shell.open();
    }
    
}
