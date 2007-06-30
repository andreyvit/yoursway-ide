package com.yoursway.rails.chooser.ui.view;

public class SpecificRailsChoice implements IChoice {
    
    private final IRailsDescription rails;
    
    public SpecificRailsChoice(IRailsDescription rails) {
        this.rails = rails;
    }
    
    public IRailsDescription getRails() {
        return rails;
    }
    
    public String getOkButtonLabel() {
        return "Choose " + rails.getVersion();
    }
    
}
