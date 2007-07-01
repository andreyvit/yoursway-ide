package com.yoursway.rails.launching;

import java.util.Map;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.IProcessFactory;
import org.eclipse.debug.core.model.IProcess;

public class PublicMorozovProcessFactory implements IProcessFactory {
    
    public static final String ID = "com.yoursway.ide.publicMorozovRuntimeProcessFactory";
    
    @SuppressWarnings("unchecked")
    public IProcess newProcess(ILaunch launch, Process process, String label, Map attributes) {
        return new PublicMorozovProcess(launch, process, label, attributes);
    }
    
}
