package com.yoursway.ide.application.view.mainwindow;

import com.yoursway.ide.application.view.impl.CommandExecutor;

public interface MainWindowCallback extends CommandExecutor {

    void activated();

    void deactivated();

    void windowDisposed();
    
}
