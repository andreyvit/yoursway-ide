package com.yoursway.ide.application.view.impl;

import static com.yoursway.swt.additions.YsSwtUtils.centerShellOnNearestMonitor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.ide.application.view.mainwindow.MainWindow;
import com.yoursway.ide.application.view.mainwindow.MainWindowCallback;

public class MainWindowImpl implements MainWindow {
    
    private final MainWindowCallback callback;
    
    public MainWindowImpl(Display display, MainWindowCallback callback) {
        if (callback == null)
            throw new NullPointerException("callback is null");
        this.callback = callback;
        
        Shell shell = new Shell(display);
        shell.setLayout(new GridLayout(1, false));
        
        new Label(shell, SWT.NONE).setText("Hello, world!");
        
        shell.pack();
        shell.setSize(600, 300);
        centerShellOnNearestMonitor(shell);
        shell.open();
    }
    
}
