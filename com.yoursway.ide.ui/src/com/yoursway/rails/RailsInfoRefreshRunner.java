package com.yoursway.rails;

import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallChangedListener;
import org.eclipse.dltk.launching.PropertyChangeEvent;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.ruby.core.RubyNature;

/**
 * Object of this class listens for the changes in installed RREs and runs Rails
 * search on modified ones.
 */
public class RailsInfoRefreshRunner implements IInterpreterInstallChangedListener {
    
    /**
     * Starts the listening to the 'interpreters changed' event in
     * {@link ScriptRuntime}.
     */
    public static void startListening() {
        RailsInfoRefreshRunner searchRunner = new RailsInfoRefreshRunner();
        ScriptRuntime.addInterpreterInstallChangedListener(searchRunner);
    }
    
    /**
     * {@link RailsInfoRefreshRunner} is not intended to be instantiated, except
     * by the {@link #startListening()}.
     */
    private RailsInfoRefreshRunner() {
    }
    
    /**
     * Changing default interpreter install does not affect Rails installations.
     */
    public void defaultInterpreterInstallChanged(IInterpreterInstall previous, IInterpreterInstall current) {
    }
    
    /**
     * Adding new Ruby interpreter triggers the Rails search.
     */
    public void interpreterAdded(IInterpreterInstall interpreter) {
        if (isRubyInterpreter(interpreter)) {
            RailsRuntime.runSearchRails(interpreter);
        }
    }
    
    /**
     * Changing the existing Ruby interpreter triggers the Rails search.
     */
    public void interpreterChanged(PropertyChangeEvent event) {
        IInterpreterInstall interpreter = (IInterpreterInstall) event.getSource();
        if (isRubyInterpreter(interpreter)) {
            RailsRuntime.runSearchRails(interpreter);
        }
    }
    
    /**
     * Removing the existing Ruby interpreter removes associated Rails
     * instances.
     */
    public void interpreterRemoved(IInterpreterInstall interpreter) {
        if (isRubyInterpreter(interpreter)) {
            RailsRuntime.removeRails(interpreter);
        }
    }
    
    /**
     * Checks whether the given {@link IInterpreterInstall} denotes Ruby
     * interpreter.
     * 
     * @param interpreter
     *            interpreter to check
     * @return true if given interpreter is Ruby interpreter, false otherwise.
     */
    private static boolean isRubyInterpreter(IInterpreterInstall interpreter) {
        String natureId = interpreter.getInterpreterInstallType().getNatureId();
        return natureId != null && natureId.equals(RubyNature.NATURE_ID);
    }
}
