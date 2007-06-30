package com.yoursway.rails.search;

import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.ruby.core.RubyNature;

import com.yoursway.utils.TypedListenerList;

public class RailsSearching {
    
    public static final Object NEEDS_RAILS_FAMILY = new Object();
    
    private static final RailsSearching INSTANCE = new RailsSearching();
    
    private final TypedListenerList<IRailsSearchingListener> listeners = new TypedListenerList<IRailsSearchingListener>() {
        
        @Override
        protected IRailsSearchingListener[] makeArray(int size) {
            return new IRailsSearchingListener[size];
        }
        
    };
    
    public void addListener(IRailsSearchingListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(IRailsSearchingListener listener) {
        listeners.remove(listener);
    }
    
    public static RailsSearching getInstance() {
        return INSTANCE;
    }
    
    public static void initialize() {
        // triggers loading of this class, but otherwise does nothing
    }
    
    /**
     * Starts searching Rails instances in all available Ruby instances.
     */
    public static void runSearchRails() {
        IInterpreterInstallType[] rubyInterpreters = ScriptRuntime
                .getInterpreterInstallTypes(RubyNature.NATURE_ID);
        assert rubyInterpreters.length == 1 : "Only one IInterpreterInstallType is expected for Ruby nature";
        
        for (IInterpreterInstall rubyInterpreter : rubyInterpreters[0].getInterpreterInstalls())
            runSearchRails(rubyInterpreter);
    }
    
    /**
     * Starts searching Rails instances in the background.
     * 
     * @param rubyInterpreter
     *            Ruby interpreter where Rails instances will be searched in.
     */
    public static void runSearchRails(IInterpreterInstall rubyInterpreter) {
        RailsSearchJob job = new RailsSearchJob(rubyInterpreter);
        job.schedule();
    }
    
}
