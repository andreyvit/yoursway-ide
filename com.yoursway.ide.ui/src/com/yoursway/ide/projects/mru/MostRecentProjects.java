package com.yoursway.ide.projects.mru;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.yoursway.ide.common.mru.MostRecentlyUsedList;
import com.yoursway.ide.common.mru.internal.MruListEntry;

public class MostRecentProjects {
    
    private static class Entry extends MruListEntry {
        
        private String location;
        
        public Entry(String location) {
            this.location = location;
        }
        
        public String getLocation() {
            return location;
        }
        
        public void setLocation(String location) {
            this.location = location;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((location == null) ? 0 : location.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Entry other = (Entry) obj;
            if (location == null) {
                if (other.location != null)
                    return false;
            } else if (!location.equals(other.location))
                return false;
            return true;
        }
        
    }
    
    // subclass to make it use the correct class loader
    private final MostRecentlyUsedList<Entry> mruList = new MostRecentlyUsedList<Entry>("projects") {
        
    };
    
    private final static MostRecentProjects INSTANCE = new MostRecentProjects();
    
    public static MostRecentProjects instance() {
        return INSTANCE;
    }
    
    public List<File> getRecentLocations(int limit) {
        List<File> result = new ArrayList<File>();
        for (Entry entry : mruList.getEntries()) {
            if (result.size() > limit)
                break;
            result.add(new File(entry.getLocation()));
        }
        return result;
    }
    
    public void locationUsed(File location) {
        mruList.entryUsed(new Entry(location.getAbsolutePath()));
    }
    
}
