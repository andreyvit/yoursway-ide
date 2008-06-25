package com.yoursway.ide.application.view.mainwindow;

import com.yoursway.ide.application.view.ViewFactory;

public interface MainWindow extends ViewFactory {
    
    MainWindowAreas definition();
    
    void open();
    
}
