package com.yoursway.ide.application.controllers;

import static com.yoursway.ide.application.problems.Severity.USER_COMMAND_IGNORED;
import static com.yoursway.utils.DebugOutputHelper.simpleNameOf;

import com.yoursway.ide.application.problems.Bugs;
import com.yoursway.ide.application.view.impl.CommandExecutor;
import com.yoursway.ide.application.view.impl.commands.Command;
import com.yoursway.ide.application.view.impl.commands.Handler;

public class CommandDispatcher implements CommandExecutor {
    
    private final Handler delegate;
    
    public CommandDispatcher(Handler delegate) {
        if (delegate == null)
            throw new NullPointerException("delegate is null");
        this.delegate = delegate;
    }
    
    public void execute(Command command) {
        if (!command.invokeSpecificHandler(delegate)) {
            Bugs.illegalCaseRecovery(USER_COMMAND_IGNORED, "No handler found for " + simpleNameOf(command));
        }
    }
    
}
