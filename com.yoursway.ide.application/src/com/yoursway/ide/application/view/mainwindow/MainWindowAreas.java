package com.yoursway.ide.application.view.mainwindow;

import com.yoursway.ide.application.view.impl.WindowDef;

public class MainWindowAreas extends WindowDef {
    
    public final MainWindowArea projectViewArea = new MainWindowArea("Project View", false) {
        public void accept(MainWindowViewAreaVisitor visitor) {
            visitor.visitProjectViewArea();
        }
    };
    
    public final MainWindowArea generalArea = new MainWindowArea("General", true) {
        public void accept(MainWindowViewAreaVisitor visitor) {
            visitor.visitGeneralArea();
        }
    };
    
}
