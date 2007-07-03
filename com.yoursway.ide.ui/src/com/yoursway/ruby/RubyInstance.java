package com.yoursway.ruby;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.launching.IInterpreterInstall;

import com.yoursway.rails.Version;

public class RubyInstance {
    
    private final File installLocation;
    private final Version version;
    private final RubyInstallWrapper installWrapper;
    
    RubyInstance(File location, Version version, RubyInstallWrapper installWrapper) {
        Assert.isNotNull(location);
        Assert.isNotNull(version);
        Assert.isNotNull(installWrapper);
        
        this.installLocation = location;
        this.version = version;
        this.installWrapper = installWrapper;
    }
    
    public String getVersionAsString() {
        return version.asDotDelimitedString();
    }
    
    public Version getVersion() {
        return version;
    }
    
    public File getLocation() {
        return installWrapper.getLocation();
    }
    
    /**
     * @param fileName
     * @param arguments
     * @param monitor
     *            the progress monitor to use for reporting progress to the
     *            user. It is the caller's responsibility to call done() on the
     *            given monitor. Accepts <code>null</code>, indicating that
     *            no progress should be reported and that the operation cannot
     *            be cancelled.
     * @return
     * @throws RubyScriptInvokationError
     */
    public ToolExecutionResult runRubyScript(String fileName, List<String> arguments, IProgressMonitor monitor)
            throws RubyScriptInvokationError {
        return installWrapper.runRubyScript(fileName, arguments, monitor);
    }
    
    public IInterpreterInstall getRawDLTKInterpreterInstall() {
        return installWrapper.getRawDLTKInterpreterInstall();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((installLocation == null) ? 0 : installLocation.hashCode());
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
        final RubyInstance other = (RubyInstance) obj;
        if (installLocation == null) {
            if (other.installLocation != null)
                return false;
        } else if (!installLocation.equals(other.installLocation))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }
    
    RubyInstallWrapper getInstallWrapper() {
        return installWrapper;
    }
    
}
