/**
 * 
 */
package com.yoursway.ui.popup.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.HyperlinkGroup;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;

import com.yoursway.ui.popup.DisableAwareHyperlink;
import com.yoursway.ui.popup.SnappableYellowPopup;

/**
 * A pair of a hyperlink and keybinding text, shown as a single row in a
 * <code>TableBasedPopup</code>.
 * 
 * @author Andrey Tarantsov
 */
public class InfoEntry {
    private final Hyperlink fLink;
    private final Label fBindingLabel;
    
    public InfoEntry(Composite table, HyperlinkGroup hyperlinkGroup, String info, final Runnable runnable,
            String keybinding, SnappableYellowPopup popup) {
        final Display display = table.getDisplay();
        fLink = new DisableAwareHyperlink(table, SWT.NONE);
        fLink.setText(info);
        fLink.setForeground(hyperlinkGroup.getForeground());
        fLink.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(HyperlinkEvent e) {
                //workaround for 157196: [Forms] Hyperlink listener notification throws AIOOBE when listener removed in callback
                display.asyncExec(runnable);
            }
        });
        hyperlinkGroup.add(fLink);
        
        if (keybinding != null) {
            fBindingLabel = new Label(table, SWT.NONE);
            fBindingLabel.setText(keybinding);
            popup.addMoveSupport(fBindingLabel);
        } else {
            fBindingLabel = null;
        }
    }
    
    public void setEnabled(boolean enabled) {
        fLink.setEnabled(enabled);
        if (fBindingLabel != null)
            fBindingLabel.setEnabled(enabled);
    }
    
    public Hyperlink getLink() {
        return fLink;
    }
}