package com.yoursway.ruby.internal;

import java.io.File;

public interface IPossibleRubyLocationsRequestor {
    
    void accept(File executableLocation);
    
}
