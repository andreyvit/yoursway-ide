package com.yoursway.ide.application.view.impl;

import com.yoursway.ide.application.view.impl.commands.Command;

public interface CommandExecutor {
    
    void execute(Command command);
    
}
