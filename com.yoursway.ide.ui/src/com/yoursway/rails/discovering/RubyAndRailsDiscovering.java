package com.yoursway.rails.discovering;

import java.util.Collection;

import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.ruby.core.RubyNature;

import com.yoursway.ruby.IRubyDiscoveryListener;
import com.yoursway.ruby.IRubyInstancesListener;
import com.yoursway.ruby.RubyInstance;
import com.yoursway.ruby.RubyInstanceCollection;

public class RubyAndRailsDiscovering implements IRubyInstancesListener {
    
    private static final RubyAndRailsDiscovering INSTANCE = new RubyAndRailsDiscovering();
    
    public static RubyAndRailsDiscovering getInstance() {
        return INSTANCE;
    }
    
    public static void initialize() {
        // triggers loading of this class, but otherwise does nothing
    }
    
    public RubyAndRailsDiscovering() {
        RubyInstanceCollection.instance().addListener(this);
    }
    
    /**
     * Starts searching Rails instances in all available Ruby instances.
     */
    public static void runSearchRails() {
        IInterpreterInstallType rubyInstallType = getRubyInstallType();
        
        for (IInterpreterInstall rubyInterpreter : rubyInstallType.getInterpreterInstalls())
            runSearchRails(rubyInterpreter);
    }
    
    public static IInterpreterInstallType getRubyInstallType() {
        RubyInstanceCollection.instance().discover(new IRubyDiscoveryListener() {
            
            public void railsInstancesUnchanged(Collection<RubyInstance> instances) {
                for (RubyInstance instance : instances) {
                    runSearchRails(instance.getRawDLTKInterpreterInstall());
                }
            }
            
        });
        IInterpreterInstallType[] rubyInterpreters = ScriptRuntime
                .getInterpreterInstallTypes(RubyNature.NATURE_ID);
        assert rubyInterpreters.length == 1 : "Only one IInterpreterInstallType is expected for Ruby nature";
        IInterpreterInstallType rubyInstallType = rubyInterpreters[0];
        return rubyInstallType;
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
    
    public void rubyInstanceAdded(RubyInstance rubyInstance) {
        runSearchRails(rubyInstance.getRawDLTKInterpreterInstall());
    }
    
    public void rubyInstanceRemoved(RubyInstance rubyInstance) {
    }
    
}
