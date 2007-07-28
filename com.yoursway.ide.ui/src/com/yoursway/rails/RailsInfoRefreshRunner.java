package com.yoursway.rails;

import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.ruby.core.RubyNature;

import com.yoursway.rails.discovering.RubyAndRailsDiscovering;
import com.yoursway.ruby.RubyInstance;
import com.yoursway.ruby.RubyInstanceCollectionChangedListener;
import com.yoursway.ruby.internal.RubyInstanceCollection;
import com.yoursway.ruby.internal.RubyInstanceImpl;

/**
 * Object of this class listens for the changes in installed RREs and runs Rails
 * search on modified ones.
 */
public class RailsInfoRefreshRunner implements RubyInstanceCollectionChangedListener {
    
    /**
     * FIXME: make singleton?
     * 
     * Starts listening to the 'Ruby instances changed' events in
     * RubyInstanceCollection.
     */
    public static void startListening() {
        new RailsInfoRefreshRunner();
    }
    
    /**
     * {@link RailsInfoRefreshRunner} is not intended to be instantiated, except
     * by the {@link #startListening()}.
     */
    private RailsInfoRefreshRunner() {
        RubyInstanceCollection.instance().addRubyInstanceCollectionChangedListener(this);
    }
    
    // FIXME: move to proper place
    /**
     * Checks whether the given {@link RubyInstanceImpl} denotes Ruby interpreter.
     * 
     * @param interpreter
     *            interpreter to check
     * @return true if given interpreter is Ruby interpreter, false otherwise.
     */
    private static boolean isRubyInterpreter(IInterpreterInstall interpreter) {
        String natureId = interpreter.getInterpreterInstallType().getNatureId();
        // XXX dirty hack: indexOf("Temp")
        return natureId != null && natureId.equals(RubyNature.NATURE_ID)
                && interpreter.getId().indexOf("Temp") < 0;
    }
    
    public void rubyInstanceAdded(RubyInstance ruby) {
        RubyAndRailsDiscovering.runSearchRails(ruby);
    }
    
    public void rubyInstanceRemoved(RubyInstance ruby) {
        RailsInstancesManager.removeRails(ruby);
    }
}
