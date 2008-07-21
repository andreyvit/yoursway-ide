package com.yoursway.ide.worksheet.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
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
    
    private final Collection<Insertion> insertions = new LinkedList<Insertion>();
    
    private final Collection<ResizeListener> resizeListeners = new LinkedList<ResizeListener>();
    
    public ExtendedTextInternal(Composite parent, int style) {
        super(parent, style);
        
        addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                int start = e.start;
                int replaceCharCount = e.end - e.start;
                int newCharCount = e.text.length();
                
                Iterator<Insertion> it = insertions.iterator();
                while (it.hasNext()) {
                    Insertion insertion = it.next();
                    int offset = insertion.offset();
                    
                    if (start <= offset && offset < start + replaceCharCount) {
                        insertion.dispose();
                        it.remove();
                    } else if (offset >= start) {
                        offset += newCharCount - replaceCharCount;
                        insertion.offset(offset);
                    }
                }
            }
        });
        addPaintObjectListener(new PaintObjectListener() {
            public void paintObject(PaintObjectEvent e) {
                int offset = e.style.start;
                
                for (Insertion insertion : insertions) {
                    if (offset == insertion.offset()) {
                        insertion.updateLocation();
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
                for (ResizeListener listener : resizeListeners) {
                    listener.resized(getSize());
                }
                redraw(); //?
            }
        });
        
        ExtendedTextController controller = new ExtendedTextController(this);
        addVerifyKeyListener(controller);
        addKeyListener(controller);
        addMouseListener(controller);
    }
    
    @Override
    public void scroll(int destX, int destY, int x, int y, int width, int height, boolean all) {
        super.scroll(destX, destY, x, y, width, height, false);
        
        int deltaX = destX - x;
        int deltaY = destY - y;
        
        for (Insertion insertion : insertions) {
            Rectangle rect = insertion.getBounds();
            if (rect.intersects(x, y, width, height) || rect.intersects(destX, destY, width, height)) {
                insertion.setLocation(rect.x + deltaX, rect.y + deltaY);
            }
        }
        // other children scrolling are not supported
    }
    
    String insertionPlaceholder() {
        return "\uFFFC";
    }
    
    private int insertionPlaceholderLength() throws AssertionError {
        if (insertionPlaceholder().length() != 1)
            throw new AssertionError("An insertion placeholder must have 1 char length.");
        return 1;
    }
    
    public void addInsertion(int lineIndex, final InsertionContent content) {
        int offset = lineEndOffset(lineIndex);
        replaceTextRange(offset, 0, "\n" + insertionPlaceholder());
        offset++; // "\n"
        final Composite composite = new Composite(this, SWT.NO_FOCUS | SWT.NO_BACKGROUND);
        
        final Insertion insertion = new Insertion(content, offset, composite, this);
        insertions.add(insertion);
        
        composite.addControlListener(new ControlListener() {
            public void controlMoved(ControlEvent e) {
                // nothing
            }
            
            public void controlResized(ControlEvent e) {
                updateMetrics(insertion.offset(), composite.getSize());
            }
        });
        
        content.init(composite, new ListenersAcceptor() {
            public void addResizeListener(ResizeListener listener) {
                resizeListeners.add(listener);
            }
        });
    }
    
    public int lineEndOffset(int lineIndex) {
        int lineOffset = getOffsetAtLine(lineIndex);
        int lineLength = getLine(lineIndex).length();
        return lineOffset + lineLength;
    }
    
    public InsertionContent existingInsertion(int lineIndex) {
        int offset = lineEndOffset(lineIndex) + 1;
        for (Insertion insertion : insertions) {
            if (insertion.offset() == offset)
                return insertion.content();
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
    
    private void updateMetrics(int offset, Point size) {
        StyleRange style = new StyleRange();
        style.start = offset;
        style.length = insertionPlaceholderLength();
        int width = size.x - (size.x > 20 ? 20 : 0); // hack
        style.metrics = new GlyphMetrics(size.y, 0, width);
        setStyleRange(style);
        
        showSelection(); //? when
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
