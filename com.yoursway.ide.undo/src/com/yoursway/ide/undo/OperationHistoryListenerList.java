package com.yoursway.ide.undo;

import java.util.LinkedList;
import java.util.List;

public class OperationHistoryListenerList implements IOperationHistoryListener {
    
    private final List<IOperationHistoryListener> listeners = new LinkedList<IOperationHistoryListener>();
    
    public void addListener(IOperationHistoryListener listener) {
        listeners.add(listener);
    }
    
    public void add(IUndoableOperation operation) {
        for (IOperationHistoryListener each : listeners) {
            each.add(operation);
        }
    }
    
    public void remove(IUndoableOperation operation) {
        for (IOperationHistoryListener each : listeners) {
            each.remove(operation);
        }
    }
    
}
