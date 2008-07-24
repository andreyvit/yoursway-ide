package com.yoursway.ide.worksheet.internal.view;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

import com.yoursway.ide.worksheet.WorksheetStyle;
import com.yoursway.ide.worksheet.internal.controller.Command;
import com.yoursway.ide.worksheet.internal.controller.ResultInsetProvider;
import com.yoursway.swt.styledtext.extended.ExtendedStyledText;
import com.yoursway.utils.annotations.UseFromUIThread;

@UseFromUIThread
public class WorksheetView {
    
    private final WorksheetStyle style;
    
    private final ExtendedStyledText extendedText;
    
    public WorksheetView(Composite parent, WorksheetViewCallback callback, WorksheetStyle style) {
        if (style == null)
            throw new NullPointerException("style is null");
        
        this.style = style;
        
        extendedText = new ExtendedStyledText(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL); //> SWT.WRAP or SWT.H_SCROLL switching
        extendedText.setFont(style.worksheetFont());
        
        extendedText.addVerifyKeyListener(callback);
        extendedText.addExtendedModifyListener(callback);
    }
    
    public boolean multilineSelection() {
        Point lines = extendedText.selectedLines();
        return lines.x != lines.y;
    }
    
    public Iterable<Command> selectedCommands() {
        List<Command> commands = new LinkedList<Command>();
        Point lines = extendedText.selectedLines();
        for (int i = lines.x; i <= lines.y; i++) {
            String commandText = commandText(i);
            if (commandText.trim().length() == 0)
                continue;
            
            final int j = i;
            commands.add(new Command(commandText, new ResultInsetProvider() {
                @UseFromUIThread
                public ResultInset get() {
                    return inset(j);
                }
            }));
        }
        return commands;
    }
    
    private String commandText(int lineIndex) {
        return extendedText.getLine(lineIndex);
    }
    
    @UseFromUIThread
    private ResultInset inset(int lineIndex) {
        if (extendedText.lineHasInset(lineIndex))
            return (ResultInset) extendedText.existingInset(lineIndex);
        else
            return addInset(lineIndex);
    }
    
    private ResultInset addInset(int lineIndex) {
        ResultInset inset = new ResultInset(style);
        extendedText.addInset(lineIndex, inset);
        return inset;
    }
    
    public void removeAllInsets() {
        for (int i = 0; i < extendedText.getLineCount(); i++) {
            extendedText.removeInset(i);
        }
    }
    
    public boolean inLastLine() {
        return extendedText.inLastLine();
    }
    
    public void makeNewLineAtEnd() {
        extendedText.append("\n");
        extendedText.setSelection(extendedText.getCharCount());
    }
    
    public void makeInsetsObsolete(int start, int end) {
        int firstLine = extendedText.getLineAtOffset(start);
        int lastLine = extendedText.getLineAtOffset(end);
        for (int i = firstLine; i <= lastLine; i++) {
            if (extendedText.lineHasInset(i)) {
                ResultInset inset = (ResultInset) extendedText.existingInset(i);
                inset.becomeObsolete();
            }
        }
    }
    
    @UseFromUIThread
    public void showSelectedText() {
        MessageBox messageBox = new MessageBox(extendedText.getShell());
        messageBox.setMessage(extendedText.getSelectionText());
        messageBox.open();
    }
    
    public void lineDown() {
        extendedText.lineDown();
    }
    
    public boolean isNewLineChar(int offset) {
        return extendedText.getText(offset, offset).equals("\n");
    }
    
}
