package com.yoursway.ide.platforms.api;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.RegistryFactory;

public class PlatformUtilities {
    
    private static final String EP = "com.yoursway.ide.platforms.platformImplementations";
    
    public static PlatformSupport createPlatformImpl() {
        IConfigurationElement[] elements = RegistryFactory.getRegistry().getConfigurationElementsFor(EP);
        if (elements.length == 0)
            throw new AssertionError("No platform implementations registered");
        if (elements.length > 1)
            throw new AssertionError("Multiple platform implementations registered");
        try {
            return (PlatformSupport) elements[0].createExecutableExtension("class");
        } catch (CoreException e) {
            throw new AssertionError(e);
        }
    }
    
}
