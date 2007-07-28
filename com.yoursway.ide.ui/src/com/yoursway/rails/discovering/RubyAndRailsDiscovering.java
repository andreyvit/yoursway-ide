package com.yoursway.rails.discovering;

import com.yoursway.ruby.RubyInstance;
import com.yoursway.ruby.internal.RubyInstanceCollection;

public class RubyAndRailsDiscovering {
    
    private static final RubyAndRailsDiscovering INSTANCE = new RubyAndRailsDiscovering();
    
    public static RubyAndRailsDiscovering getInstance() {
        return INSTANCE;
    }
    
    public static void initialize() {
        // ??
        // triggers loading of this class, but otherwise does nothing
    }
    
    public RubyAndRailsDiscovering() {
    }
    
    /**
     * Starts searching Rails instances in all available Ruby instances.
     */
    public static void runSearchRails() {
        for (RubyInstance ruby : RubyInstanceCollection.instance().getAll())
            runSearchRails(ruby);
    }
    
    /**
     * Starts searching Rails instances in the background.
     * 
     * @param ruby
     *            Ruby interpreter where Rails instances will be searched in.
     */
    public static void runSearchRails(RubyInstance ruby) {
        DiscoverRailsInstancesInsideGivenRubyJob job = new DiscoverRailsInstancesInsideGivenRubyJob(ruby);
        job.schedule();
    }
}
