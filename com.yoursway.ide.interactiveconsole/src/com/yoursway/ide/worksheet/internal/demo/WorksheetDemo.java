package com.yoursway.ide.worksheet.internal.demo;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.ide.worksheet.Worksheet;
import com.yoursway.ide.worksheet.executors.standard.ExternalCommandExecutor;

public class WorksheetDemo {
    
    public static void main(String[] args) {
        Display display = new Display();
        
        Shell shell = new Shell(display);
        shell.setText("Worksheet");
        shell.setBounds(new Rectangle(240, 120, 640, 480));
        shell.setLayout(new FillLayout());
        
        ExternalCommandExecutor executor = new ExternalCommandExecutor("irb");
        
        Worksheet.create(shell, executor, new ShortcutsMock(), new StyleMock(display));
        
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
    
}
