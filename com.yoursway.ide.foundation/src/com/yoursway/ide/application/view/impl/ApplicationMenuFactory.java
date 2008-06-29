package com.yoursway.ide.application.view.impl;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

public interface ApplicationMenuFactory {
    
    Menu createMenuFor(Display display, CommandExecutor executor);
    
    Menu createMenuFor(Shell shell, CommandExecutor executor);
    
}
