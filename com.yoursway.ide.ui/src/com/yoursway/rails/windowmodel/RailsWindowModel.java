package com.yoursway.rails.windowmodel;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.yoursway.utils.TypedListenerList;

public class RailsWindowModel {
    
    private static final RailsWindowModel INSTANCE = new RailsWindowModel();
    
    private final Map<IWorkbenchWindow, RailsWindow> mapping = new HashMap<IWorkbenchWindow, RailsWindow>();
    
    private final TypedListenerList<IRailsWindowModelListener> listeners = new TypedListenerList<IRailsWindowModelListener>() {
        
        @Override
        protected IRailsWindowModelListener[] makeArray(int size) {
            return new IRailsWindowModelListener[size];
        }
        
    };
    
    public static RailsWindowModel instance() {
        return INSTANCE;
    }
    
    private RailsWindowModel() {
        PlatformUI.getWorkbench().addWindowListener(new IWindowListener() {
            
            public void windowActivated(IWorkbenchWindow window) {
            }
            
            public void windowClosed(IWorkbenchWindow window) {
                mapping.remove(window);
            }
            
            public void windowDeactivated(IWorkbenchWindow window) {
            }
            
            public void windowOpened(IWorkbenchWindow window) {
            }
            
        });
    }
    
    public void addListener(IRailsWindowModelListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(IRailsWindowModelListener listener) {
        listeners.remove(listener);
    }
    
    public RailsWindow getWindow(IWorkbenchWindow window) {
        RailsWindow railsWindow = mapping.get(window);
        if (railsWindow == null) {
            railsWindow = new RailsWindow(this, window);
            mapping.put(window, railsWindow);
        }
        return railsWindow;
    }
    
    void fire(RailsWindowModelChange event) {
        IRailsWindowModelListener[] array = listeners.getListeners();
        for (IRailsWindowModelListener listener : array) {
            event.sendTo(listener);
        }
    }
    
}
