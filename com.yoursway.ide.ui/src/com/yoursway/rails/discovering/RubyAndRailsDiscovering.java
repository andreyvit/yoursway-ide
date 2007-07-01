package com.yoursway.rails.discovering;

import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.ruby.core.RubyNature;

public class RubyAndRailsDiscovering {
    
    private static final RubyAndRailsDiscovering INSTANCE = new RubyAndRailsDiscovering();
    
    public static RubyAndRailsDiscovering getInstance() {
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
        DiscoverRailsInstancesInsideGivenRubyJob job = new DiscoverRailsInstancesInsideGivenRubyJob(
                rubyInterpreter);
        job.schedule();
    }
    
}
