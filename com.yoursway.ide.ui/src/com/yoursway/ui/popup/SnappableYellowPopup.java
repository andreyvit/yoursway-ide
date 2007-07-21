package com.yoursway.ui.popup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tracker;
import org.eclipse.ui.services.IDisposable;

import com.yoursway.common.TypedListenerList;

/**
 * A non-modal popup shell manager. The created shell does not have a titlebar
 * and generally looks like a tooltip.
 * 
 * The created shell will snap to one of the locations provided by the client
 * (it will never float). The user may drag the shell to any of such locations.
 * Child controls which should allow the shell to be dragged must be registered
 * by <code>addMoveSupport</code> method.
 * 
 * @author Andrey Tarantsov
 */
public abstract class SnappableYellowPopup {
    
    public enum ParentShellActivation {
        
        ACTIVATE_PARENT_WHEN_POSSIBLE,

        KEEP_POPUP_ACTIVE,
        
    }
    
    private SnapPosition snapPosition;
    private final IPopupHost popupOwner;
    
    private final TypedListenerList<IPopupLifecycleListener> lifecycleListeners = new TypedListenerList<IPopupLifecycleListener>() {
        
        @Override
        protected IPopupLifecycleListener[] makeArray(int size) {
            return new IPopupLifecycleListener[size];
        }
        
    };
    
    private final TypedListenerList<IPopupSnappingListener> snappingListeners = new TypedListenerList<IPopupSnappingListener>() {
        
        @Override
        protected IPopupSnappingListener[] makeArray(int size) {
            return new IPopupSnappingListener[size];
        }
        
    };
    
    private final TypedListenerList<IPopupVoter> voters = new TypedListenerList<IPopupVoter>() {
        
        @Override
        protected IPopupVoter[] makeArray(int size) {
            return new IPopupVoter[size];
        }
        
    };
    private final Collection<SnapPosition> snapPositions;
    private final ParentShellActivation parentShellActivationPolicy;
    private Shell popup;
    
    public SnappableYellowPopup(IPopupHost popupOwner, Collection<? extends SnapPosition> snapPositions,
            ParentShellActivation activateParentShell) {
        this.parentShellActivationPolicy = activateParentShell;
        Assert.isLegal(!snapPositions.isEmpty(), "At least one snap position must be specified");
        this.popupOwner = popupOwner;
        this.snapPositions = Collections.unmodifiableCollection(new ArrayList<SnapPosition>(snapPositions));
        setSnapPosition(this.snapPositions.iterator().next());
    }
    
    public void activateParentShell() {
        getParentShell().setActive();
    }
    
    public void activateParentShellIfRequested() {
        if (parentShellActivationPolicy == ParentShellActivation.ACTIVATE_PARENT_WHEN_POSSIBLE)
            activateParentShell();
    }
    
    public IDisposable addLifecycleListener(final IPopupLifecycleListener listener) {
        lifecycleListeners.add(listener);
        return new IDisposable() {
            
            public void dispose() {
                lifecycleListeners.remove(listener);
            }
            
        };
    }
    
    public IDisposable addVoter(final IPopupVoter boter) {
        voters.add(boter);
        return new IDisposable() {
            
            public void dispose() {
                voters.remove(boter);
            }
            
        };
    }
    
    public void addMoveSupport(final Control movedControl) {
        movedControl.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseDown(final MouseEvent downEvent) {
                if (downEvent.button != 1) {
                    return;
                }
                
                final Point POPUP_SOURCE = popup.getLocation();
                final Composite parent = popupOwner.getParent();
                Point pSize = popup.getSize();
                final SnapPosition[] newSnapPosition = new SnapPosition[1];
                
                /*
                 * Feature in Tracker: it is not possible to directly control
                 * the feedback, see
                 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=121300 and
                 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=121298#c1 .
                 * 
                 * Workaround is to have an offscreen rectangle for tracking
                 * mouse movement and a manually updated rectangle for the
                 * actual drop target.
                 */
                final Tracker tracker = new Tracker(parent, SWT.NONE);
                
                Collection<Rectangle> dropTargets = new ArrayList<Rectangle>(snapPositions.size());
                Collection<Point> locations = new ArrayList<Point>(snapPositions.size());
                Rectangle currentRect = null;
                for (ISnapPosition snapPos : snapPositions) {
                    Point topLeft = parent.toControl(snapPos.calculate(pSize, parent));
                    locations.add(topLeft);
                    final Rectangle rect = Geometry.createRectangle(topLeft, pSize);
                    if (snapPos == snapPosition)
                        currentRect = rect;
                    dropTargets.add(rect);
                }
                final SnapPosition[] SNAP_POSITIONS = snapPositions.toArray(new SnapPosition[snapPositions
                        .size()]);
                final Rectangle[] DROP_TARGETS = dropTargets.toArray(new Rectangle[dropTargets.size()]);
                final Point[] LOCATIONS = locations.toArray(new Point[locations.size()]);
                final Rectangle MOUSE_MOVE_SOURCE = new Rectangle(1000000, 0, 0, 0);
                tracker.setRectangles(new Rectangle[] { MOUSE_MOVE_SOURCE, currentRect });
                tracker.setStippled(true);
                
                ControlListener moveListener = new ControlAdapter() {
                    /*
                     * @see org.eclipse.swt.events.ControlAdapter#controlMoved(org.eclipse.swt.events.ControlEvent)
                     */
                    @Override
                    public void controlMoved(ControlEvent moveEvent) {
                        Rectangle[] currentRects = tracker.getRectangles();
                        final Rectangle mouseMoveCurrent = currentRects[0];
                        Point popupLoc = new Point(POPUP_SOURCE.x + mouseMoveCurrent.x - MOUSE_MOVE_SOURCE.x,
                                POPUP_SOURCE.y + mouseMoveCurrent.y - MOUSE_MOVE_SOURCE.y);
                        
                        popup.setLocation(popupLoc);
                        
                        Point ePopupLoc = parent.toControl(popupLoc);
                        int minDist = Integer.MAX_VALUE;
                        for (int snapPos = 0; snapPos < DROP_TARGETS.length; snapPos++) {
                            int dist = Geometry.distanceSquared(ePopupLoc, LOCATIONS[snapPos]);
                            if (dist < minDist) {
                                minDist = dist;
                                newSnapPosition[0] = SNAP_POSITIONS[snapPos];
                                currentRects[1] = DROP_TARGETS[snapPos];
                            }
                        }
                        tracker.setRectangles(currentRects);
                    }
                };
                tracker.addControlListener(moveListener);
                boolean committed = tracker.open();
                tracker.close();
                tracker.dispose();
                if (committed) {
                    setSnapPosition(newSnapPosition[0]);
                }
                activateParentShellIfRequested();
            }
        });
    }
    
    public IDisposable addSnappingListener(final IPopupSnappingListener listener) {
        snappingListeners.add(listener);
        return new IDisposable() {
            
            public void dispose() {
                snappingListeners.remove(listener);
            }
            
        };
    }
    
    public void close() {
        if (popup != null) {
            if (!popup.isDisposed()) {
                popup.close();
            }
            popup = null;
            for (IPopupLifecycleListener listener : lifecycleListeners.getListeners())
                listener.popupClosed();
            dispose();
        }
    }
    
    protected void dispose() {
    }
    
    public IPopupHost getPopupOwner() {
        return popupOwner;
    }
    
    public Shell getShell() {
        return popup;
    }
    
    public SnapPosition getSnapPosition() {
        return snapPosition;
    }
    
    public Collection<? extends SnapPosition> getSnapPositions() {
        return snapPositions;
    }
    
    public boolean isOpen() {
        return popup != null && !popup.isDisposed();
    }
    
    public void open() {
        if (isOpen())
            close();
        
        Shell workbenchShell = getParentShell();
        
        popup = new Shell(workbenchShell, SWT.ON_TOP | SWT.NO_TRIM);
        GridLayout shellLayout = new GridLayout();
        shellLayout.marginWidth = 1;
        shellLayout.marginHeight = 1;
        popup.setLayout(shellLayout);
        popup.setBackground(popupOwner.getParent().getForeground());
        
        createPopupContents(popup);
        
        popup.pack();
        updatePopupLocation();
        
        addMoveSupport(popup);
        
        popup.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                //XXX workaround for bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=173438 :
                //                  fRenameLinkedMode.cancel();
                getParentShell().getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        close();
                    }
                });
            }
        });
        
        //		fPopup.moveBelow(null); // make sure hovers are on top of the info popup
        // XXX workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=170774
        popup.moveBelow(workbenchShell.getShells()[0]);
        
        popup.setVisible(true);
        
        for (IPopupLifecycleListener listener : lifecycleListeners.getListeners())
            listener.popupOpened();
    }
    
    public void setSnapPosition(SnapPosition newSnapPosition) {
        snapPosition = newSnapPosition;
        if (isOpen())
            updatePopupLocation();
        for (IPopupSnappingListener listener : snappingListeners.getListeners())
            listener.popupSnapped(this, newSnapPosition);
    }
    
    protected abstract Control createPopupContents(Composite parent);
    
    public Shell getParentShell() {
        return popupOwner.getShell();
    }
    
    void updatePopupLocation() {
        Point loc = snapPosition.calculate(popup.getSize(), popupOwner.getParent());
        if (loc != null) {
            popup.setLocation(loc);
            // XXX workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=170774
            popup.moveBelow(getParentShell().getShells()[0]);
        }
    }
    
    public void setVisible(boolean visibility) {
        if (!isOpen())
            throw new IllegalStateException("Cannot adjust visibility when popup is not open");
        if (getShell().getVisible() != visibility)
            getShell().setVisible(visibility);
    }
    
    protected PopupPoll createPoll() {
        return new PopupPoll(this);
    }
    
    public void revote() {
        final PopupPoll poll = createPoll();
        IPopupVoter[] listeners = voters.getListeners();
        for (IPopupVoter voter : listeners) {
            voter.participate(poll);
        }
        // To hit less bugs that are quite common in SWT
        // TODO: probably remove when Eclipse 3.3 ships
        getParentShell().getDisplay().asyncExec(new Runnable() {
            public void run() {
                poll.publishResultsInNewspaper();
            }
        });
    }
    
}
