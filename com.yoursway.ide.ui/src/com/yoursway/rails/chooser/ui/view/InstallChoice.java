package com.yoursway.rails.chooser.ui.view;

public class InstallChoice implements IChoice {
    
    private final IRubyDescription ruby;
    
    public InstallChoice(IRubyDescription ruby) {
        this.ruby = ruby;
    }
    
    public IRubyDescription getRuby() {
        return ruby;
    }
    
    public String getOkButtonLabel() {
        return "Install";
    }
    
}
