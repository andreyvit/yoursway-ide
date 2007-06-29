package com.yoursway.utils;

import java.io.File;

import com.yoursway.common.rcp.name.MultiChoiceName;
import com.yoursway.common.utilities.Activator;

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
    
}
