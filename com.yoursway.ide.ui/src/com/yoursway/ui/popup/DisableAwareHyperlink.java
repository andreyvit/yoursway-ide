/**
 * 
 */
package com.yoursway.ui.popup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Hyperlink;

/**
 * workaround for 169859: Hyperlink widget should be rendered gray when disabled
 * 
 * @author Andrey Tarantsov
 */
public class DisableAwareHyperlink extends Hyperlink {
    
    public DisableAwareHyperlink(Composite parent, int style) {
        super(parent, style);
    }
    
    @Override
    public Color getForeground() {
        if (getEnabled())
            return super.getForeground();
        else
            return getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
    }
}