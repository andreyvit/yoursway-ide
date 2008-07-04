package com.yoursway.ide.undo;

public interface IOperationHistoryListener {
    
    void add(IUndoableOperation operation);
    
    void remove(IUndoableOperation operation);
    
}
