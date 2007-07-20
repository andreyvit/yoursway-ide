package com.yoursway.common.resources;

import org.eclipse.core.resources.IResourceDelta;

public abstract class ResourceDeltaSwitch {
    
    protected abstract void visitAddedDelta(IResourceDelta delta);
    
    protected abstract void visitRemovedDelta(IResourceDelta delta);
    
    protected abstract void visitChangedDelta(IResourceDelta delta);
    
    public final void visit(IResourceDelta delta) {
        switch (delta.getKind()) {
        case IResourceDelta.ADDED:
            visitAddedDelta(delta);
            break;
        case IResourceDelta.REMOVED:
            visitRemovedDelta(delta);
            break;
        case IResourceDelta.CHANGED:
            visitChangedDelta(delta);
            break;
        }
    }
}
