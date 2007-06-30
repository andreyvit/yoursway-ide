package com.yoursway.rails.chooser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.yoursway.rails.RailsInstance;
import com.yoursway.rails.RailsInstancesManager;

public class RailsRecommender {
    
    public RailsInstance chooseBestRailsInstanceVersion(List<RailsInstance> allRailsInstance) {
        RailsInstance bestRails = null;
        for (RailsInstance railsInstance : allRailsInstance)
            if (bestRails == null || bestRails.getVersion().compareTo(railsInstance.getVersion()) < 0)
                bestRails = railsInstance;
        if (bestRails == null)
            return null;
        
        // choose random Rails
        List<RailsInstance> candidates = new ArrayList<RailsInstance>();
        for (RailsInstance railsInstance : RailsInstancesManager.getRailsInstance())
            if (bestRails.getVersion().equals(railsInstance.getVersion()))
                candidates.add(railsInstance);
        Collections.shuffle(candidates);
        return candidates.iterator().next();
    }
    
}
