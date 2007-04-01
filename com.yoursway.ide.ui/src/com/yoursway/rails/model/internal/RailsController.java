package com.yoursway.rails.model.internal;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.dltk.core.IType;

import com.yoursway.rails.model.IRailsAction;
import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsControllersCollection;

public class RailsController implements IRailsController {
    
    private final IRailsControllersCollection parent;
    private final IFile file;
    private IType controllerType;
    
    public RailsController(IRailsControllersCollection parent, IFile file) {
        this.parent = parent;
        this.file = file;
        updateType();
    }
    
    private void updateType() {
        controllerType = RailsFileUtils.findControllerTypeInFile(file);
    }
    
    public Collection<IRailsAction> getActions() {
        return null;
    }
    
    public IFile getFile() {
        return file;
    }
    
    public IType getType() {
        return controllerType;
    }
    
    public void reconcile(RailsDeltaBuilder deltaBuilder, IResourceDelta delta) {
        updateType();
    }
    
}
