package com.yoursway.ide.preferences.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.yoursway.ide.preferences.PreferencesDialog;

public class OpenPreferencesHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent arg0) throws ExecutionException {
        PreferencesDialog.show();
        return null;
    }
    
}
