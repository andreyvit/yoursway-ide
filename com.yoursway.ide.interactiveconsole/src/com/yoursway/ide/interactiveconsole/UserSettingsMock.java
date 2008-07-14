package com.yoursway.ide.interactiveconsole;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.yoursway.ide.debug.model.ExternalDebug;
import com.yoursway.ide.debug.model.IDebug;

public class UserSettingsMock implements IUserSettings {
    
    private final Display display;
    private final CommandHistory history;
    private final ExternalDebug debug;
    
    public UserSettingsMock() {
        display = new Display();
        history = new CommandHistory();
        debug = new ExternalDebug("irb", history);
    }
    
    public Display display() {
        return display;
    }
    
    public CommandHistory history() {
        return history;
    }
    
    public IDebug debug() {
        return debug;
    }
    
    public String consoleTitle() {
        return "Interactive Console";
    }
    
    public Rectangle consoleBounds() {
        return new Rectangle(240, 240, 640, 240);
    }
    
    public Font consoleFont() {
        return new Font(display, "Monaco", 12, 0);
    }
    
    public String greetings() {
        return "Hello world!\n";
    }
    
    public String inputPrompt() {
        return ">";
    }
    
    public StyleRange inputStyle(StyleRange range) {
        StyleRange r = (StyleRange) range.clone(); //? need not to clone
        r.fontStyle = SWT.BOLD;
        r.underline = true;
        return r;
    }
    
    public StyleRange errorStyle(StyleRange range) {
        StyleRange r = (StyleRange) range.clone(); //? need not to clone
        r.foreground = new Color(display, 192, 0, 0);
        return r;
    }
    
    public boolean isCopyHotkeys(KeyEvent e) {
        return e.character == 'c' && (e.stateMask == SWT.CONTROL || e.stateMask == SWT.COMMAND);
    }
    
    public Point proposalListSize() {
        return new Point(200, 100);
    }
    
}
