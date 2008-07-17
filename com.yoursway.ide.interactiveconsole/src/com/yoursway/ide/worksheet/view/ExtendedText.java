package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public class ExtendedText {
    
    ExtendedTextInternal internal; //? rename to ExtendedTextWidget widget
    
    public ExtendedText(Composite parent, int style) {
        internal = new ExtendedTextInternal(parent, style);
    }
    
    //! internal
    @Deprecated
    public void addExtendedModifyListener(ExtendedModifyListener extendedModifyListener) {
        internal.addExtendedModifyListener(extendedModifyListener);
    }
    
    //! internal
    @Deprecated
    public void addVerifyKeyListener(VerifyKeyListener listener) {
        internal.addVerifyKeyListener(listener);
    }
    
    int lineIndexToExternal(int internalLineIndex) {
        return internalLineIndex - insertionLinesAbove(internalLineIndex, false);
    }
    
    int lineIndexToInternal(int externalLineIndex) {
        int internalLineIndex = externalLineIndex + insertionLinesAbove(externalLineIndex, true);
        if (internal.isInsertionLine(internalLineIndex))
            throw new AssertionError("External line can't be insertion line.");
        return internalLineIndex;
    }
    
    private int insertionLinesAbove(int lineIndex, boolean external) {
        int workingLineIndex = lineIndex;
        int count = 0;
        for (int i = 0; i <= workingLineIndex; i++) {
            if (internal.isInsertionLine(i)) {
                count++;
                if (external)
                    workingLineIndex++;
            }
        }
        return count;
    }
    
    public int caretLine() {
        return lineIndexToExternal(internal.caretLine());
    }
    
    public String getLine(int lineIndex) {
        return internal.getLine(lineIndexToInternal(lineIndex));
    }
    
    public Point selectedLines() {
        Point internalLines = internal.selectedLines();
        int x = lineIndexToExternal(internalLines.x);
        int y = lineIndexToExternal(internalLines.y);
        return new Point(x, y);
    }
    
    public void setFont(Font font) {
        internal.setFont(font);
    }
    
    //! internal
    @Deprecated
    public void addInsertion(int lineIndex, Insertion insertion) {
        internal.addInsertion(lineIndex, insertion);
    }
    
    public void append(String string) {
        internal.append(string);
    }
    
    //! internal
    @Deprecated
    public Insertion existingInsertion(int lineIndex) {
        return internal.existingInsertion(lineIndex);
    }
    
    //! internal
    @Deprecated
    public int getCharCount() {
        return internal.getCharCount();
    }
    
    //! internal
    @Deprecated
    public int getLineAtOffset(int offset) {
        return internal.getLineAtOffset(offset);
    }
    
    //! internal
    @Deprecated
    public boolean inLastLine() {
        return internal.inLastLine();
    }
    
    //! internal !!!
    @Deprecated
    public void invokeAction(int action) {
        internal.invokeAction(action);
    }
    
    //! internal
    @Deprecated
    public boolean lineHasInsertion(int lineIndex) {
        return internal.lineHasInsertion(lineIndex);
    }
    
    //! internal
    @Deprecated
    public boolean removeInsertion(int lineIndex) {
        return internal.removeInsertion(lineIndex);
    }
    
    @Deprecated
    void selectInsertionLineEnd() {
        internal.selectInsertionLineEnd();
    }
    
    //!
    @Deprecated
    String selectionWithoutInsertions() {
        return internal.selectionWithoutInsertions();
    }
    
    //! internal
    @Deprecated
    public void setSelection(int start) {
        internal.setSelection(start);
    }
    
    @Deprecated
    ExtendedTextInternal internal() {
        return internal;
    }
    
    //! internal
    @Deprecated
    public int getLineCount() {
        return internal.getLineCount();
    }
    
    public Color getBackground() {
        return internal.getBackground();
    }
    
    public Rectangle getClientArea() {
        return internal.getClientArea();
    }
    
    //! for Insertion
    @Deprecated
    public void updateMetrics(int offset, Rectangle rect) {
        internal.updateMetrics(offset, rect);
    }
    
}
