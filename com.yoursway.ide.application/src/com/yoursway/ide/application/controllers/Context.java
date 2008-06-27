package com.yoursway.ide.application.controllers;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.yoursway.ide.application.problems.Severity.USER_COMMAND_IGNORED;
import static com.yoursway.utils.DebugOutputHelper.simpleNameOf;

import java.util.Collection;
import java.util.Map;

import com.yoursway.ide.application.problems.Bugs;
import com.yoursway.ide.application.view.impl.commands.Command;
import com.yoursway.ide.application.view.impl.commands.Handler;
import com.yoursway.utils.disposable.Disposable;
import com.yoursway.utils.disposable.Disposer;

public class Context implements Disposable, PublicContext {
    
    private final Context parent;
    
    private boolean active;
    
    private Collection<Context> activeChildren = newArrayList();
    
    private Map<Command, Handler> handlers = newHashMap();
    
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
    
    public void addHandler(Command command, Handler handler) {
        if (command == null)
            throw new NullPointerException("command is null");
        if (handler == null)
            throw new NullPointerException("handler is null");
        handlers.put(command, handler);
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
    
    public void execute(Command command) {
        if (!tryExecute(command))
            Bugs.illegalCaseRecovery(USER_COMMAND_IGNORED, "No handler found for " + simpleNameOf(command)
                    + " " + command.toString());
    }
    
    private boolean tryExecute(Command command) {
        for (Context child : activeChildren)
            if (child.tryExecute(command))
                return true;
        return tryExecuteHere(command);
    }
    
    private boolean tryExecuteHere(Command command) {
        Handler handler = handlers.get(command);
        if (handler == null)
            return false;
        else
            return handler.run(command);
    }
    
}
