package com.mkalugin.corchy.internal.ui.location;

import static com.mkalugin.corchy.internal.ui.location.InitialShellPosition.CENTERED;
import static com.mkalugin.corchy.internal.ui.location.ShellPositionConstraint.CONTAINED_WITHIN_SINGLE_MONITOR;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;


public class WindowLocationConfiguration {
    
    private InitialShellPosition initialPosition = CENTERED;
    
    private ShellPositionConstraint positionConstraint = CONTAINED_WITHIN_SINGLE_MONITOR;
    
    private boolean persistSize = true;
    
    private boolean persistLocation = true;
    
    private Point defaultSize = new Point(SWT.DEFAULT, SWT.DEFAULT);
    
    public WindowLocationConfiguration width(int width) {
        defaultSize.x = width;
        return this;
    }
    
    public WindowLocationConfiguration height(int height) {
        defaultSize.y = height;
        return this;
    }
    
    public WindowLocationConfiguration size(int width, int height) {
        defaultSize.x = width;
        defaultSize.y = height;
        return this;
    }
    
    public WindowLocationConfiguration positionConstraint(ShellPositionConstraint positionConstraint) {
        if (positionConstraint == null)
            throw new NullPointerException("positionConstraint is null");
        this.positionConstraint = positionConstraint;
        return this;
    }
    
    public WindowLocationConfiguration initialPosition(InitialShellPosition initialPosition) {
        if (initialPosition == null)
            throw new NullPointerException("initialPosition is null");
        this.initialPosition = initialPosition;
        return this;
    }
    
    public WindowLocationConfiguration persistNothing() {
        persistLocation = false;
        persistSize = false;
        return this;
    }
    
    public WindowLocationConfiguration persistLocation(boolean persistLocation) {
        this.persistLocation = persistLocation;
        return this;
    }
    
    public WindowLocationConfiguration persistSize(boolean persistSize) {
        this.persistSize = persistSize;
        return this;
    }
    
    public Point getDefaultSize() {
        return defaultSize;
    }
    
    public InitialShellPosition getInitialPosition() {
        return initialPosition;
    }
    
    public ShellPositionConstraint getPositionConstraint() {
        return positionConstraint;
    }
    
    public boolean shouldPersistSomething() {
        return persistLocation || persistSize;
    }
    
    public boolean shouldPersistLocation() {
        return persistLocation;
    }
    
    public boolean shouldPersistSize() {
        return persistSize;
    }
    
}
