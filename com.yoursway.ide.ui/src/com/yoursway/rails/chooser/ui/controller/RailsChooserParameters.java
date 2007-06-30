package com.yoursway.rails.chooser.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.yoursway.rails.chooser.ui.view.IRailsChooserParameters;
import com.yoursway.rails.chooser.ui.view.IRailsDescription;
import com.yoursway.rails.chooser.ui.view.RecommendedChoice;

public class RailsChooserParameters implements IRailsChooserParameters {
    
    private IRailsDescription latestRails;
    
    private final List<RailsDescription> specificRailsVersions = new ArrayList<RailsDescription>();
    
    private String railsVersionToInstall;
    
    private RecommendedChoice recommendedChoice;
    
    public IRailsDescription getLatestRails() {
        return latestRails;
    }
    
    public void setLatestRails(IRailsDescription latestRails) {
        this.latestRails = latestRails;
    }
    
    public List<RailsDescription> getSpecificRailsVersions() {
        return specificRailsVersions;
    }
    
    public String getRailsVersionToInstall() {
        return railsVersionToInstall;
    }
    
    public void setRailsVersionToInstall(String railsVersionToInstall) {
        this.railsVersionToInstall = railsVersionToInstall;
    }
    
    public RecommendedChoice getRecommendedChoice() {
        return recommendedChoice;
    }
    
    public void setRecommendedChoice(RecommendedChoice recommendedChoice) {
        this.recommendedChoice = recommendedChoice;
    }
    
}
