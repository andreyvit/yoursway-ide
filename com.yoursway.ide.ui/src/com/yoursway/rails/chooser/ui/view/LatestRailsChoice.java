package com.yoursway.rails.chooser.ui.view;

public class LatestRailsChoice implements IChoice {
    
    private final IRailsDescription rails;
    
    public LatestRailsChoice(IRailsDescription rails) {
        this.rails = rails;
    }
    
    public IRailsDescription getRails() {
        return rails;
    }
    
    public String getOkButtonLabel() {
        return "Choose latest";
    }
    
}
