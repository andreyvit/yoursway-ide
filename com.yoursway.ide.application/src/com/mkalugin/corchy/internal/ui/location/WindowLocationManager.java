package com.mkalugin.corchy.internal.ui.location;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.mkalugin.corchy.ui.core.preference.IPreferenceStore;

public class WindowLocationManager {
    
    private static final String KEY_WIDTH = "width";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_Y = "y";
    private static final String KEY_X = "x";
    
    private IPreferenceStore preferenceStore;
    private final Shell shell;
    
    private InitialShellPosition initialPosition;
    private Point defaultSize;
    private ShellPositionConstraint positionConstraint;
    private boolean persistLocation;
    private boolean persistSize;
    private boolean boundsInitialized;
    
    public WindowLocationManager(Shell shell, WindowLocationConfiguration configuration) {
        if (shell == null)
            throw new NullPointerException("shell is null");
        if (configuration == null)
            throw new NullPointerException("configuration is null");
        this.shell = shell;
        this.initialPosition = configuration.getInitialPosition();
        this.defaultSize = configuration.getDefaultSize();
        this.positionConstraint = configuration.getPositionConstraint();
        this.persistLocation = configuration.shouldPersistLocation();
        this.persistSize = configuration.shouldPersistSize();
        Listener saveStateListener = new Listener() {
            public void handleEvent(Event event) {
                saveState();
            }
        };
        shell.addListener(SWT.Resize, saveStateListener);
        shell.addListener(SWT.Move, saveStateListener);
        if (!(persistLocation || persistSize))
            initializeBounds();
    }
    
    public void setDialogSettings(IPreferenceStore preferenceStore) {
        if (preferenceStore == null && (persistLocation || persistSize))
            throw new NullPointerException("dialogSettings is null, but persistance is requestored");
        this.preferenceStore = preferenceStore;
        if (!boundsInitialized)
            initializeBounds();
        else
            saveState();
    }
    
    private void initializeBounds() {
        Point size = loadSize();
        if (size == null)
            size = computeInitialSize(shell);
        Point location = loadLocation();
        if (location == null) {
            location = initialPosition.calculatePosition(shell.getDisplay(), (Shell) shell.getParent(), size);
            if (location == null)
                location = shell.getLocation();
        }
        shell.setBounds(positionConstraint.constrain(new Rectangle(location.x, location.y, size.x, size.y),
                shell.getDisplay()));
        boundsInitialized = true;
    }
    
    private Point computeInitialSize(Shell shell) {
        Point size = shell.computeSize(defaultSize.x, defaultSize.y, true);
        if (defaultSize.x != SWT.DEFAULT)
            size.x = defaultSize.x;
        if (defaultSize.y != SWT.DEFAULT)
            size.y = defaultSize.y;
        return size;
    }
    
    private Point loadSize() {
        if (!persistSize)
            return null;
        try {
            int width = preferenceStore.getInt(KEY_WIDTH);
            int height = preferenceStore.getInt(KEY_HEIGHT);
            if (width < 1 || height < 1)
                return null;
            return new Point(width, height);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private Point loadLocation() {
        if (!persistLocation)
            return null;
        try {
            if (!preferenceStore.contains(KEY_X) || !preferenceStore.contains(KEY_Y))
                return null;
            int x = preferenceStore.getInt(KEY_X);
            int y = preferenceStore.getInt(KEY_Y);
            return new Point(x, y);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private void saveState() {
        Rectangle bounds = shell.getBounds();
        if (persistLocation) {
            preferenceStore.setValue(KEY_X, bounds.x);
            preferenceStore.setValue(KEY_Y, bounds.y);
        }
        if (persistSize) {
            preferenceStore.setValue(KEY_WIDTH, bounds.width);
            preferenceStore.setValue(KEY_HEIGHT, bounds.height);
        }
    }
    
}
