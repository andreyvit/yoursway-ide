package com.yoursway.ide.ui.rubyeditor;

import org.eclipse.dltk.ruby.internal.ui.editor.RubyEditor;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.ide.ui.Activator;

public class HumaneRubyEditor extends RubyEditor {
    
    public static final String EDITOR_ID = "com.yoursway.ide.ui.rubyeditor";
    
    private final Color assistTextColor = new Color(null, 127, 127, 127);
    
    public String getEditorId() {
        return EDITOR_ID;
    }
    
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        final StyledText textWidget = getSourceViewer().getTextWidget();
        textWidget.addPaintObjectListener(new PaintObjectListener() {
            
            public void paintObject(PaintObjectEvent event) {
                System.out.println(".paintObject()");
            }
            
        });
        PaintListener paintListener = new PaintListener() {
            
            public void paintControl(PaintEvent event) {
                try {
                    System.out.println(".paintControl()");
                    int startLine = textWidget.getLineIndex(event.y);
                    int y = textWidget.getLinePixel(startLine);
                    int endY = event.y + event.height;
                    int lineHeight = textWidget.getLineHeight();
                    event.gc.setForeground(assistTextColor);
                    if (endY > 0) {
                        int lineCount = textWidget.getContent().getLineCount();
                        // int x = 0 /* leftMargin */-
                        // textWidget.getHorizontalPixel();
                        int charCount = textWidget.getCharCount();
                        for (int i = startLine; y < endY && i < lineCount; i++) {
                            int startOffset = textWidget.getOffsetAtLine(i);
                            if (startOffset >= charCount)
                                continue;
                            int endOffset;
                            if (i == lineCount - 1)
                                endOffset = charCount - 1;
                            else
                                endOffset = textWidget.getOffsetAtLine(i + 1) - 1;
                            Rectangle lineBounds = textWidget.getTextBounds(startOffset, endOffset);
                            event.gc.drawText("Cool!", lineBounds.x + lineBounds.width, y, true);
                            y += lineHeight;
                        }
                    }
                } catch (Throwable e) {
                    Activator.log(e);
                }
            }
            
        };
        textWidget.addPaintListener(paintListener);
    }
    
}
