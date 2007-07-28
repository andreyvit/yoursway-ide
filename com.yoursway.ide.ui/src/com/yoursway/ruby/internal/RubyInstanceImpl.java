package com.yoursway.ruby.internal;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;

import com.yoursway.rails.Version;
import com.yoursway.ruby.ProcessResult;
import com.yoursway.ruby.RubyInstance;
import com.yoursway.ruby.RubyScriptInvokationError;

public class RubyInstanceImpl implements RubyInstance {
    
    private final File installLocation;
    private final Version version;
    private final RubyInstallWrapper installWrapper;
    
    RubyInstanceImpl(File location, Version version, RubyInstallWrapper installWrapper) {
        Assert.isNotNull(location);
        Assert.isNotNull(version);
        Assert.isNotNull(installWrapper);
        
        this.installLocation = location;
        this.version = version;
        this.installWrapper = installWrapper;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.yoursway.ruby.RubyInstance#getVersion()
     */
    public Version getVersion() {
        return version;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.yoursway.ruby.RubyInstance#getLocation()
     */
    public File getLocation() {
        return installLocation;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.yoursway.ruby.RubyInstance#runRubyScript(java.lang.String,
     *      java.util.List, org.eclipse.core.runtime.IProgressMonitor)
     */
    public ProcessResult runRubyScript(String fileName, List<String> arguments, IProgressMonitor monitor)
            throws RubyScriptInvokationError {
        return getInstallWrapper().runRubyScript(fileName, arguments, monitor);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.yoursway.ruby.RubyInstance#startRubyScript(java.lang.String,
     *      java.util.List)
     */
    public ILaunch startRubyScript(String fileName, List<String> arguments) throws RubyScriptInvokationError {
        return getInstallWrapper().startRubyScript(fileName, arguments);
    }
    
    //    public IInterpreterInstall getRawDLTKInterpreterInstall() {
    //        return installWrapper.getRawDLTKInterpreterInstall();
    //    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + installLocation.hashCode();
        result = prime * result + version.hashCode();
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
        final RubyInstanceImpl other = (RubyInstanceImpl) obj;
        if (!installLocation.equals(other.installLocation))
            return false;
        if (!version.equals(other.version))
            return false;
        return true;
    }
    
    private RubyInstallWrapper getInstallWrapper() {
        return installWrapper;
    }
}
