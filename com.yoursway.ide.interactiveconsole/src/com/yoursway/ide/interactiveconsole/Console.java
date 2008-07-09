package com.yoursway.ide.interactiveconsole;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;

public class Console extends StyledText implements IConsoleForProposalPopup {
    
    private final IDebug debug;
    
    private static Display display;
    private static CompletionProposalPopup proposalPopup;
    
    private boolean inputting;
    private boolean needToScrollToEnd;
    
    private static CommandHistory history = new CommandHistory();
    
    public Console(Composite parent, final IDebug debug) {
        super(parent, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        
        setFont(new Font(display, "Monaco", 12, 0));
        
        output("Hello world!\n");
        prepareForInput();
        
        this.debug = debug;
        
        // listeners
        
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
                    StyleRange style = new StyleRange(event.start, event.length, getForeground(),
                            getBackground(), SWT.BOLD);
                    style.underline = true;
                    setStyleRange(style);
                }
                
                if (needToScrollToEnd) {
                    scrollToEnd();
                }
            }
            
        });
        
        addVerifyListener(new VerifyListener() {
            
            public void verifyText(VerifyEvent e) {
                ScrollBar scrollbar = getVerticalBar();
                boolean scrolledToEnd = (scrollbar.getSelection() + scrollbar.getPageIncrement() == scrollbar
                        .getMaximum());
                needToScrollToEnd = scrolledToEnd;
            }
            
        });
        
        addVerifyKeyListener(new VerifyKeyListener() {
            
            public void verifyKey(VerifyEvent e) {
                
                if (!inInput()) {
                    if (e.character != 'c' || (e.stateMask != SWT.CONTROL && e.stateMask != SWT.COMMAND)) {
                        e.doit = false;
                        return;
                    }
                }
                
                switch (e.character) {
                
                case '\n':
                case '\r':
                    e.doit = false;
                    
                    if (proposalPopup.visible()) {
                        proposalPopup.apply();
                    } else {
                        String command = command();
                        if (command.trim().equals(""))
                            return;
                        
                        append("\n");
                        prepareForInput();
                        
                        debug.executeCommand(command);
                        history.add(command);
                    }
                    
                    break;
                
                case '\t':
                    e.doit = false;
                    proposalPopup.showOrSelectNext();
                    scrollToEnd();
                    break;
                }
                
                if (e.character == '\b' || e.keyCode == SWT.ARROW_LEFT) {
                    if (getCaretOffset() <= inputOffset())
                        e.doit = false;
                }

                else if (e.keyCode == SWT.ESC) {
                    proposalPopup.hide();
                }

                else if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
                    e.doit = false;
                    String command = command();
                    
                    if (e.keyCode == SWT.ARROW_UP) {
                        history.previous(command);
                    } else { // e.keyCode == SWT.ARROW_DOWN
                        history.next(command);
                    }
                    
                    int commandLength = command.length();
                    replaceTextRange(getCharCount() - commandLength, commandLength, history.command());
                    
                    moveCaretToEnd(); //> don't move if command didn't replace
                }
                
            }
            
        });
        
        addKeyListener(new KeyListener() {
            
            public void keyPressed(KeyEvent e) {
                
            }
            
            public void keyReleased(KeyEvent e) {
                if (e.keyCode == '\t' && e.stateMask == SWT.SHIFT) {
                    proposalPopup.selectPrevious();
                }
            }
            
        });
        
        getVerticalBar().addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent e) {
                
            }
            
            public void widgetSelected(SelectionEvent e) {
                proposalPopup.hide();
            }
        });
        
    }
    
    public static void main(String[] args) {
        display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Interactive Console");
        shell.setBounds(240, 240, 640, 240); //! magic :)
        shell.setLayout(new FillLayout());
        
        Console console = new Console(shell, new DebugMock(history));
        proposalPopup = new CompletionProposalPopup(shell, console);
        
        shell.open();
        
        while (!shell.isDisposed()) {
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
    
    private void prepareForInput() {
        inputting = false;
        append("\n" + inputPrompt());
        moveCaretToEnd();
        inputting = true;
    }
    
    private void scrollToEnd() {
        Point selection = getSelection();
        moveCaretToEnd();
        setSelection(selection);
    }
    
    private void moveCaretToEnd() {
        setSelection(getCharCount());
    }
    
    private boolean inInput() {
        return (getSelection().x >= inputOffset());
    }
    
    private int inputOffset() {
        return getText().lastIndexOf('\n') + 1 + inputPrompt().length();
    }
    
    private String inputPrompt() {
        return ">";
    }
    
    private String command() {
        return getText().substring(inputOffset());
    }
    
    public List<CompletionProposal> getCompletionProposals() {
        int position = getSelection().x - inputOffset();
        if (position < 0)
            return null;
        return debug.complete(command(), position);
    }
    
    public Point getLocationForPopup() {
        int offset = inputOffset();
        Point p = getLocationAtOffset(offset);
        p.y += getLineHeight(offset);
        return toDisplay(p);
    }
    
    public void useCompletionProposal(final CompletionProposal proposal, boolean select) {
        int start = inputOffset() + proposal.replaceStart();
        int length = proposal.replaceLength();
        String text = proposal.text();
        
        replaceTextRange(start, length, text);
        if (select)
            setSelectionRange(start + length, text.length() - length);
        else
            moveCaretToEnd();
    }
    
}
