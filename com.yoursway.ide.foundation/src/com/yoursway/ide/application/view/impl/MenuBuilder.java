package com.yoursway.ide.application.view.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.ide.application.view.impl.commands.Command;

public class MenuBuilder {
    
    private final Menu menu;
    private final MenuItem parentItem;
    private final CommandExecutor target;
    
    public MenuBuilder(Shell shell, CommandExecutor target) {
        this.menu = new Menu(shell, SWT.BAR);
        this.parentItem = null;
        this.target = target;
    }
    
    public MenuBuilder(Menu submenu, MenuItem item, CommandExecutor target) {
        this.menu = submenu;
        this.parentItem = item;
        this.target = target;
    }
    
    public MenuBuilder submenu(String title, int accel) {
        MenuItem item = createItem(title, accel, SWT.CASCADE);
        Menu submenu = new Menu(item);
        item.setMenu(submenu);
        return new MenuBuilder(submenu, item, target);
    }
    
    public Menu getMenu() {
        return menu;
    }
    
    public MenuItem getParentItem() {
        return parentItem;
    }
    
    public MenuItem item(String title, int accel, final Command command) {
        MenuItem item = createItem(title, accel, SWT.PUSH);
        item.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                target.execute(command);
            }
        });
        return item;
    }
    
    public MenuItem item(String title, Command command) {
        return item(title, -1, command);
    }
    
    public void separator() {
        new MenuItem(menu, SWT.SEPARATOR);
    }
    
    private MenuItem createItem(String title, int accel, int style) {
        MenuItem item = new MenuItem(menu, style);
        item.setText(title);
        if (accel >= 0)
            item.setAccelerator(accel);
        return item;
    }
    
}
