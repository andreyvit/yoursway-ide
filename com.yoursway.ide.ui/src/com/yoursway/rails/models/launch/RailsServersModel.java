package com.yoursway.rails.models.launch;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.utils.TypedListenerList;

public class RailsServersModel {
    
    private final TypedListenerList<ILaunchingModelListener> listeners = new TypedListenerList<ILaunchingModelListener>() {
        
        @Override
        protected ILaunchingModelListener[] makeArray(int size) {
            return new ILaunchingModelListener[size];
        }
        
    };
    
    private static final class SingletonHolder {
        
        private static RailsServersModel instance = new RailsServersModel();
        
    }
    
    private final Map<IRailsProject, ProjectLaunching> projectToLaunching = new HashMap<IRailsProject, ProjectLaunching>();
    
    public static RailsServersModel instance() {
        return SingletonHolder.instance;
    }
    
    public synchronized IProjectLaunching get(IRailsProject railsProject) {
        ProjectLaunching result = projectToLaunching.get(railsProject);
        if (result == null) {
            result = new ProjectLaunching(this, railsProject);
            projectToLaunching.put(railsProject, result);
        }
        return result;
    }
    
    public void addListener(ILaunchingModelListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(ILaunchingModelListener listener) {
        listeners.remove(listener);
    }
    
    void fireProjectStateChanged(final IProjectLaunching launching) {
        for (final ILaunchingModelListener listener : listeners.getListeners()) {
            SafeRunner.run(new ISafeRunnable() {
                
                public void handleException(Throwable exception) {
                    Activator.unexpectedError(exception);
                }
                
                public void run() throws Exception {
                    listener.projectStateChanged(launching);
                }
                
            });
        }
    }
    
}
