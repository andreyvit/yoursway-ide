package com.yoursway.ide.interactiveconsole;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;

public class Console {
    
    private final IUserSettings settings;
    
    private final CommandHistory history;
    private final IDebug debug;
    
    private final Display display;
    private final Shell shell;
    private final StyledText styledText;
    private final CompletionProposalPopup proposalPopup;
    
    private boolean disabled;
    private boolean inputting;
    private boolean needToScrollToEnd;
    
    public Console(final IUserSettings settings) {
        this.settings = settings;
        
        history = settings.history();
        debug = settings.debug();
        
        disabled = false;
        
        display = settings.display();
        shell = new Shell(display);
        shell.setText(settings.consoleTitle());
        shell.setBounds(settings.consoleBounds());
        shell.setLayout(new FillLayout());
        
        proposalPopup = new CompletionProposalPopup(shell, this, settings);
        
        styledText = new StyledText(shell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        styledText.setFont(settings.consoleFont());
        
        output(settings.greetings(), false);
        prepareForInput();
        
        // listeners
        
        debug.addTerminationListener(new ITerminationListener() {
            
            public void terminated() {
                disabled = true;
            }
            
        });
        
        debug.addOutputListener(new IOutputListener() {
            
            public void outputted(final String text, final boolean error) {
                display.syncExec(new Runnable() {
                    
                    public void run() {
                        output(text, error);
                    }
                    
                });
            }
            
        });
        
        styledText.addVerifyListener(new VerifyListener() {
            
            public void verifyText(VerifyEvent e) {
                ScrollBar scrollbar = styledText.getVerticalBar();
                boolean scrolledToEnd = (scrollbar.getSelection() + scrollbar.getPageIncrement() == scrollbar
                        .getMaximum());
                needToScrollToEnd = scrolledToEnd;
            }
            
        });
        
        styledText.addExtendedModifyListener(new ExtendedModifyListener() {
            
            public void modifyText(ExtendedModifyEvent event) {
                if (inputting) {
                    StyleRange style = styleRange(event.start, event.length);
                    styledText.setStyleRange(settings.inputStyle(style));
                }
                
                if (needToScrollToEnd) {
                    scrollToEnd();
                }
                
                proposalPopup.update();
            }
            
        });
        
        styledText.addVerifyKeyListener(new VerifyKeyListener() {
            
            public void verifyKey(VerifyEvent e) {
                
                if (disabled || !inInput()) {
                    if (!settings.isCopyHotkeys(e)) {
                        e.doit = false;
                        return;
                    }
                }
                
                //
                
                if (e.character == '\n' || e.character == '\r' || e.character == '\t')
                    e.doit = false;
                
                if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN)
                    e.doit = false;
                
                if (e.character == '\b' || e.keyCode == SWT.ARROW_LEFT) {
                    if (styledText.getCaretOffset() <= inputOffset())
                        e.doit = false;
                }
                
                if (e.keyCode == SWT.PAGE_UP || e.keyCode == SWT.PAGE_DOWN) {
                    e.doit = false;
                    
                    int increment = styledText.getVerticalBar().getPageIncrement()
                            / styledText.getLineHeight();
                    int index = styledText.getTopIndex();
                    index += (e.keyCode == SWT.PAGE_UP) ? -increment : increment;
                    styledText.setTopIndex(index);
                }
                
                if (e.keyCode == SWT.HOME || e.keyCode == SWT.END) {
                    e.doit = false;
                    
                    int index = (e.keyCode == SWT.HOME) ? 0 : styledText.getLineCount();
                    styledText.setTopIndex(index);
                }
                
            }
            
        });
        
        styledText.addTraverseListener(new TraverseListener() {
            
            public void keyTraversed(TraverseEvent e) {
                
                if (disabled) {
                    e.doit = false;
                    return;
                }
                
                proposalPopup.update();
                
                switch (e.detail) {
                
                case SWT.TRAVERSE_RETURN:
                    if (proposalPopup.visible()) {
                        proposalPopup.apply();
                    } else {
                        String command = command();
                        if (command.trim().equals(""))
                            return;
                        
                        styledText.append("\n");
                        prepareForInput();
                        
                        debug.executeCommand(command);
                        history.add(command);
                    }
                    break;
                
                case SWT.TRAVERSE_ARROW_NEXT:
                case SWT.TRAVERSE_ARROW_PREVIOUS:
                    if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
                        String command = command();
                        
                        if (e.keyCode == SWT.ARROW_UP)
                            history.previous(command);
                        else
                            history.next(command);
                        
                        int commandLength = command.length();
                        styledText.replaceTextRange(styledText.getCharCount() - commandLength, commandLength,
                                history.command());
                        
                        moveCaretToEnd(); //> don't move if command didn't replace
                    }
                    break;
                
                case SWT.TRAVERSE_TAB_NEXT:
                    proposalPopup.showOrSelectNext();
                    scrollToEnd();
                    break;
                
                case SWT.TRAVERSE_TAB_PREVIOUS:
                    proposalPopup.selectPrevious();
                    break;
                
                case SWT.TRAVERSE_ESCAPE:
                    proposalPopup.hide();
                    break;
                
                }
                
            }
            
        });
        
        styledText.addMouseListener(new MouseListener() {
            
            public void mouseDoubleClick(MouseEvent e) {
                
            }
            
            public void mouseDown(MouseEvent e) {
                proposalPopup.update();
            }
            
            public void mouseUp(MouseEvent e) {
                
            }
            
        });
        
        styledText.getVerticalBar().addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent e) {
                
            }
            
            public void widgetSelected(SelectionEvent e) {
                proposalPopup.hide();
            }
        });
        
    }
    
    public static void main(String[] args) {
        IUserSettings settings = new UserSettingsMock();
        
        Console console = new Console(settings);
        Shell shell = console.shell();
        Display display = settings.display();
        
        shell.open();
        
        while (!shell.isDisposed()) {
            try {
                if (!display.readAndDispatch())
                    display.sleep();
            } catch (Throwable throwable) {
                throwable.printStackTrace(System.err);
            }
        }
        
        display.dispose();
    }
    
    private Shell shell() {
        return shell;
    }
    
    private void output(final String string, boolean error) {
        inputting = false;
        int place = styledText.getText().lastIndexOf('\n');
        if (place < 0)
            place = 0;
        styledText.replaceTextRange(place, 0, string);
        if (error) {
            StyleRange range = styleRange(place, string.length());
            styledText.setStyleRange(settings.errorStyle(range));
        }
        inputting = true;
    }
    
    private StyleRange styleRange(int place, int length) {
        return new StyleRange(place, length, styledText.getForeground(), styledText.getBackground());
    }
    
    private void prepareForInput() {
        inputting = false;
        styledText.append("\n" + inputPrompt());
        moveCaretToEnd();
        inputting = true;
    }
    
    private void scrollToEnd() {
        Point selection = styledText.getSelection();
        moveCaretToEnd();
        styledText.setSelection(selection);
    }
    
    private void moveCaretToEnd() {
        styledText.setSelection(styledText.getCharCount());
    }
    
    private boolean inInput() {
        return (styledText.getSelection().x >= inputOffset());
    }
    
    private int inputOffset() {
        return styledText.getText().lastIndexOf('\n') + 1 + inputPrompt().length(); //!
    }
    
    private String inputPrompt() {
        return settings.inputPrompt();
    }
    
    private String command() {
        return styledText.getText().substring(inputOffset());
    }
    
    public List<CompletionProposal> getCompletionProposals() {
        int position = styledText.getSelection().x - inputOffset();
        if (position < 0)
            return null;
        return debug.complete(command(), position);
    }
    
    public Point getLocationForPopup() {
        int offset = inputOffset();
        Point p = styledText.getLocationAtOffset(offset);
        p.y += styledText.getLineHeight(offset);
        return styledText.toDisplay(p);
    }
    
    public void useCompletionProposal(final CompletionProposal proposal, boolean select) {
        int start = inputOffset() + proposal.replaceStart();
        int length = proposal.replaceLength();
        String text = proposal.text();
        
        styledText.replaceTextRange(start, length, text);
        if (select)
            styledText.setSelectionRange(start + length, text.length() - length);
        else
            moveCaretToEnd();
    }
    
    public void focus() {
        shell.setActive();
    }
    
}
