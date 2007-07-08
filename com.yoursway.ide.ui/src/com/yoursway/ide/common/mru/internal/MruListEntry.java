package com.yoursway.ide.common.mru.internal;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class MruListEntry implements Comparable<MruListEntry>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public MruListEntry() {
        setLastUseToNow();
    }
    
    private Date lastUse;
    
    public Date getLastUse() {
        return lastUse;
    }
    
    public int compareTo(MruListEntry o) {
        return -(lastUse.compareTo(o.lastUse));
    }
    
    public void setLastUseToNow() {
        lastUse = Calendar.getInstance().getTime();
    }
    
}
