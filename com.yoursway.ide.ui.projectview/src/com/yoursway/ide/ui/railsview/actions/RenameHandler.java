package com.yoursway.ide.ui.railsview.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

public class RenameHandler implements IHandler {
    
    public void addHandlerListener(IHandlerListener handlerListener) {
    }
    
    public void dispose() {
    }
    
    public Object execute(ExecutionEvent event) throws ExecutionException {
        System.out.println("RenameHandler.execute()");
        return null;
    }
    
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }
    
    public boolean isHandled() {
        // TODO Auto-generated method stub
        return false;
    }
    
    public void removeHandlerListener(IHandlerListener handlerListener) {
        // TODO Auto-generated method stub
        
    }
    
}
