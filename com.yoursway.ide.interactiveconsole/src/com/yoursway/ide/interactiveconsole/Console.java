package com.yoursway.ide.interactiveconsole;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.MovementEvent;
import org.eclipse.swt.custom.MovementListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Console extends StyledText {
    
    private static Display display;
    private boolean inputting;
    private String[] history;
    private int historyIndex;
    
    public Console(Composite parent, final IDebug debug) {
        super(parent, SWT.MULTI | SWT.WRAP);
        
        setFont(new Font(display, "Monaco", 12, 0));
        setBounds(parent.getClientArea());
        
        output("Hello world!\n");
        prepareForInput();
        
        history = debug.getHistory(); //> get old history
        
        debug.addOutputListener(new IDebugOutputListener() {
            
            public void outputString(final String string) {
                display.syncExec(new Runnable() {
                    
                    public void run() {
                        output(string);
                    }
                    
                });
            }
            
        });
        
        addExtendedModifyListener(new ExtendedModifyListener() {
            
            public void modifyText(ExtendedModifyEvent event) {
                if (inputting) {
                    setStyleRange(new StyleRange(event.start, event.length, getForeground(), getBackground(),
                            SWT.BOLD));
                }
            }
            
        });
        
        addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent e) {
                
            }
            
            public void widgetSelected(SelectionEvent e) {
                
            }
            
        });
        
        addVerifyKeyListener(new VerifyKeyListener() {
            
            public void verifyKey(VerifyEvent e) {
                
            }
            
        });
        
        addKeyListener(new KeyListener() {
            
            public void keyPressed(KeyEvent e) {
                if (e.character == '\n' || e.character == '\r') {
                    
                    String[] lines = getText().split("\n");
                    String command = lines[lines.length - 1].substring(inputPrefix().length());
                    //? trim
                    
                    if (command.trim().equals(""))
                        return;
                    
                    prepareForInput();
                    
                    debug.executeCommand(command);
                    debug.addToHistory(command);
                    
                    history = debug.getHistory();
                    historyIndex = history.length;
                }
                
                if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
                    
                    if (e.keyCode == SWT.ARROW_UP) {
                        if (historyIndex > 0)
                            historyIndex--;
                    } else { // e.keyCode == SWT.ARROW_DOWN
                        if (historyIndex < history.length - 1)
                            historyIndex++;
                    }
                    
                    if (historyIndex < history.length) {
                        String command = history[historyIndex];
                        int place = getText().lastIndexOf('\n') + 1 + inputPrefix().length();
                        replaceTextRange(place, getCharCount() - place, command);
                    }
                    
                    moveCaretToEnd(); //? what if command didn't replace
                }
            }
            
            public void keyReleased(KeyEvent e) {
                
            }
            
        });
        
        addVerifyListener(new VerifyListener() {
            
            public void verifyText(VerifyEvent e) {
                
            }
            
        });
        
        addWordMovementListener(new MovementListener() {
            
            public void getNextOffset(MovementEvent event) {
                
            }
            
            public void getPreviousOffset(MovementEvent event) {
                
            }
            
        });
        
    }
    
    public static void main(String[] args) {
        display = new Display();
        Shell shell = new Shell(display);
        
        shell.setText("Interactive Console");
        
        new Console(shell, new DebugMock());
        
        shell.open();
        
        while (!shell.isDisposed()) { //?
            if (!display.readAndDispatch())
                display.sleep();
        }
        
        display.dispose();
    }
    
    private void output(final String string) {
        inputting = false;
        int place = getText().lastIndexOf('\n');
        if (place < 0)
            place = 0;
        replaceTextRange(place, 0, string);
        inputting = true;
    }
    
    private void prepareForInput() { //? rename to printInputPrefix
        append("\n" + inputPrefix());
        moveCaretToEnd();
        inputting = true;
    }
    
    private void moveCaretToEnd() {
        setCaretOffset(getCharCount());
    }
    
    private String inputPrefix() {
        return ">";
    }
    
}
