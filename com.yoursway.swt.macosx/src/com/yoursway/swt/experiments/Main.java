package com.yoursway.swt.experiments;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Drawer;
import org.eclipse.swt.widgets.Shell;

public class Main {
    
    private static Display display;
    private static Shell shell;
    private static Shell subshell;

    public static void main(String[] args) {
        display = new Display();
        shell = new Shell(display);
        shell.setLayout(new GridLayout());
        Button button = new Button(shell, SWT.NONE);
        button.setText("Cool!");
        button.addSelectionListener(new SelectionAdapter() {
           

            @Override
            public void widgetSelected(SelectionEvent e) {
                subshell = new Drawer(shell);
                subshell.setLayout(new GridLayout());
                subshell.setSize(100, 50);
                Button button = new Button(subshell, SWT.NONE);
                button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
                button.setText("Subshell");
                subshell.open();
            }
            
        });
        button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        shell.open();
        while (!shell.isDisposed())
            if (!display.readAndDispatch())
                display.sleep();
        display.dispose();
    }
    
}
