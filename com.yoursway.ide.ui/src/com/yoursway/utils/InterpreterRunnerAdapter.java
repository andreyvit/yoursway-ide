package com.yoursway.utils;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * This class adapts IInterpreterRunner interface to the standard
 * IRunnableWithProgress interface.
 * 
 * (Yes, in the language with currying this adapter would be unnecessary)
 */
public class InterpreterRunnerAdapter implements IRunnableWithProgress {
    
    private final IInterpreterRunner runner;
    private final InterpreterConfig config;
    private final ILaunch launch;
    
    InterpreterRunnerAdapter(IInterpreterRunner runner, InterpreterConfig config, ILaunch launch) {
        this.runner = runner;
        this.config = config;
        this.launch = launch;
    }
    
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        try {
            runner.run(config, launch, monitor);
        } catch (CoreException e) {
            throw new InvocationTargetException(e);
        }
    }
}
