package com.yoursway.ide.application;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.ide.application.view.impl.ApplicationMenuFactory;
import com.yoursway.ide.application.view.impl.CommandExecutor;

public class ApplicationMenuFactoryImpl implements ApplicationMenuFactory {

    public Menu createMenuFor(Display display, CommandExecutor executor) {
        return new ApplicationMenu(display, executor).getMenu();
    }

    public Menu createMenuFor(Shell shell, CommandExecutor executor) {
        return new ApplicationMenu(shell, executor).getMenu();
    }
    
}
