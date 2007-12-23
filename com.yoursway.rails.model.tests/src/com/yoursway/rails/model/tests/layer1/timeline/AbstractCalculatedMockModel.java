package com.yoursway.rails.model.tests.layer1.timeline;

import java.util.Set;

import com.yoursway.model.repository.ICalculatedModelUpdater;
import com.yoursway.model.repository.IHandle;
import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.repository.IResolver;
import com.yoursway.model.repository.NoSuchHandleException;
import com.yoursway.model.repository.Scheduler;
import com.yoursway.model.resource.internal.SnapshotBuilder;

public abstract class AbstractCalculatedMockModel implements ICalculatedModelUpdater {
    
    private int updates;
    private boolean frozen;
    private final IModelRoot root;
    private AssertionError error;
    
    @SuppressWarnings("unchecked")
    public AbstractCalculatedMockModel(Scheduler scheduler, IModelRoot root) {
        scheduler.registerModel((Class<IModelRoot>) root.getClass(), (IModelRoot) root, this);
        this.root = root;
        updates = 0;
        frozen = false;
    }
    
    public Class<?> getModelRootInterface() {
        return root.getClass();
    }
    
    protected Class<?> getKlass() {
        return root.getClass();
    }
    
    public int updates() {
        return updates;
    }
    
    public void freeze() {
        frozen = true;
    }
    
    public void unfreeze() {
        frozen = false;
    }
    
    public void check() throws AssertionError {
        if (error != null)
            throw error;
    }
    
    public AssertionError getError() {
        return error;
    }
    
    public void update(IResolver resolver, SnapshotBuilder snapshotBuilder, Set<IHandle<?>> changedHandles) {
        if (frozen)
            return;        
        try {
            updateInternal(resolver, snapshotBuilder, changedHandles);
        } catch (AssertionError e) {
            e.printStackTrace();
            this.error = e;
        } catch (NoSuchHandleException e) {
            this.error = new AssertionError(e);
        }
        updates++;
    }
    
    public abstract void updateInternal(IResolver resolver, SnapshotBuilder snapshotBuilder,
            Set<IHandle<?>> changedHandles) throws NoSuchHandleException;
    
}
