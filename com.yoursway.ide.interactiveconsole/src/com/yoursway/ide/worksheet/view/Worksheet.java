package com.yoursway.ide.worksheet.view;

import org.eclipse.swt.SWT;
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
    
    private final Shell shell;
    private final ExtendedText extendedText;
    
    public Worksheet(final IUserSettings settings) {
        this.settings = settings;
        
        shell = new Shell(settings.display());
        shell.setText(settings.worksheetTitle());
        shell.setBounds(settings.worksheetBounds());
        shell.setLayout(new FillLayout());
        
        extendedText = new ExtendedText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP); //> SWT.WRAP or SWT.H_SCROLL switching
        extendedText.setFont(settings.workspaceFont());
        
        WorksheetController controller = new WorksheetController(this, settings);
        extendedText.addVerifyKeyListener(controller);
        extendedText.addExtendedModifyListener(controller);
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
            multicommand.append(command(i));
            if (i < lines.y)
                multicommand.append('\n');
        }
        return multicommand.toString();
    }
    
    public ResultInsertion insertion() {
        return insertion(extendedText.selectedLines().y);
    }
    
    private ResultInsertion insertion(int lineIndex) {
        if (extendedText.lineHasInsertion(lineIndex))
            return (ResultInsertion) extendedText.existingInsertion(lineIndex);
        else
            return addInsertion(lineIndex);
    }
    
    private ResultInsertion addInsertion(int lineIndex) {
        ResultInsertion insertion = new ResultInsertion(settings, extendedText);
        extendedText.addInsertion(lineIndex, insertion);
        return insertion;
    }
    
    public void removeAllInsertions() {
        for (int i = 0; i < extendedText.getLineCount(); i++) {
            extendedText.removeInsertion(i);
        }
    }
    
    public boolean inLastLine() {
        return extendedText.inLastLine();
    }
    
    public void makeNewLineAtEnd() {
        extendedText.append("\n");
        extendedText.setSelection(extendedText.getCharCount());
    }
    
    public void makeInsertionsObsolete(int start, int end) {
        int firstLine = extendedText.getLineAtOffset(start);
        int lastLine = extendedText.getLineAtOffset(end);
        for (int i = firstLine; i <= lastLine; i++) {
            if (extendedText.lineHasInsertion(i)) {
                ResultInsertion insertion = (ResultInsertion) extendedText.existingInsertion(i);
                insertion.becomeObsolete();
            }
        }
    }
    
    public void showSelectedText() {
        MessageBox messageBox = new MessageBox(shell);
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
