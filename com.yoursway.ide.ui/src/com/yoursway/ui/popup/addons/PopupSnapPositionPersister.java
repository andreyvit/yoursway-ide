package com.yoursway.ui.popup.addons;

import java.util.Collection;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.ui.services.IDisposable;

import com.yoursway.ui.popup.IPopupSnappingListener;
import com.yoursway.ui.popup.ISnapPosition;
import com.yoursway.ui.popup.SnapPosition;
import com.yoursway.ui.popup.SnappableYellowPopup;

/**
 * Persists and restores the position the given popup is snapped to in Eclipse
 * dialog settings.
 * 
 * @author Andrey Tarantsov
 */
public class PopupSnapPositionPersister {
    
    private final SnappableYellowPopup popup;
    private final IDialogSettings settingsSection;
    
    private static final String POSITION_KEY = "snappedAt";
    private final IDisposable snappingListenerHandle;
    
    public PopupSnapPositionPersister(SnappableYellowPopup popup, IDialogSettings settingsSection) {
        this.popup = popup;
        this.settingsSection = settingsSection;
        restorePosition();
        snappingListenerHandle = popup.addSnappingListener(new IPopupSnappingListener() {
            
            public void popupSnapped(SnappableYellowPopup popup, ISnapPosition newPosition) {
                savePosition();
            }
            
        });
    }
    
    public void dispose() {
        snappingListenerHandle.dispose();
    }
    
    protected void restorePosition() {
        String id = settingsSection.get(POSITION_KEY);
        SnapPosition position = findPosition(id);
        if (position != null)
            popup.setSnapPosition(position);
    }
    
    protected void savePosition() {
        settingsSection.put(POSITION_KEY, popup.getSnapPosition().getId());
    }
    
    private SnapPosition findPosition(String id) {
        Collection<? extends SnapPosition> snapPositions = popup.getSnapPositions();
        for (SnapPosition position : snapPositions) {
            if (position.getId().equals(id))
                return position;
        }
        return null;
    }
}
