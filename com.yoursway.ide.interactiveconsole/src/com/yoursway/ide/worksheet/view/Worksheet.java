package com.yoursway.ide.worksheet.view;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.ide.worksheet.controller.WorksheetController;
import com.yoursway.ide.worksheet.viewmodel.IUserSettings;
import com.yoursway.ide.worksheet.viewmodel.UserSettingsMock;

public class Worksheet {
    
    private final IUserSettings settings;
    private final WorksheetController controller;
    
    private final Shell shell;
    private final StyledText styledText;
    
    private final List<Insertion> insertions = new LinkedList<Insertion>();
    
    public Worksheet(final IUserSettings settings) {
        this.settings = settings;
        controller = new WorksheetController(this, settings);
        
        shell = new Shell(settings.display());
        shell.setText(settings.worksheetTitle());
        shell.setBounds(settings.worksheetBounds());
        shell.setLayout(new FillLayout());
        
        styledText = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP); //> SWT.WRAP or SWT.H_SCROLL switching
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
        styledText.addControlListener(new ControlListener() {
            public void controlMoved(ControlEvent e) {
                // nothing
            }
            
            public void controlResized(ControlEvent e) {
                for (Insertion insertion : insertions) {
                    insertion.updateSize();
                }
            }
        });
        
        styledText.addVerifyKeyListener(controller);
        styledText.addKeyListener(controller);
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
    
    public String command() {
        return command(caretLine());
    }
    
    private String command(int lineIndex) {
        return styledText.getLine(lineIndex);
    }
    
    private int caretLine() {
        int offset = styledText.getCaretOffset();
        return styledText.getLineAtOffset(offset);
    }
    
    public Insertion insertion() {
        return insertion(caretLine());
    }
    
    private Insertion insertion(int lineIndex) {
        if (lineHasInsertion(lineIndex))
            return existingInsertion(lineIndex);
        else
            return addInsertion(lineIndex);
    }
    
    private boolean lineHasInsertion(int lineIndex) {
        int offset = lineEndOffset(lineIndex);
        if (styledText.getCharCount() < offset + 2)
            return false;
        return styledText.getText(offset, offset + 1).equals("\n" + insertionPlaceholder());
    }
    
    private Insertion existingInsertion(int lineIndex) {
        int offset = lineEndOffset(lineIndex) + 1;
        for (Insertion insertion : insertions) {
            if (insertion.offset() == offset)
                return insertion;
        }
        return null;
    }
    
    public void updateMetrics(int offset, Rectangle rect) {
        StyleRange style = new StyleRange();
        style.start = offset;
        style.length = insertionPlaceholderLength();
        style.metrics = new GlyphMetrics(rect.height, 0, rect.width - 1); // hack: -1 
        styledText.setStyleRange(style);
    }
    
    public void removeAllInsertions() {
        for (int i = 0; i < styledText.getLineCount(); i++) {
            removeInsertion(i);
        }
    }
    
    private boolean removeInsertion(int lineIndex) {
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
    
    private int lineEndOffset(int lineIndex) {
        int lineOffset = styledText.getOffsetAtLine(lineIndex);
        int lineLength = styledText.getLine(lineIndex).length();
        return lineOffset + lineLength;
    }
    
    private Insertion addInsertion(int lineIndex) throws AssertionError {
        int offset = lineEndOffset(lineIndex);
        
        styledText.replaceTextRange(offset, 0, "\n" + insertionPlaceholder());
        offset++; // "\n"
        
        Insertion insertion = new Insertion(offset, "", this, styledText, settings);
        insertions.add(insertion);
        
        return insertion;
    }
    
    public boolean inInsertionLine() {
        return isInsertionLine(caretLine());
    }
    
    private boolean isInsertionLine(int lineIndex) {
        if (styledText.getLineCount() <= lineIndex)
            return false;
        return (styledText.getLine(lineIndex).equals(insertionPlaceholder()));
    }
    
    public boolean inLastLine() {
        return caretLine() == styledText.getLineCount() - 1;
    }
    
    public boolean lineHasInsertion() {
        //? replace lineHasInsertion with "a"
        boolean a = isInsertionLine(caretLine() + 1);
        boolean b = lineHasInsertion(caretLine());
        if (a != b)
            throw new AssertionError("a must be equal to b");
        return a;
    }
    
    public void selectInsertionLineEnd() {
        if (!lineHasInsertion())
            throw new AssertionError("Selected line must have an insertion.");
        
        int offset = lineEndOffset(caretLine() + 1);
        styledText.setSelection(offset, offset);
    }
    
    public void lineUp() {
        styledText.invokeAction(ST.LINE_UP);
    }
    
    public void lineDown() {
        styledText.invokeAction(ST.LINE_DOWN);
    }
    
}
