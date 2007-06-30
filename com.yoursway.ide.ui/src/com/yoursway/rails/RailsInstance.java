package com.yoursway.rails;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.launching.IInterpreterInstall;

import com.yoursway.ruby.RubyInstance;
import com.yoursway.rubygems.IGem;

public class RailsInstance {
    private final Version version;
    private final IInterpreterInstall ruby;
    private final Set<String> paths;
    private final IGem[] gems;
    private IGem railsGem;
    
    public RailsInstance(IInterpreterInstall ruby, String version, IGem[] gems) {
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
        return ruby;
    }
    
    public RubyInstance getRuby() {
        return RubyInstance.adapt(ruby);
    }
    
}
