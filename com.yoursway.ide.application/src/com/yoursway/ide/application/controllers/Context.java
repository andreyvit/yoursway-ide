package com.yoursway.ide.application.controllers;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import com.yoursway.utils.disposable.Disposable;
import com.yoursway.utils.disposable.Disposer;

public class Context implements Disposable, PublicContext {
    
    private final Context parent;
    
    private boolean active;
    
    private Collection<Context> activeChildren = newArrayList();

    public Context(Disposer disposer) {
        this.parent = null;
        this.active = true;
        disposer.addDisposeListener(this);
    }
    
    public Context(Disposer disposer, Context parent) {
        if (parent == null)
            throw new NullPointerException("parent is null");
        this.parent = parent;
        disposer.addDisposeListener(this);
    }
    
    public void setActive(boolean active) {
        if (parent == null)
            throw new IllegalStateException("Cannot activate/deactivate the root context");
        if (active != this.active) {
            this.active = active;
            if (active)
                parent.addActiveChild(this);
            else
                parent.removeActiveChild(this);
        }
    }
    
    void addActiveChild(Context context) {
        if (context == null)
            throw new NullPointerException("context is null");
        activeChildren.add(context);
    }

    void removeActiveChild(Context context) {
        if (context == null)
            throw new NullPointerException("context is null");
        activeChildren.remove(context);
    }

    public void dispose() {
        if (parent != null)
            setActive(false);
    }
    
}
