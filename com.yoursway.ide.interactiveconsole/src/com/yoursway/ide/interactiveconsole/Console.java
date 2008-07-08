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
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Console extends StyledText {
    
    private static Display display;
    private static CompletionProposalPopup proposalPopup;
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
        
        addVerifyKeyListener(new VerifyKeyListener() {
            
            public void verifyKey(VerifyEvent e) {
                
                switch (e.character) {
                
                case '\n':
                case '\r':
                    if (command().trim().equals(""))
                        e.doit = false;
                    break;
                
                case '\t':
                    e.doit = false;
                    int position = getSelection().x - inputStartOffset();
                    List<CompletionProposal> proposals = debug.complete(command(), position);
                    proposalPopup.show(proposals);
                    break;
                }
                
                if (e.character == '\b' || e.keyCode == SWT.ARROW_LEFT) {
                    if (getCaretOffset() <= inputStartOffset())
                        e.doit = false;
                }
                
            }
            
        });
        
        addKeyListener(new KeyListener() {
            
            public void keyPressed(KeyEvent e) {
                if (e.character == '\n' || e.character == '\r') {
                    
                    String command = command(); //? trim
                    if (command.trim().equals("")) //? extract method
                        return;
                    
                    prepareForInput();
                    
                    debug.executeCommand(command);
                    debug.addToHistory(command);
                    
                    history = debug.getHistory();
                    historyIndex = history.length;
                }

                else if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
                    
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
                    
                    moveCaretToEnd(); //? what if command didn't replace
                }
                
            }
            
            public void keyReleased(KeyEvent e) {
                
            }
            
        });
        
        // addVerifyListener(new VerifyListener() {});
        // addSelectionListener(new SelectionListener() {});
        // addWordMovementListener(new MovementListener() {});
        
    }
    
    public static void main(String[] args) {
        display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Interactive Console");
        
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
    
    private String command() {
        String[] lines = getText().split("\n");
        return lines[lines.length - 1].substring(inputPrefix().length());
        //? use inputPrefixOffset instead
    }
    
    public int inputStartOffset() {
        return getText().lastIndexOf('\n') + 1 + inputPrefix().length();
    }
    
    public void useCompletionProposal(final CompletionProposal proposal) {
        int start = inputStartOffset() + proposal.replaceStart();
        int length = proposal.replaceLength();
        String text = proposal.text();
        
        //? need not to use syncExec? it's weird
        replaceTextRange(start, length, text);
        // setSelectionRange(start, text.length());
        moveCaretToEnd();
    }
}
