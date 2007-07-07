package com.yoursway.ide.projects.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.yoursway.ide.projects.YourSwayProjects;

public class CreateRailsApplicationHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        YourSwayProjects.createRailsApplication();
        return null;
    }
    
}
