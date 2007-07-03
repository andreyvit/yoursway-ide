package com.yoursway.rails.chooser.ui.view;

import java.util.List;

import com.yoursway.rails.chooser.ui.controller.RubyDescription;

public interface IRailsChooserParameters {
    
    IRailsDescription getLatestRails();
    
    List<? extends IRailsDescription> getSpecificRailsVersions();
    
    String getRailsVersionToInstall();
    
    RecommendedChoice getRecommendedChoice();
    
    IChoice getInitialChoice();
    
    List<RubyDescription> getRubyInstancesToInstallInto();
    
    boolean isLatestRailsAvailable();
    
    boolean isSpecificRailsAvailable();
    
    boolean isInstallAvailable();
    
}