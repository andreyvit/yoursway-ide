package com.yoursway.ide.application.view.impl.commands;

public abstract class AbstractHandler implements Handler {
    
    public boolean run(Command command) {
        return command.invokeSpecificHandler(this);
    }
    
}
