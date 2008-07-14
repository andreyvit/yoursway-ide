package com.yoursway.ide.worksheet.view;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.ide.debug.model.IDebug;
import com.yoursway.ide.debug.model.IOutputListener;
import com.yoursway.ide.worksheet.viewmodel.IUserSettings;
import com.yoursway.ide.worksheet.viewmodel.UserSettingsMock;

public class Worksheet {
    
    private final IUserSettings settings;
    
    private final IDebug debug;
    
    private final Display display;
    private final Shell shell;
    private final StyledText styledText;
    
    private final List<Insertion> insertions = new LinkedList<Insertion>();
    private Insertion outputInsertion;
    
    public Worksheet(final IUserSettings settings) {
        this.settings = settings;
        
        display = settings.display();
        shell = new Shell(display);
        shell.setText(settings.worksheetTitle());
        shell.setBounds(settings.worksheetBounds());
        shell.setLayout(new FillLayout());
        
        styledText = new StyledText(shell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        styledText.setFont(settings.workspaceFont());
        
        styledText.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                for (Iterator<Insertion> it = insertions.iterator(); it.hasNext();) {
                    Insertion insertion = it.next();
                    
                    insertion.updateOffset(e);
                    
                    if (insertion.disposed())
                        it.remove();
                }
            }
        });
        styledText.addPaintObjectListener(new PaintObjectListener() {
            public void paintObject(PaintObjectEvent e) {
                for (Insertion insertion : insertions) {
                    insertion.updateLocation(e);
                }
            }
        });
        
        styledText.addVerifyKeyListener(new VerifyKeyListener() {
            public void verifyKey(VerifyEvent e) {
                if (settings.isExecHotkey(e)) {
                    executeCommand();
                }

                else if (settings.isRemoveInsertionsHotkey(e)) {
                    removeAllInsertions();
                }

                else {
                    // it's ok, nothing to do
                }
            }
        });
        
        debug = settings.debug();
        debug.addOutputListener(new IOutputListener() {
            public void outputted(final String text, boolean error) {
                display.syncExec(new Runnable() {
                    public void run() {
                        output(text);
                    }
                });
            }
        });
    }
    
    public static void main(String[] args) {
        IUserSettings settings = new UserSettingsMock();
        
        Worksheet worksheet = new Worksheet(settings);
        Shell shell = worksheet.shell();
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
    
    private String insertionPlaceholder() {
        return "\uFFFC";
    }
    
    private int insertionPlaceholderLength() throws AssertionError {
        if (insertionPlaceholder().length() != 1)
            throw new AssertionError("An insertion placeholder must have 1 char length.");
        return 1;
    }
    
    private synchronized void executeCommand() {
        int offset = styledText.getCaretOffset();
        int lineIndex = styledText.getLineAtOffset(offset);
        
        outputInsertion = resetInsertionAtLine(lineIndex);
        executeCommandAtLine(lineIndex);
    }
    
    private void executeCommandAtLine(int lineIndex) {
        String command = styledText.getLine(lineIndex);
        debug.executeCommand(command);
    }
    
    private Insertion resetInsertionAtLine(int lineIndex) throws AssertionError {
        if (lineHasInsertion(lineIndex)) {
            Insertion insertion = insertion(lineIndex);
            insertion.setText("");
            return insertion;
        } else {
            return addInsertionAtLine(lineIndex);
        }
    }
    
    private Insertion insertion(int lineIndex) {
        int offset = lineEndOffset(lineIndex) + 1;
        for (Insertion insertion : insertions) {
            if (insertion.offset() == offset)
                return insertion;
        }
        return null;
    }
    
    private boolean removeInsertionAtLine(int lineIndex) {
        if (!lineHasInsertion(lineIndex))
            return false;
        
        int s = insertions.size();
        
        int offset = lineEndOffset(lineIndex);
        //! must be 2 == ("\n" + insertionPlaceholder()).length()
        styledText.replaceTextRange(offset, 2, "");
        
        if (insertions.size() != s - 1)
            throw new AssertionError("Insertion object hasn't been removed from collection.");
        
        return true;
    }
    
    private void removeAllInsertions() {
        for (int i = 0; i < styledText.getLineCount(); i++) {
            removeInsertionAtLine(i);
        }
    }
    
    private int lineEndOffset(int lineIndex) {
        int lineOffset = styledText.getOffsetAtLine(lineIndex);
        int lineLength = styledText.getLine(lineIndex).length();
        return lineOffset + lineLength;
    }
    
    private boolean lineHasInsertion(int lineIndex) {
        int offset = lineEndOffset(lineIndex);
        if (styledText.getCharCount() < offset + 2)
            return false;
        return styledText.getText(offset, offset + 1).equals("\n" + insertionPlaceholder());
    }
    
    private Insertion addInsertionAtLine(int lineIndex) throws AssertionError {
        int offset = lineEndOffset(lineIndex);
        
        styledText.replaceTextRange(offset, 0, "\n" + insertionPlaceholder());
        offset++; // "\n"
        
        Insertion insertion = new Insertion(offset, "", this);
        insertions.add(insertion);
        
        return insertion;
    }
    
    public void updateMetrics(int offset, Control control) {
        StyleRange style = new StyleRange();
        style.start = offset;
        style.length = insertionPlaceholderLength();
        
        control.pack();
        Rectangle rect = control.getBounds();
        
        style.metrics = new GlyphMetrics(rect.height, 0, rect.width);
        styledText.setStyleRange(style);
    }
    
    private synchronized void output(String text) {
        outputInsertion.append(text);
    }
    
    public Composite styledText() {
        return styledText;
    }
}
