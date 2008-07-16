package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.ide.worksheet.controller.WorksheetController;
import com.yoursway.ide.worksheet.viewmodel.IUserSettings;
import com.yoursway.ide.worksheet.viewmodel.UserSettingsMock;

public class Worksheet {
    
    private final IUserSettings settings;
    private final WorksheetController controller;
    
    private final Shell shell;
    private final ExtendedText extendedText;
    
    public Worksheet(final IUserSettings settings) {
        this.settings = settings;
        controller = new WorksheetController(this, settings);
        
        shell = new Shell(settings.display());
        shell.setText(settings.worksheetTitle());
        shell.setBounds(settings.worksheetBounds());
        shell.setLayout(new FillLayout());
        
        extendedText = new ExtendedText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP); //> SWT.WRAP or SWT.H_SCROLL switching
        extendedText.setFont(settings.workspaceFont());
        
        extendedText.addVerifyKeyListener(controller);
        extendedText.addKeyListener(controller);
        extendedText.addExtendedModifyListener(controller);
        extendedText.addMouseListener(controller);
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
    
    public String command() {
        if (multilineSelection())
            return multilineCommand();
        else
            return command(extendedText.caretLine());
    }
    
    private String command(int lineIndex) {
        return extendedText.getLine(lineIndex);
    }
    
    private boolean multilineSelection() {
        Point lines = extendedText.selectedLines();
        return lines.x != lines.y;
    }
    
    private String multilineCommand() {
        Point lines = extendedText.selectedLines();
        StringBuilder multicommand = new StringBuilder();
        for (int i = lines.x; i <= lines.y; i++) {
            if (extendedText.isInsertionLine(i))
                continue;
            
            multicommand.append(command(i));
            if (i < lines.y)
                multicommand.append('\n');
        }
        return multicommand.toString();
    }
    
    public Insertion insertion() {
        return insertion(extendedText.selectedLines().y);
    }
    
    private Insertion insertion(int lineIndex) {
        if (extendedText.lineHasInsertion(lineIndex))
            return extendedText.existingInsertion(lineIndex);
        else
            return addInsertion(lineIndex);
    }
    
    /**
     * @deprecated Use {@link
     *  com.yoursway.ide.worksheet.view.ExtendedText#lineHasInsertion
     *  (com.yoursway.ide.worksheet.view.Worksheet)} instead
     */
    @Deprecated
    public boolean lineHasInsertion() {
        return extendedText.lineHasInsertion();
    }
    
    private Insertion addInsertion(int lineIndex) {
        Insertion insertion = new Insertion("", extendedText, settings);
        extendedText.addInsertion(lineIndex, insertion);
        return insertion;
    }
    
    public boolean removeInsertion() {
        return extendedText.removeInsertion(extendedText.caretLine());
    }
    
    public void removePrevLineInserionIfExists() {
        int prevLine = extendedText.caretLine() - 2;
        if (prevLine >= 0) {
            if (extendedText.lineHasInsertion(prevLine))
                extendedText.removeInsertion(prevLine);
        }
    }
    
    public void removeAllInsertions() {
        for (int i = 0; i < extendedText.getLineCount(); i++) {
            extendedText.removeInsertion(i);
        }
    }
    
    /**
     * @deprecated Use {@link
     *  com.yoursway.ide.worksheet.view.ExtendedText#inInsertionLine()} instead
     */
    @Deprecated
    public boolean inInsertionLine() {
        return extendedText.inInsertionLine();
    }
    
    /**
     * @deprecated Use {@link
     *  com.yoursway.ide.worksheet.view.ExtendedText#inLastLine()} instead
     */
    @Deprecated
    public boolean inLastLine() {
        return extendedText.inLastLine();
    }
    
    /**
     * @deprecated Use {@link
     *  com.yoursway.ide.worksheet.view.ExtendedText#selectInsertionLineEnd
     *  (com.yoursway.ide.worksheet.view.Worksheet)} instead
     */
    @Deprecated
    public void selectInsertionLineEnd() {
        extendedText.selectInsertionLineEnd(this);
    }
    
    public void makeNewLineAtEnd() {
        extendedText.append("\n");
        extendedText.setSelection(extendedText.getCharCount());
    }
    
    public void makeInsertionsObsolete(int start, int end) {
        int firstLine = extendedText.getLineAtOffset(start);
        int lastLine = extendedText.getLineAtOffset(end);
        for (int i = firstLine; i <= lastLine; i++) {
            if (extendedText.lineHasInsertion(i))
                extendedText.existingInsertion(i).becomeObsolete();
        }
    }
    
    /**
     * @deprecated Use {@link
     *  com.yoursway.ide.worksheet.view.ExtendedText#atLineBegin()} instead
     */
    @Deprecated
    public boolean atLineBegin() {
        return extendedText.atLineBegin();
    }
    
    /**
     * @deprecated Use {@link
     *  com.yoursway.ide.worksheet.view.ExtendedText#atLineEnd ()} instead
     */
    @Deprecated
    public boolean atLineEnd() {
        return extendedText.atLineEnd();
    }
    
    public boolean lineEmpty() {
        return extendedText.getLine(extendedText.caretLine()).length() == 0;
    }
    
    public void lineUp(boolean selection) {
        extendedText.invokeAction(selection ? ST.SELECT_LINE_UP : ST.LINE_UP);
    }
    
    public void lineDown(boolean selection) {
        extendedText.invokeAction(selection ? ST.SELECT_LINE_DOWN : ST.LINE_DOWN);
    }
    
    public void lineEnd(boolean selection) {
        extendedText.invokeAction(selection ? ST.SELECT_LINE_END : ST.LINE_END);
    }
    
    /**
     * @deprecated Use {@link
     *  com.yoursway.ide.worksheet.view.ExtendedText#moveCaretFromInsertionLine
     *  (boolean)} instead
     */
    @Deprecated
    public void moveCaretFromInsertionLine(boolean selection) {
        extendedText.moveCaretFromInsertionLine(selection);
    }
    
    public void showSelectedText() {
        MessageBox messageBox = new MessageBox(shell);
        messageBox.setMessage(extendedText.selectionWithoutInsertions());
        messageBox.open();
    }
    
}
