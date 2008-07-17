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
    
    public void addExtendedModifyListener(ExtendedModifyListener extendedModifyListener) {
        internal.addExtendedModifyListener(extendedModifyListener);
    }
    
    public void addVerifyKeyListener(VerifyKeyListener listener) {
        internal.addVerifyKeyListener(listener);
    }
    
    public int caretLine() {
        return internal.caretLine();
    }
    
    public String getLine(int lineIndex) {
        return internal.getLine(lineIndex);
    }
    
    public Point selectedLines() {
        return internal.selectedLines();
    }
    
    public void setFont(Font font) {
        internal.setFont(font);
    }
    
    public void addInsertion(int lineIndex, Insertion insertion) {
        internal.addInsertion(lineIndex, insertion);
    }
    
    public void append(String string) {
        internal.append(string);
    }
    
    public Insertion existingInsertion(int lineIndex) {
        return internal.existingInsertion(lineIndex);
    }
    
    public int getCharCount() {
        return internal.getCharCount();
    }
    
    public int getLineAtOffset(int offset) {
        return internal.getLineAtOffset(offset);
    }
    
    public boolean inLastLine() {
        return internal.inLastLine();
    }
    
    public void invokeAction(int action) {
        internal.invokeAction(action);
    }
    
    public boolean isInsertionLine(int lineIndex) {
        return internal.isInsertionLine(lineIndex);
    }
    
    public boolean lineHasInsertion(int lineIndex) {
        return internal.lineHasInsertion(lineIndex);
    }
    
    public boolean removeInsertion(int lineIndex) {
        return internal.removeInsertion(lineIndex);
    }
    
    public void selectInsertionLineEnd() {
        internal.selectInsertionLineEnd();
    }
    
    public String selectionWithoutInsertions() {
        return internal.selectionWithoutInsertions();
    }
    
    public void setSelection(int start) {
        internal.setSelection(start);
    }
    
    //!
    ExtendedTextInternal internal() {
        return internal;
    }
    
    public int getLineCount() {
        return internal.getLineCount();
    }
    
    public Color getBackground() {
        return internal.getBackground();
    }
    
    public Rectangle getClientArea() {
        return internal.getClientArea();
    }
    
    public void updateMetrics(int offset, Rectangle rect) {
        internal.updateMetrics(offset, rect);
    }
    
}
