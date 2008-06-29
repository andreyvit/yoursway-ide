package com.yoursway.ide.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.ide.application.view.impl.ApplicationCommands;
import com.yoursway.ide.application.view.impl.CommandExecutor;
import com.yoursway.ide.application.view.impl.MenuBuilder;

public class ApplicationMenu {
    
    private final Menu menu;

    public ApplicationMenu(Display display, CommandExecutor target) {
        this(new Shell(display), target);
    }
    
    public ApplicationMenu(Shell shell, CommandExecutor target) {
        if (target == null)
            throw new NullPointerException("target is null");
        MenuBuilder builder = new MenuBuilder(shell, target);
        createFileMenu(builder.submenu("File", 'F'));
        menu = builder.getMenu();
    }
    
    void createFileMenu(final MenuBuilder builder) {
        builder.item("New", SWT.MOD1 + 'N', new ApplicationCommands.NewDocumentCommand());
        builder.item("New Project", SWT.MOD1 + SWT.SHIFT + 'N', new ApplicationCommands.NewProjectCommand());
        builder.item("Open Project...", SWT.MOD1 + 'O', new ApplicationCommands.OpenProjectCommand());
        builder.separator();
        builder.item("Close File", SWT.MOD1 + 'W', new ApplicationCommands.CloseDocumentCommand());
        builder.item("Close Project", SWT.MOD1 + SWT.SHIFT + 'W', new ApplicationCommands.CloseProjectCommand());
        builder.item("Save As...", SWT.MOD1 + SWT.SHIFT + 'S', new ApplicationCommands.SaveFileAsCommand());
    }

    public Menu getMenu() {
        return menu;
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
