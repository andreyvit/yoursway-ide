package com.yoursway.rails.model.internal;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsView;

public class RailsView extends RailsBaseView implements IRailsView {
    
    public RailsView(IRailsController controller, IFile file, String name) {
        super(controller, file, name);
        // TODO Auto-generated constructor stub
    }
    
}
