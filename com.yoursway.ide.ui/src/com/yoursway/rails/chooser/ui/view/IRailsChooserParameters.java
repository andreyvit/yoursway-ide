package com.yoursway.rails.chooser.ui.view;

import java.util.List;

public interface IRailsChooserParameters {
    
    IRailsDescription getLatestRails();
    
    List<? extends IRailsDescription> getSpecificRailsVersions();
    
    String getRailsVersionToInstall();
    
    RecommendedChoice getRecommendedChoice();
    
    public IChoice getInitialChoice();
    
}