package com.yoursway.rails;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RailsRuntime {
    
    private static Set<Rails> rails = new HashSet<Rails>();
    
    public synchronized static boolean removeRails(Rails railsInstance) {
        return rails.remove(railsInstance);
    }
    
    public synchronized static void addRails(Rails railsInstance) {
        rails.add(railsInstance);
    }
    
    public synchronized static List<Rails> getRails() {
        ArrayList<Rails> railsList = new ArrayList<Rails>();
        for (Rails r : rails)
            railsList.add(r);
        return railsList;
    }
}
