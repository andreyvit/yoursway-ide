package com.yoursway.rails.chooser.ui.view;

import java.util.List;

import com.yoursway.rails.chooser.ui.controller.RailsDescription;

public interface IRailsChooserParameters {
    
    IRailsDescription getLatestRails();
    
    List<RailsDescription> getSpecificRailsVersions();
    
    String getRailsVersionToInstall();
    
    RecommendedChoice getRecommendedChoice();
    
}