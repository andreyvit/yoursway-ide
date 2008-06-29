package com.yoursway.ide.application.view.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

public class ApplicationMenu {
    
    private final ApplicationCommands commands;
    
    public final Menu menu;

    public ApplicationMenu(Display display, CommandExecutor target, ApplicationCommands commands) {
        this(new Shell(display), target, commands);
    }
    
    public ApplicationMenu(Shell shell, CommandExecutor target, ApplicationCommands commands) {
        if (target == null)
            throw new NullPointerException("target is null");
        if (commands == null)
            throw new NullPointerException("commands is null");
        this.commands = commands;
        MenuBuilder builder = new MenuBuilder(shell, target);
        createFileMenu(builder.submenu("File", 'F'));
        menu = builder.getMenu();
    }
    
    void createFileMenu(final MenuBuilder builder) {
        builder.item("New", SWT.MOD1 + 'N', commands.newFile);
        builder.item("Open Project...", SWT.MOD1 + 'O', commands.openProject);
        builder.separator();
        builder.item("Close File", SWT.MOD1 + 'W', commands.closeFile);
        builder.item("Close Project", SWT.MOD1 + SWT.SHIFT + 'W', commands.closeProject);
        builder.item("Save As...", SWT.MOD1 + SWT.SHIFT + 'S', commands.saveFile);
    }
    
//  Menu createEditMenu(Shell shell) {
//      Menu menu = new Menu(shell, SWT.DROP_DOWN);
//      
//      MenuBuilder builder = new MenuBuilder(menu);
//      
//      builder.item("Undo", SWT.MOD1 + 'Z', new Runnable() {
//          public void run() {
//              Control focusControl = Display.getCurrent().getFocusControl();
//              if (focusControl instanceof StyledText) {
//                  CorchyViewer viewer = CorchyViewer.fromControl((StyledText) focusControl);
//                  if (viewer != null)
//                      viewer.getUndoManager().undo();
//              }
//          }
//      });
//      builder.item("Redo", SWT.MOD1 + SWT.SHIFT + 'Y', new Runnable() {
//          public void run() {
//              Control focusControl = Display.getCurrent().getFocusControl();
//              if (focusControl instanceof StyledText) {
//                  CorchyViewer viewer = CorchyViewer.fromControl((StyledText) focusControl);
//                  if (viewer != null)
//                      viewer.getUndoManager().redo();
//              }
//          }
//      });
//      builder.separator();
//      builder.item("Cut", SWT.MOD1 + 'X', new Runnable() {
//          public void run() {
//              Control focusControl = Display.getCurrent().getFocusControl();
//              if (focusControl instanceof StyledText) {
//                  ((StyledText) focusControl).cut();
//              } else if (focusControl instanceof Text) {
//                  ((Text) focusControl).cut();
//              }
//          }
//      });
//      builder.item("Copy", SWT.MOD1 + 'C', new Runnable() {
//          public void run() {
//              Control focusControl = Display.getCurrent().getFocusControl();
//              if (focusControl instanceof StyledText) {
//                  ((StyledText) focusControl).copy();
//              } else if (focusControl instanceof Text) {
//                  ((Text) focusControl).copy();
//              }
//          }
//      });
//      builder.item("Paste", SWT.MOD1 + 'V', new Runnable() {
//          public void run() {
//              Control focusControl = Display.getCurrent().getFocusControl();
//              if (focusControl instanceof StyledText) {
//                  ((StyledText) focusControl).paste();
//              } else if (focusControl instanceof Text) {
//                  ((Text) focusControl).paste();
//              }
//          }
//      });
//      builder.item("Select All", SWT.MOD1 + 'A', new Runnable() {
//          public void run() {
//              Control focusControl = Display.getCurrent().getFocusControl();
//              if (focusControl instanceof StyledText) {
//                  ((StyledText) focusControl).selectAll();
//              }
//              if (focusControl instanceof Text) {
//                  ((Text) focusControl).selectAll();
//              }
//          }
//      });
//      
//      builder.separator();
//      
//      builder.cascade("Find", -1, createFindMenu(shell));
//      
//      return menu;
//  }
//  
//  private Menu createFindMenu(Shell shell) {
//      Menu menu = new Menu(shell, SWT.DROP_DOWN);
//      
//      MenuBuilder builder = new MenuBuilder(menu);
//      
//      builder.item("Document Search", SWT.MOD1 + SWT.MOD3 + 'F', new Runnable() {
//          public void run() {
//              activeWindow.switchFocusToSearch();
//          }
//      });
//      builder.separator();
//      builder.item("Find Next", SWT.MOD1 + 'G', new Runnable() {
//          public void run() {
//              activeWindow.findNext();
//          }
//      });
//      builder.item("Find Previous", SWT.MOD1 + SWT.SHIFT + 'G', new Runnable() {
//          public void run() {
//              activeWindow.findPrevious();
//          }
//      });
//      return menu;
//  }
    
}
