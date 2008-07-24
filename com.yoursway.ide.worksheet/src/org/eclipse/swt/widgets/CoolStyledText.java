package org.eclipse.swt.widgets;

import org.eclipse.swt.custom.StyledText;

import com.mkalugin.swthell.CoolScrollBar;

public class CoolStyledText extends StyledText {
    
    private CoolScrollBar scrollBar;
    
    public CoolStyledText(Composite parent, int style) {
        super(parent, style);
    }
    
    @Override
    boolean sendMouseWheel(short wheelAxis, int wheelDelta) {
        return scrollBar.working();
    }
    
    public void setScrollBar(CoolScrollBar scrollBar) {
        this.scrollBar = scrollBar;
        
    }
    
}
