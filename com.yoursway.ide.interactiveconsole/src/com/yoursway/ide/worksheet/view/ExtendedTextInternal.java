package com.yoursway.ide.worksheet.view;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.ide.worksheet.controller.ExtendedTextController;

public class ExtendedTextInternal extends StyledText {
    
    private final List<Insertion> insertions = new LinkedList<Insertion>();
    
    public ExtendedTextInternal(Composite parent, int style) {
        super(parent, style);
        
        addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                for (Iterator<Insertion> it = insertions.iterator(); it.hasNext();) {
                    Insertion insertion = it.next();
                    
                    insertion.updateOffset(e);
                    
                    if (insertion.disposed())
                        it.remove();
                }
            }
        });
        addPaintObjectListener(new PaintObjectListener() {
            public void paintObject(PaintObjectEvent e) {
                for (Insertion insertion : insertions) {
                    insertion.updateLocation(e);
                }
            }
        });
        addControlListener(new ControlListener() {
            public void controlMoved(ControlEvent e) {
                // nothing
            }
            
            public void controlResized(ControlEvent e) {
                for (Insertion insertion : insertions) {
                    insertion.updateSize();
                }
            }
        });
        
        ExtendedTextController controller = new ExtendedTextController(this);
        addVerifyKeyListener(controller);
        addKeyListener(controller);
        addMouseListener(controller);
    }
    
    private String insertionPlaceholder() {
        return "\uFFFC";
    }
    
    private int insertionPlaceholderLength() throws AssertionError {
        if (insertionPlaceholder().length() != 1)
            throw new AssertionError("An insertion placeholder must have 1 char length.");
        return 1;
    }
    
    public void addInsertion(int lineIndex, Insertion insertion) {
        int offset = lineEndOffset(lineIndex);
        
        replaceTextRange(offset, 0, "\n" + insertionPlaceholder());
        offset++; // "\n"
        
        insertion.offset(offset);
        insertions.add(insertion);
    }
    
    public int lineEndOffset(int lineIndex) {
        int lineOffset = getOffsetAtLine(lineIndex);
        int lineLength = getLine(lineIndex).length();
        return lineOffset + lineLength;
    }
    
    public Insertion existingInsertion(int lineIndex) {
        int offset = lineEndOffset(lineIndex) + 1;
        for (Insertion insertion : insertions) {
            if (insertion.offset() == offset)
                return insertion;
        }
        return null;
    }
    
    public boolean removeInsertion(int lineIndex) {
        if (!lineHasInsertion(lineIndex))
            return false;
        
        int s = insertions.size();
        
        int offset = lineEndOffset(lineIndex);
        //! must be 2 == ("\n" + insertionPlaceholder()).length()
        replaceTextRange(offset, 2, "");
        
        if (insertions.size() != s - 1)
            throw new AssertionError("Insertion object hasn't been removed from collection.");
        
        return true;
    }
    
    //! for controller
    public boolean lineHasInsertion() {
        return lineHasInsertion(selectedLines().y);
    }
    
    public boolean lineHasInsertion(int lineIndex) {
        return isInsertionLine(lineIndex + 1);
    }
    
    //!
    public boolean isInsertionLine(int lineIndex) {
        if (getLineCount() <= lineIndex)
            return false;
        return (getLine(lineIndex).equals(insertionPlaceholder()));
    }
    
    //!
    public String selectionWithoutInsertions() {
        String text = getSelectionText();
        return text.replace("\n" + insertionPlaceholder(), "");
    }
    
    public void updateMetrics(int offset, Rectangle rect) { //!
        StyleRange style = new StyleRange();
        style.start = offset;
        style.length = insertionPlaceholderLength();
        style.metrics = new GlyphMetrics(rect.height, 0, rect.width - 1); // hack: -1 
        setStyleRange(style);
    }
    
    //! for controller
    public void selectInsertionLineEnd() {
        if (!lineHasInsertion())
            throw new AssertionError("Selected line must have an insertion.");
        
        int offset = lineEndOffset(selectedLines().y + 1);
        setSelection(offset);
    }
    
    //?
    public Point selectedLines() {
        Point sel = getSelection();
        int firstLine = getLineAtOffset(sel.x);
        int lastLine = getLineAtOffset(sel.y);
        if (firstLine > lastLine)
            throw new AssertionError("First line of selection must be <= than last.");
        return new Point(firstLine, lastLine);
    }
    
    public boolean inInsertionLine() {
        return isInsertionLine(caretLine());
    }
    
    public void moveCaretFromInsertionLine(boolean selection) {
        if (inInsertionLine())
            moveCaret(selection, atLineBegin() ? -1 : inLastLine() ? -2 : 1);
    }
    
    //?
    public int caretLine() {
        return getLineAtOffset(getCaretOffset());
    }
    
    private void moveCaret(boolean selection, int where) {
        if (selection) {
            Point sel = getSelection();
            if (caretAtSelectionEnd())
                setSelection(sel.x, sel.y + where);
            else
                setSelection(sel.x + where, sel.y);
        } else {
            setCaretOffset(getCaretOffset() + where);
        }
    }
    
    private boolean caretAtSelectionEnd() {
        return getCaretOffset() == getSelection().y;
    }
    
    //!
    public boolean atLineBegin() {
        int offset = getOffsetAtLine(caretLine());
        return getCaretOffset() == offset;
    }
    
    //!
    public boolean inLastLine() {
        return caretLine() == getLineCount() - 1;
    }
    
}
