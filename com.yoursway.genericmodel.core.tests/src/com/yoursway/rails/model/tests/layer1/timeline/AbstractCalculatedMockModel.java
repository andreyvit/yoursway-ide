package com.yoursway.rails.model.tests.layer1.timeline;

import com.yoursway.model.repository.ICalculatedModelUpdater;
import com.yoursway.model.repository.IModelRoot;
import com.yoursway.model.repository.IResolver;
import com.yoursway.model.repository.ModelDelta;
import com.yoursway.model.repository.NoSuchHandleException;
import com.yoursway.model.repository.Scheduler;
import com.yoursway.model.repository.SnapshotBuilder;
import com.yoursway.model.repository.SnapshotDeltaPair;

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
    
    public Class<? extends IModelRoot> getModelRootInterface() {
        return root.getClass();
    }
    
    protected Class<? extends IModelRoot> getKlass() {
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
    
    public SnapshotDeltaPair update(IResolver resolver) {
        if (frozen)
            return null;        
        SnapshotBuilder snapshotBuilder = new SnapshotBuilder();
        try {
            updateInternal(resolver, snapshotBuilder);
        } catch (AssertionError e) {
            e.printStackTrace();
            this.error = e;
        } catch (NoSuchHandleException e) {
            e.printStackTrace();
            this.error = new AssertionError(e);
        }
        updates++;
        return new SnapshotDeltaPair(snapshotBuilder.getSnapshot(), new ModelDelta(snapshotBuilder.getChangedHandles()));
    }
    
    public abstract void updateInternal(IResolver resolver, SnapshotBuilder snapshotBuilder) throws NoSuchHandleException;
    
}
