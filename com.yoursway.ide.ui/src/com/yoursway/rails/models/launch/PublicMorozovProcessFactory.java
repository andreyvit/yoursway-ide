package com.yoursway.rails.models.launch;

import java.util.Map;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.IProcessFactory;
import org.eclipse.debug.core.model.IProcess;

public class PublicMorozovProcessFactory implements IProcessFactory {
    
    public static final String ID = "com.yoursway.ide.publicMorozovRuntimeProcessFactory";
    
    public IProcess newProcess(ILaunch launch, Process process, String label, Map attributes) {
        return new PublicMorozovProcess(launch, process, label, attributes);
    }
    
}
