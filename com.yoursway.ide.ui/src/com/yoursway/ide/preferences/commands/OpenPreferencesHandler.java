package com.yoursway.ide.preferences.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.yoursway.ide.preferences.YourSwayPreferences;

public class OpenPreferencesHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent arg0) throws ExecutionException {
        YourSwayPreferences.show();
        return null;
    }
    
}
