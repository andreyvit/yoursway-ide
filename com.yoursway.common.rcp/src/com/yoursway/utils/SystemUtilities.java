package com.yoursway.utils;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.Assert;

import com.yoursway.common.rcp.internal.Activator;
import com.yoursway.common.rcp.name.MultiChoiceName;

public abstract class SystemUtilities {
    
    private static class SystemUtilitiesNotAvailableException extends RuntimeException {
        
        private static final long serialVersionUID = 1L;
        
        public SystemUtilitiesNotAvailableException(Throwable cause) {
            super(cause);
        }
        
    }
    
    private static class InstanceHolder {
        
        public static SystemUtilities INSTANCE = createInstance();
        
        private static SystemUtilities createInstance() {
            try {
                Class<?> implClass = Activator.getDefault().getBundle().loadClass(
                        "com.yoursway.utils.SystemUtilitiesImpl");
                Object implInstance = implClass.newInstance();
                return (SystemUtilities) implInstance;
            } catch (ClassNotFoundException e) {
                throw new SystemUtilitiesNotAvailableException(e);
            } catch (InstantiationException e) {
                throw new SystemUtilitiesNotAvailableException(e);
            } catch (IllegalAccessException e) {
                throw new SystemUtilitiesNotAvailableException(e);
            } catch (ClassCastException e) {
                throw new SystemUtilitiesNotAvailableException(e);
            }
        }
        
    }
    
    public static SystemUtilities getInstance() {
        return InstanceHolder.INSTANCE;
    }
    
    public abstract File getDocumentsStorageLocation();
    
    public abstract File getRCPWorkspaceStorageLocation(MultiChoiceName rcpName);
    
    public static File getFileSystemPathFromLocalURL(URL url) {
        Assert.isTrue("file".equals(url.getProtocol()));
        return getInstance().doGetFileSystemPathFromLocalURL(url);
    }
    
    public File getProgramFilesLocation() {
        throw new UnsupportedOperationException("Does not make sense for this platform");
    }
    
    public boolean isOkayToTreatAsRuby(File file) {
        return true;
    }
    
    protected File doGetFileSystemPathFromLocalURL(URL url) {
        return new File(url.getPath());
    }
}
