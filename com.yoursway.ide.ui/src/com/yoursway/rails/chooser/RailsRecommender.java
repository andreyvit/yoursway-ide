package com.yoursway.rails.chooser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.yoursway.rails.Rails;
import com.yoursway.rails.RailsRuntime;

public class RailsRecommender {
    
    public Rails chooseBestRailsVersion(List<Rails> allRails) {
        Rails bestRails = null;
        for (Rails rails : allRails)
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
