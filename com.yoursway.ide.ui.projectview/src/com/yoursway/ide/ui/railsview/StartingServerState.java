package com.yoursway.ide.ui.railsview;

public class StartingServerState extends RailsServerState {
    @Override
    public void visit(IRailsServerStateVisitor visitor) {
        visitor.visit(this);
    }
}
