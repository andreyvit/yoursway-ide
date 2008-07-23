package com.yoursway.ide.worksheet.executors.standard;

import java.util.LinkedList;
import java.util.List;

import com.yoursway.ide.worksheet.executors.OutputListener;
import com.yoursway.ide.worksheet.executors.TerminationListener;
import com.yoursway.ide.worksheet.executors.WorksheetCommandExecutor;
import com.yoursway.ide.worksheet.internal.misc.OutputEntry;
import com.yoursway.utils.bugs.Bugs;
import com.yoursway.utils.bugs.Severity;

public abstract class AbstractWorksheetCommandExecutor implements WorksheetCommandExecutor {
    
    private final List<TerminationListener> terminationListeners = new LinkedList<TerminationListener>();
    private final List<OutputListener> outputListeners = new LinkedList<OutputListener>();
    private final List<OutputEntry> output = new LinkedList<OutputEntry>();
    
    public void addTerminationListener(TerminationListener listener) {
        terminationListeners.add(listener);
    }
    
    protected void terminated() {
        for (TerminationListener listener : terminationListeners) {
            try {
                listener.terminated();
            } catch (Throwable e) {
                Bugs.listenerFailed(e, listener);
            }
        }
    }
    
    public synchronized void addOutputListener(OutputListener listener) {
        //? refactor
        try {
            for (OutputEntry e : output) {
                listener.outputted(e.text(), e.error());
            }
        } catch (Throwable e) {
            Bugs.listenerFailed(e, listener);
        }
        
        outputListeners.add(listener);
    }
    
    protected void output(String text) {
        output(text, false);
    }
    
    protected synchronized void output(String text, boolean error) {
        output.add(new OutputEntry(text, error));
        
        for (OutputListener listener : outputListeners) {
            try {
                listener.outputted(text, error);
            } catch (Throwable e) {
                Bugs.listenerFailed(e, listener);
            }
        }
    }
    
    protected void completed() {
        for (OutputListener listener : outputListeners) {
            try {
                listener.completed();
            } catch (Throwable e) {
                Bugs.listenerFailed(e, listener);
            }
        }
    }
    
    protected OutputListener outputter() {
        return new OutputListener() {
            public void outputted(String text, boolean error) {
                output(text, error);
            }
            
            public void completed() {
                Bugs.illegalCaseRecovery(Severity.UNKNOWN, "The outputter.completed() method invoked.");
            }
        };
    }
    
}