package com.yoursway.ruby.model;

public class RubyModelFailure extends RuntimeException {
    
    private static final long serialVersionUID = 7455092480612344008L;
    
    public RubyModelFailure(Exception cause) {
        super(cause);
    }
}
