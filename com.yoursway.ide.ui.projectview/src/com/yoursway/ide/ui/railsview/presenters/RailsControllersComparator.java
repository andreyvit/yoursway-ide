/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import java.util.Comparator;

import com.yoursway.model.rails.impl.RailsController;

public final class RailsControllersComparator implements Comparator<RailsController> {
    public int compare(RailsController o1, RailsController o2) {
        return o1.getDisplayName().compareTo(o2.getDisplayName());
    }
}