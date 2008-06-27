package com.yoursway.ide.application.view.impl.commands;

import java.util.Collection;

public interface Command {
    
    boolean invokeSpecificHandler(Handler handler);
    
    Collection<? extends Object> tags();
    
}
