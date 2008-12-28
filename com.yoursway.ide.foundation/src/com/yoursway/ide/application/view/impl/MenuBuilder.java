package com.yoursway.ide.application.view.impl;

import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.bindings.keys.formatting.KeyFormatterFactory;
import org.eclipse.jface.bindings.keys.formatting.NativeKeyFormatter;
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
	
	static {
        KeyFormatterFactory.setDefault(new NativeKeyFormatter());
	}

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

	public MenuBuilder submenu(String title) {
		return submenu(title, null);
	}

	public MenuBuilder submenu(String title, String accel) {
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

	public MenuItem item(String title, String accel, final Command command) {
		MenuItem item = createItem(title, accel, SWT.PUSH);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				target.execute(command);
			}
		});
		return item;
	}

	public MenuItem item(String title, Command command) {
		return item(title, null, command);
	}
	
	public MenuItem checkbox(String title, String accel, final Command command) {
		MenuItem item = createItem(title, accel, SWT.CHECK);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				target.execute(command);
			}
		});
		return item;
	}
	
	public MenuItem checkbox(String title, Command command) {
		return checkbox(title, null, command);
	}
	
	public MenuItem radio(String title, String accel, final Command command) {
		MenuItem item = createItem(title, accel, SWT.RADIO);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				target.execute(command);
			}
		});
		return item;
	}
	
	public MenuItem radio(String title, Command command) {
		return radio(title, null, command);
	}

	public void separator() {
		new MenuItem(menu, SWT.SEPARATOR);
	}

	private MenuItem createItem(String title, String accel, int style) {
		MenuItem item = new MenuItem(menu, style);
		if (accel != null)
			try {
				KeySequence seq = KeySequence.getInstance(accel);
				KeyStroke[] keyStrokes = seq.getKeyStrokes();
				if (keyStrokes.length == 1)
					item.setAccelerator(keyStrokes[0].getModifierKeys()
							| keyStrokes[0].getNaturalKey());
				else
					title = title + "\t" + seq.format();
			} catch (ParseException e) {
				throw new AssertionError("Invalid keystroke spec: " + accel
						+ " -- " + e.getMessage());
			}
			item.setText(title);
		return item;
	}

}
