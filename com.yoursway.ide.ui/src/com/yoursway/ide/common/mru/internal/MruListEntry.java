package com.yoursway.ide.common.mru.internal;

import java.io.Serializable;
import java.util.Date;

public class MruListEntry implements Comparable<MruListEntry>, Serializable {
    
    private Date lastUse;
    
    public Date getLastUse() {
        return lastUse;
    }
    
    public void setLastUse(Date lastUse) {
        this.lastUse = lastUse;
    }
    
    public int compareTo(MruListEntry o) {
        return -(lastUse.compareTo(o.lastUse));
    }
    
}
