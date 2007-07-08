package com.yoursway.ide.ui;

import java.lang.reflect.Field;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.WorkbenchException;
import org.osgi.framework.Bundle;

import com.yoursway.ide.projects.YourSwayProjects;
import com.yoursway.rails.RailsInfoRefreshRunner;
import com.yoursway.rails.discovering.RubyAndRailsDiscovering;

public class YourSwayIDE {
    
    public static void finishLoading() {
        try {
            YourSwayProjects.makeSureAllWindowsExist();
        } catch (WorkbenchException e) {
            Activator.unexpectedError(e);
        }
        
        RubyAndRailsDiscovering.initialize();
        RailsInfoRefreshRunner.startListening();
    }
    
    public static void beforeLoading() {
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        try {
            Object masterToken = hijackSecurityToObtainMasterToken(extensionRegistry);
            powerDiet(new Herbalife(extensionRegistry, masterToken));
        } catch (CannotReadMasterToken e) {
            Activator.unexpectedError(e);
        }
    }
    
    private static void powerDiet(Herbalife herbalife) {
        // get rid of the Search menu
        herbalife.starveActionSet("org.eclipse.search.searchActionSet");
    }
    
    private static final class Herbalife {
        
        private final IExtensionRegistry extensionRegistry;
        private final Object masterToken;
        
        public Herbalife(IExtensionRegistry extensionRegistry, Object masterToken) {
            this.extensionRegistry = extensionRegistry;
            this.masterToken = masterToken;
        }
        
        public void starveActionSet(String offendingId) {
            IExtensionPoint actionSets = extensionRegistry.getExtensionPoint("org.eclipse.ui.actionSets");
            for (IExtension extension : actionSets.getExtensions()) {
                boolean isOffendingOne = false;
                for (IConfigurationElement element : extension.getConfigurationElements()) {
                    String id = element.getAttribute("id");
                    if (offendingId.equals(id)) {
                        isOffendingOne = true;
                        break;
                    }
                }
                if (isOffendingOne) {
                    extensionRegistry.removeExtension(extension, masterToken);
                    break;
                }
            }
        }
        
    }
    
    private static final class CannotReadMasterToken extends Exception {
        
        private static final long serialVersionUID = 1L;
        
        public CannotReadMasterToken(Throwable cause) {
            super(cause);
        }
        
    }
    
    private static Object hijackSecurityToObtainMasterToken(IExtensionRegistry extensionRegistry)
            throws CannotReadMasterToken {
        try {
            Bundle osgiRegistryBundle = Platform.getBundle("org.eclipse.equinox.registry");
            Class<?> keyHolder = osgiRegistryBundle
                    .loadClass("org.eclipse.core.internal.registry.ExtensionRegistry");
            Field keyField = keyHolder.getDeclaredField("masterToken");
            keyField.setAccessible(true);
            return keyField.get(extensionRegistry);
        } catch (ClassNotFoundException e) {
            throw new CannotReadMasterToken(e);
        } catch (NoSuchFieldException e) {
            throw new CannotReadMasterToken(e);
        } catch (IllegalAccessException e) {
            throw new CannotReadMasterToken(e);
        }
    }
    
}
