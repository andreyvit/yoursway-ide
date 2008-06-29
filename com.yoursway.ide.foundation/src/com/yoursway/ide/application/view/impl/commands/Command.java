package com.yoursway.ide.application.view.impl.commands;

import java.util.Collection;

public interface Command {
    
    Collection<? extends Object> tags();

    boolean invokeSpecificHandler(Handler handler);
    
}
