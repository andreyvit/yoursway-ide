package com.yoursway.ide.ui.railsview;

public class RunningServerState extends RailsServerState {
    
    private final String host;
    private final int port;
    
    public RunningServerState(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }
    
    @Override
    public void visit(IRailsServerStateVisitor visitor) {
        visitor.visit(this);
    }
    
    public String getHost() {
        return host;
    }
    
    public int getPort() {
        return port;
    }
    
}
