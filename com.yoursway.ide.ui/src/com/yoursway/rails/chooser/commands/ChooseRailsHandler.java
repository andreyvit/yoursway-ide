package com.yoursway.rails.chooser.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.yoursway.rails.chooser.RailsProvider;
import com.yoursway.rails.discovering.RubyAndRailsDiscovering;

public class ChooseRailsHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        RubyAndRailsDiscovering.runSearchRails();
        RailsProvider.getInstance().showRailsChooser();
        return null;
    }
    
}
