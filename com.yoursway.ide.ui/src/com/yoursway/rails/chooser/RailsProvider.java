package com.yoursway.rails.chooser;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.NLS;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.RailsInstance;

public class RailsProvider {
    
    private static final RailsProvider INSTANCE = new RailsProvider();
    
    private boolean isChoosingRails = false;
    
    private RailsInstance chosenRailsInstance;
    
    private static final String CHOOSER_EP = "com.yoursway.rails.chooser";
    
    private static final String CHOOSER_ELEMENT = "chooser";
    
    private static final String CHOOSER_CLASS_ATTR = "class";
    
    private IRailsChooser railsChooser;
    
    public static RailsProvider getInstance() {
        return INSTANCE;
    }
    
    /**
     * @return the version of Rails chosen by the user, or <code>null</code>
     *         if none has been chosen.
     */
    public synchronized RailsInstance getChosenRailsInstanceInterpreter() {
        if (chosenRailsInstance == null)
            showRailsChooser();
        while (isChoosingRails)
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw (AssertionFailedException) new AssertionFailedException("Cannot happen").initCause(e);
            }
        return chosenRailsInstance;
    }
    
    public synchronized void showRailsChooser() {
        if (isChoosingRails)
            return;
        isChoosingRails = true;
        new Thread() {
            @Override
            public void run() {
                try {
                    chosenRailsInstance = getRailsChooser().choose(chosenRailsInstance);
                } finally {
                    synchronized (RailsProvider.this) {
                        isChoosingRails = false;
                        RailsProvider.this.notifyAll();
                    }
                }
            }
        }.start();
    }
    
    private IRailsChooser getRailsChooser() {
        if (railsChooser == null)
            railsChooser = createRailsChooser();
        return railsChooser;
    }
    
    private IRailsChooser createRailsChooser() {
        for (IConfigurationElement element : Platform.getExtensionRegistry().getExtensionPoint(CHOOSER_EP)
                .getConfigurationElements()) {
            if (CHOOSER_ELEMENT.equals(element.getName())) {
                try {
                    return (IRailsChooser) element.createExecutableExtension(CHOOSER_CLASS_ATTR);
                } catch (CoreException e) {
                    Activator.unexpectedError(e);
                }
            } else {
                Activator.unexpectedError(NLS.bind("Invalid EP for {0}: unknown element {1}", CHOOSER_EP,
                        element.getName()));
            }
        }
        throw new AssertionError("Rails Chooser must exist");
    }
    
}
