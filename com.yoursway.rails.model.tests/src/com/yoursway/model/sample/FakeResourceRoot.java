package com.yoursway.model.sample;

import java.util.Collections;

import com.yoursway.model.repository.IBasicModelChangesRequestor;
import com.yoursway.model.repository.ICollectionHandle;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.Scheduler;

public class FakeResourceRoot implements IResourceModelRoot {
    
    private IBasicModelChangesRequestor changesRequestor;
    
    public FakeResourceRoot(Scheduler scheduler) {
        changesRequestor = scheduler.addBasicModel(IResourceModelRoot.class, this);
    }
    
    public ICollectionHandle<IResourceProject> projects() {
        return null;
    }
    
    public void simulateChange() {
//        changesRequestor.theGivenPieceOfShitChanged(new MapBasedSnapshot(), Collections
//                .<IHandle<?>> singleton(this));
    }
    
}
