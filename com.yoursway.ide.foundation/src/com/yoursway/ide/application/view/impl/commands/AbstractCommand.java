package com.yoursway.ide.application.view.impl.commands;

import static com.yoursway.utils.DebugOutputHelper.simpleNameOf;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.Collection;

public abstract class AbstractCommand implements Command {
    
    private final String debuggingName;
    private final Class<? extends Handler> handlerContract;
    
    public AbstractCommand(Class<? extends Handler> handlerContract) {
        this.handlerContract = handlerContract;
        this.debuggingName = interfaceToName(handlerContract);
    }
    
    public AbstractCommand(String debuggingName) {
        this.debuggingName = debuggingName;
        this.handlerContract = null;
    }
    
    public Collection<? extends Object> tags() {
        if (handlerContract == null)
            return emptyList();
        else
            return singletonList(handlerContract);
    }
    
    public boolean invokeSpecificHandler(Handler handler) {
        return false;
    }

    private static String interfaceToName(Class<? extends Handler> handlerContract) {
        String name = simpleNameOf(handlerContract);
        name = name.replaceAll("^HandlerOf", "");
        name = name.replaceAll("^Handler", "");
        name = name.replaceAll("Handler$", "");
        name.replaceAll("([A-Z])", " $1");
        return name.trim();
    }
    
    @Override
    public String toString() {
        return debuggingName;
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        return (getClass() == obj.getClass());
    }
    
}
