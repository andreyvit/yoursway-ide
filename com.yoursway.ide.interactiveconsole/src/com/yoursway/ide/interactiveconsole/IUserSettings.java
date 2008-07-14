package com.yoursway.ide.interactiveconsole;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.yoursway.ide.debug.model.IDebug;

public interface IUserSettings {
    
    Display display();
    
    CommandHistory history();
    
    IDebug debug();
    
    String consoleTitle();
    
    Rectangle consoleBounds();
    
    Font consoleFont();
    
    String greetings();
    
    String inputPrompt();
    
    StyleRange inputStyle(StyleRange range);
    
    StyleRange errorStyle(StyleRange range);
    
    boolean isCopyHotkeys(KeyEvent e);
    
    Point proposalListSize();
    
}
