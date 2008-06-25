package com.yoursway.ide.application.view.mainwindow;

import com.yoursway.ide.application.view.impl.ViewArea;

public abstract class MainWindowArea extends ViewArea {

    public MainWindowArea(String debuggingName, boolean multipleAllowed) {
        super(debuggingName, multipleAllowed);
    }
    
    public abstract void accept(MainWindowViewAreaVisitor visitor);
    
}
