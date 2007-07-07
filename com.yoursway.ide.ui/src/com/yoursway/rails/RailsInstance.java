package com.yoursway.rails;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.launching.IInterpreterInstall;

import com.yoursway.ruby.RubyInstance;
import com.yoursway.rubygems.IGem;

public class RailsInstance {
    private final Version version;
    private final RubyInstance ruby;
    private final Set<String> paths;
    private final IGem[] gems;
    private IGem railsGem;
    
    public RailsInstance(RubyInstance ruby, String version, IGem[] gems) {
        Assert.isNotNull(ruby);
        Assert.isNotNull(version);
        Assert.isNotNull(gems);
        
        this.gems = gems;
        this.version = Version.fromDotDelimitedString(version);
        this.ruby = ruby;
        
        paths = new HashSet<String>();
        for (IGem gem : gems)
            for (String requirePath : gem.getRequirePaths()) {
                Path p = new Path(gem.getDirectory());
                p.append(requirePath);
                paths.add(p.toOSString());
            }
        for (IGem gem : this.gems)
            if (gem.getName().equals("rails"))
                railsGem = gem;
    }
    
    public IGem getRailsGem() {
        return railsGem;
    }
    
    public IGem[] getGems() {
        return gems;
    }
    
    public String getVersionAsString() {
        return version.asDotDelimitedString();
    }
    
    public Version getVersion() {
        return version;
    }
    
    public Set<String> getPaths() {
        return paths;
    }
    
    public IInterpreterInstall getRawRuby() {
        return ruby.getRawDLTKInterpreterInstall();
    }
    
    public RubyInstance getRuby() {
        return ruby;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ruby == null) ? 0 : ruby.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        final RailsInstance other = (RailsInstance) obj;
        if (ruby == null) {
            if (other.ruby != null)
                return false;
        } else if (!ruby.equals(other.ruby))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }
    
}
