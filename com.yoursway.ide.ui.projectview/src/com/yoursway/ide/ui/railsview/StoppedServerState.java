package com.yoursway.ide.ui.railsview;

public class StoppedServerState extends RailsServerState {
    @Override
    public void visit(IRailsServerStateVisitor visitor) {
        visitor.visit(this);
    }
}
