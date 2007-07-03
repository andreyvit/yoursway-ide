package com.yoursway.ruby;

import java.util.Collection;

public interface IRubyDiscoveryListener {
    
    void railsInstancesUnchanged(Collection<RubyInstance> instances);
    
}
