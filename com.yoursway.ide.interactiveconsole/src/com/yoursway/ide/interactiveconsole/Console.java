package com.yoursway.ide.interactiveconsole;

import java.util.LinkedList;
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
    
    private String[] history;
    private int historyIndex;
    
    public Console(Composite parent, final IDebug debug) {
        super(parent, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        
        setFont(new Font(display, "Monaco", 12, 0));
        setBounds(parent.getClientArea());
        
        output("Hello world!\n");
        prepareForInput();
        
        this.debug = debug;
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
                int selection = scrollbar.getSelection();
                int maximum = scrollbar.getMaximum();
                int pageIncrement = scrollbar.getPageIncrement();
                needToScrollToEnd = (selection + pageIncrement == maximum);
            }
            
        });
        
        addVerifyKeyListener(new VerifyKeyListener() {
            
            public void verifyKey(VerifyEvent e) {
                
                switch (e.character) {
                
                case '\n':
                case '\r':
                    e.doit = false;
                    
                    if (proposalPopup.visible()) {
                        proposalPopup.apply();
                    } else {
                        String command = command(); //? trim
                        if (command.trim().equals(""))
                            return;
                        
                        append("\n");
                        prepareForInput();
                        
                        debug.executeCommand(command);
                        debug.addToHistory(command);
                        
                        history = debug.getHistory();
                        historyIndex = history.length;
                    }
                    
                    break;
                
                case '\t':
                    e.doit = false;
                    proposalPopup.showOrSelectNext();
                    scrollToEnd();
                    break;
                }
                
                if (e.character == '\b' || e.keyCode == SWT.ARROW_LEFT) {
                    if (getCaretOffset() <= inputStartOffset())
                        e.doit = false;
                }
                
                if (e.keyCode == SWT.ESC) {
                    proposalPopup.hide();
                }
                
            }
            
        });
        
        addKeyListener(new KeyListener() {
            
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
                    
                    if (e.keyCode == SWT.ARROW_UP) {
                        if (historyIndex > 0)
                            historyIndex--;
                    } else { // e.keyCode == SWT.ARROW_DOWN
                        if (historyIndex < history.length - 1)
                            historyIndex++;
                    }
                    
                    if (historyIndex < history.length) {
                        int commandLength = command().length(); //? ineffective
                        replaceTextRange(getCharCount() - commandLength, commandLength, history[historyIndex]);
                    }
                    
                    setSelectionToEnd(); //? what if command didn't replace
                }
                
            }
            
            public void keyReleased(KeyEvent e) {
                if (e.keyCode == '\t' && e.stateMask == SWT.SHIFT) {
                    proposalPopup.selectPrevious();
                }
            }
            
        });
        
        // addSelectionListener(new SelectionListener() {});
        // addWordMovementListener(new MovementListener() {});
        
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
        shell.setBounds(240, 240, 640, 240); //! magic
        shell.setLayout(new FillLayout());
        
        Console console = new Console(shell, new DebugMock());
        proposalPopup = new CompletionProposalPopup(shell, console);
        
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
    
    private void prepareForInput() {
        inputting = false;
        append("\n" + inputPrefix());
        setSelectionToEnd();
        inputting = true;
    }
    
    private void setSelectionToEnd() {
        setSelection(getCharCount());
    }
    
    private void scrollToEnd() {
        Point selection = getSelection();
        setSelectionToEnd();
        setSelection(selection);
    }
    
    private String inputPrefix() {
        return ">";
    }
    
    private String command() {
        String[] lines = getText().split("\n");
        return lines[lines.length - 1].substring(inputPrefix().length());
        //? use inputPrefixOffset instead
    }
    
    public int inputStartOffset() {
        return getText().lastIndexOf('\n') + 1 + inputPrefix().length();
    }
    
    public List<CompletionProposal> getCompletionProposals() {
        int position = getSelection().x - inputStartOffset();
        if (position < 0)
            return new LinkedList<CompletionProposal>();
        return debug.complete(command(), position);
    }
    
    public Point getLocationForPopup() {
        int offset = inputStartOffset(); //? getSelection().x;
        Point p = getLocationAtOffset(offset);
        //p.x -= proposalShell.getBorderWidth();
        p.y += getLineHeight(offset);
        return toDisplay(p);
    }
    
    public void useCompletionProposal(final CompletionProposal proposal) {
        int start = inputStartOffset() + proposal.replaceStart();
        int length = proposal.replaceLength();
        String text = proposal.text();
        
        //? need not to use syncExec? it's weird
        replaceTextRange(start, length, text);
        // setSelectionRange(start, text.length());
        setSelectionToEnd();
    }
    
}
