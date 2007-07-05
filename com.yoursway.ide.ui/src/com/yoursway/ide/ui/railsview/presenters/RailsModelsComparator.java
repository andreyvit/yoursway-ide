/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import java.util.Comparator;

import com.yoursway.rails.core.models.RailsModel;

public final class RailsModelsComparator implements Comparator<RailsModel> {
    public int compare(RailsModel o1, RailsModel o2) {
        return o1.getCombinedClassName().compareTo(o2.getCombinedClassName());
    }
}