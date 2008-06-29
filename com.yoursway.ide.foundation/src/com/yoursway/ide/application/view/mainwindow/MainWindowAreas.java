package com.yoursway.ide.application.view.mainwindow;

import com.yoursway.ide.application.view.impl.WindowDef;

public class MainWindowAreas extends WindowDef {
    
    private MainWindowAreas() {
    }
    
    public final static MainWindowArea projectViewArea = new MainWindowArea("Project View", false) {
        public void accept(MainWindowViewAreaVisitor visitor) {
            visitor.visitProjectViewArea();
        }
    };
    
    public final static MainWindowArea generalArea = new MainWindowArea("General", true) {
        public void accept(MainWindowViewAreaVisitor visitor) {
            visitor.visitGeneralArea();
        }
    };
    
}
