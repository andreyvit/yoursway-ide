package com.yoursway.rails.fucking.shit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.yoursway.rails.Rails;
import com.yoursway.rails.RailsRuntime;

public class RailsProvider {
    
    /**
     * Returns the global "best" version of Rails. New projects and projects
     * without configured specific Rails version should use the returned one.
     * 
     * <p>
     * If there are several equal candidates, currenty chooses a random one to
     * facilate testing.
     * </p>
     */
    public static Rails getCurrentRailsInterpreter() {
        Rails bestRails = null;
        for (Rails rails : RailsRuntime.getRails())
            if (bestRails == null || bestRails.getVersion().compareTo(rails.getVersion()) < 0)
                bestRails = rails;
        if (bestRails == null)
            return null;
        
        // choose random Rails
        List<Rails> candidates = new ArrayList<Rails>();
        for (Rails rails : RailsRuntime.getRails())
            if (bestRails.getVersion().equals(rails.getVersion()))
                candidates.add(rails);
        Collections.shuffle(candidates);
        return candidates.iterator().next();
    }
    
}
