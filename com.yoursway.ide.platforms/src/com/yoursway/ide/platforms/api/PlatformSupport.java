package com.yoursway.ide.platforms.api;

import java.io.File;

public interface PlatformSupport {
    
    File defaultProjectsLocation();
    
    LastWindowCloseBehavior conventionalLastWindowCloseBehavior();
    
}
