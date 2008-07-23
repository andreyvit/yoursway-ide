package com.yoursway.ide.worksheet.demo;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.ide.worksheet.UserSettings;
import com.yoursway.ide.worksheet.Worksheet;

public class WorksheetDemo {

    public static void main(String[] args) {
        UserSettings settings = new UserSettingsMock();
        
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
    
}
