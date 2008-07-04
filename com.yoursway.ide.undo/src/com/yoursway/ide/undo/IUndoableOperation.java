package com.yoursway.ide.undo;

import org.eclipse.ui.IPersistableElement;

public interface IUndoableOperation extends IPersistableElement {
    
    //? boolean canUndo();
    
    void execute();
    
    void undo();
    
    String getLabel();
    
}
