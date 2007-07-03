package com.yoursway.rails.chooser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.yoursway.rails.RailsInstance;
import com.yoursway.rails.RailsInstancesManager;

public class RailsRecommender {
    
    public RailsInstance chooseBestRailsInstanceVersion(List<RailsInstance> allRailsInstances) {
        RailsInstance bestRails = null;
        for (RailsInstance railsInstance : allRailsInstances)
            if (bestRails == null || bestRails.getVersion().compareTo(railsInstance.getVersion()) < 0)
                bestRails = railsInstance;
        if (bestRails == null)
            return null;
        
        // choose random Rails
        List<RailsInstance> candidates = new ArrayList<RailsInstance>();
        for (RailsInstance railsInstance : RailsInstancesManager.getRailsInstances())
            if (bestRails.getVersion().equals(railsInstance.getVersion()))
                candidates.add(railsInstance);
        Collections.shuffle(candidates);
        return candidates.iterator().next();
    }
    
    public void sortRailsFromBestToWorst(List<RailsInstance> allRailsInstances) {
        Collections.sort(allRailsInstances, new Comparator<RailsInstance>() {
            
            public int compare(RailsInstance o1, RailsInstance o2) {
                int byVersion = o1.getVersion().compareTo(o2.getVersion());
                if (byVersion != 0)
                    return -byVersion;
                return o1.getRuby().getLocation().compareTo(o2.getRuby().getLocation());
            }
            
        });
    }
    
}
