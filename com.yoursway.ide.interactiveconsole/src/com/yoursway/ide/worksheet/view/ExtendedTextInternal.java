package com.yoursway.ide.worksheet.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
import org.eclipse.swt.widgets.Composite;

import com.yoursway.ide.worksheet.controller.ExtendedTextController;

public class ExtendedTextInternal extends StyledText {
    
    //private final List<Insertion> insertions = new LinkedList<Insertion>();
    private final Map<Insertion, Integer> insertions = new HashMap<Insertion, Integer>();
    
    public ExtendedTextInternal(Composite parent, int style) {
        super(parent, style);
        
        addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                int start = e.start;
                int replaceCharCount = e.end - e.start;
                int newCharCount = e.text.length();
                
                Iterator<Entry<Insertion, Integer>> it = insertions.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<Insertion, Integer> entry = it.next();
                    Insertion insertion = entry.getKey();
                    int offset = entry.getValue();
                    
                    if (start <= offset && offset < start + replaceCharCount) {
                        insertion.dispose();
                        it.remove();
                    } else if (offset >= start) {
                        offset += newCharCount - replaceCharCount;
                        entry.setValue(offset);
                    }
                }
            }
        });
        addPaintObjectListener(new PaintObjectListener() {
            public void paintObject(PaintObjectEvent e) {
                int offset = e.style.start;
                
                for (Entry<Insertion, Integer> entry : insertions.entrySet()) {
                    if (offset == entry.getValue()) {
                        entry.getKey().updateLocation(e);
                        break;
                    }
                }
            }
        });
        addControlListener(new ControlListener() {
            public void controlMoved(ControlEvent e) {
                // nothing
            }
            
            public void controlResized(ControlEvent e) {
                for (Insertion insertion : insertions.keySet()) {
                    insertion.updateSize();
                }
                redraw();
            }
        });
        
        ExtendedTextController controller = new ExtendedTextController(this);
        addVerifyKeyListener(controller);
        addKeyListener(controller);
        addMouseListener(controller);
    }
    
    String insertionPlaceholder() {
        return "\uFFFC";
    }
    
    private int insertionPlaceholderLength() throws AssertionError {
        if (insertionPlaceholder().length() != 1)
            throw new AssertionError("An insertion placeholder must have 1 char length.");
        return 1;
    }
    
    public void addInsertion(int lineIndex, final Insertion insertion) {
        int offset = lineEndOffset(lineIndex);
        replaceTextRange(offset, 0, "\n" + insertionPlaceholder());
        offset++; // "\n"
        insertions.put(insertion, offset);
        insertion.createWidget(this, new ResizingListener() {
            public void resized(Point size) {
                updateMetrics(insertion, size);
            }
        });
    }
    
    public int lineEndOffset(int lineIndex) {
        int lineOffset = getOffsetAtLine(lineIndex);
        int lineLength = getLine(lineIndex).length();
        return lineOffset + lineLength;
    }
    
    public Insertion existingInsertion(int lineIndex) {
        int offset = lineEndOffset(lineIndex) + 1;
        for (Entry<Insertion, Integer> entry : insertions.entrySet()) {
            if (entry.getValue() == offset)
                return entry.getKey();
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
    
    private void updateMetrics(Insertion insertion, Point size) {
        StyleRange style = new StyleRange();
        style.start = insertions.get(insertion);
        style.length = insertionPlaceholderLength();
        int width = size.x - (size.x > 20 ? 20 : 0); // hack
        style.metrics = new GlyphMetrics(size.y, 0, width);
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
    
    //! for controller
    public boolean atLineBegin() {
        int offset = getOffsetAtLine(caretLine());
        return getCaretOffset() == offset;
    }
    
    //! for controller
    public boolean inLastLine() {
        return caretLine() == getLineCount() - 1;
    }
    
}
