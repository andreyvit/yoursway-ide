package com.yoursway.model.resource;

import java.util.Collection;

import org.eclipse.core.resources.IProject;

import com.yoursway.model.repository.IBasicModelChangesRequestor;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.Scheduler;

public class FakeResourceRoot implements IResourceModelRoot {
    
    private final IBasicModelChangesRequestor changesRequestor;
    
    public FakeResourceRoot(Scheduler scheduler) {
        changesRequestor = scheduler.addBasicModel(IResourceModelRoot.class, this);
    }
    
    public IHandle<Collection<IResourceProject>> projects() {
        return null;
    }
    
    public void simulateChange() {
        //        changesRequestor.theGivenPieceOfShitChanged(new MapBasedSnapshot(), Collections
        //                .<IHandle<?>> singleton(this));
    }
    
    public IHandle<IResourceProject> project(IProject eclipseProject) {
        return null;
    }
    
}
