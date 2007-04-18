/**
 * 
 */
package com.yoursway.ide.ui.railsview.presentation.misc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Hyperlink;

public class DisableAwareHyperlink extends Hyperlink {
    //workaround for 169859: Hyperlink widget should be rendered gray when disabled
    
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