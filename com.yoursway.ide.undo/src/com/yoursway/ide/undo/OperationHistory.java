package com.yoursway.ide.undo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.XMLMemento;

public class OperationHistory implements IPersistableElement {
    
    private final List<IUndoableOperation> operations = Collections
            .synchronizedList(new ArrayList<IUndoableOperation>());
    
    private final OperationHistoryListenerList listeners = new OperationHistoryListenerList();
    
    private boolean locked;
    
    private static OperationHistory instance;
    
    private OperationHistory() {
        
    }
    
   
    public static OperationHistory get() {
        if (instance == null) {
            instance = new OperationHistory();            
        }
        return instance;
    }
    
    public void execute(IUndoableOperation operation) {
        operation.execute();
        
        if (!locked) {
            if (operations.size() > 0) {
                IUndoableOperation last = operations.get(operations.size() - 1);
                boolean merged = operation.tryToMergeWith(last);
                if (merged) {
                    operations.remove(last);
                    listeners.remove(last);                
                }
            }
            
            operations.add(operation);
            listeners.add(operation);
        }
    }
    
    public IUndoableOperation[] getUndoHistory() {
        return operations.toArray(new IUndoableOperation[0]);
    }
    
    public void undo(IUndoableOperation operation) {
        locked = true;
        
        operation.undo();
        operations.remove(operation);
        listeners.remove(operation);
        
        locked = false;
    }
    
    public void undoLastOperation() {
        IUndoableOperation lastOperation = operations.get(operations.size() - 1);
        undo(lastOperation);
    }
    
    public void addListener(IOperationHistoryListener listener) {
        listeners.addListener(listener);
    }

    public String getFactoryId() {
        // TODO Auto-generated method stub
        return null;
    }

    public void saveState(IMemento memento) {
        for (IUndoableOperation operation : operations) {
            memento.putMemento(MyMemento.forPersistableElement(operation));
        }        
    }

    public static void init(IMemento memento) {
        OperationHistory history = get();
        
        IMemento[] children = memento.getChildren("UndoableTextChange");
        for (IMemento child : children) {
            history.operations.add(UndoableTextChange.from(child));            
        }
        
    }
    
}
