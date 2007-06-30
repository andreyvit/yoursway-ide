package com.yoursway.rails.chooser.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.yoursway.rails.chooser.RailsProvider;
import com.yoursway.rails.search.RailsSearching;

public class ChooseRailsHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        RailsSearching.runSearchRails();
        RailsProvider.getInstance().showRailsChooser();
        return null;
    }
    
}
