package com.yoursway.rails.model.internal;

import org.eclipse.core.resources.IFile;

import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsPartial;

public class RailsPartial extends RailsBaseView implements IRailsPartial {
    
    public RailsPartial(IRailsController controller, IFile file, String name) {
        super(controller, file, name);
    }
    
}
