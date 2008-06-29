package com.yoursway.ide.application.view.impl;

import static com.yoursway.swt.additions.FormDataBuilder.formDataOf;
import static com.yoursway.swt.additions.YsSwtUtils.centerShellOnNearestMonitor;

import org.eclipse.jface.internal.databinding.provisional.swt.ControlUpdater;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.yoursway.ide.application.view.View;
import com.yoursway.ide.application.view.ViewCallback;
import com.yoursway.ide.application.view.ViewDefinition;
import com.yoursway.ide.application.view.ViewDefinitionFactory;
import com.yoursway.ide.application.view.mainwindow.EditorWindow;
import com.yoursway.ide.application.view.mainwindow.EditorWindowCallback;
import com.yoursway.ide.application.view.mainwindow.EditorWindowModel;
import com.yoursway.ide.application.view.mainwindow.MainWindow;
import com.yoursway.ide.application.view.mainwindow.MainWindowArea;
import com.yoursway.ide.application.view.mainwindow.MainWindowAreas;
import com.yoursway.ide.application.view.mainwindow.MainWindowCallback;
import com.yoursway.ide.application.view.mainwindow.MainWindowModel;
import com.yoursway.ide.application.view.mainwindow.MainWindowViewAreaVisitor;
import com.yoursway.swt.additions.YsStandardFonts;

public class MainWindowImpl implements MainWindow {
    
    private final MainWindowCallback callback;
    private final MainWindowModel windowModel;
    private Shell shell;
    private Composite projectComposite;
    private CTabFolder tabFolder;
    private final ViewDefinitionFactory viewDefinitions;
    
    public MainWindowImpl(Display display, final MainWindowModel windowModel, MainWindowCallback callback,
            ViewDefinitionFactory viewDefinitions, ApplicationMenuFactory menuFactory) {
        if (display == null)
            throw new NullPointerException("display is null");
        if (windowModel == null)
            throw new NullPointerException("windowModel is null");
        if (callback == null)
            throw new NullPointerException("callback is null");
        if (viewDefinitions == null)
            throw new NullPointerException("viewDefinitions is null");
        this.windowModel = windowModel;
        this.callback = callback;
        this.viewDefinitions = viewDefinitions;
        
        shell = new Shell(display);
        shell.setData(this);
        shell.setLayout(new FormLayout());
        
        if (menuFactory != null)
            shell.setMenuBar(menuFactory.createMenuFor(shell, callback));
        
        projectComposite = new Composite(shell, SWT.NONE);
        formDataOf(projectComposite).left(0).right(0, 170).top(0).bottom(100);
        
        tabFolder = new CTabFolder(shell, SWT.TOP | SWT.CLOSE);
        //        tabFolder = new TabFolder(shell, SWT.TOP);
        formDataOf(tabFolder).left(projectComposite).right(100).top(0).bottom(100);
        tabFolder.setTabHeight(16); // 16px should be enough for everyone
        tabFolder.setFont(YsStandardFonts.miniFont());
        tabFolder.setMRUVisible(false); // no-no-no, David Blane, no-no-no
        tabFolder.setSimple(true); // no fancy space-eating curves
        
        hookEverything(windowModel);
        
        // TODO: remember which monitor the window was in, and reopen it there
        Rectangle bounds = display.getPrimaryMonitor().getBounds();
        shell.setSize(bounds.width * 2 / 3, bounds.height * 2 / 3);
    }

    private void hookEverything(final MainWindowModel windowModel) {
        new ControlUpdater(shell) {
            protected void updateControl() {
                shell.setText(windowModel.projectLocation().getValue() + " - "
                        + windowModel.projectType().getValue().getDescriptiveName());
                shell.layout();
            }
        };
        shell.addShellListener(new ShellAdapter(){
            public void shellActivated(ShellEvent e) {
                callback.activated();
            }
            public void shellDeactivated(ShellEvent e) {
                callback.deactivated();
            }
        });
        shell.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                callback.windowDisposed();
            }
        });
    }
    
    public MainWindowAreas definition() {
        return null;
    }
    
    public View bindView(ViewDefinition definition, final ViewCallback callback) {
        class Visitor implements MainWindowViewAreaVisitor {
            
            public View result = null;

            public void visitGeneralArea() {
            }

            public void visitProjectViewArea() {
                result = new CompositeView(projectComposite, callback);
            }
            
        };
        
        MainWindowArea area = (MainWindowArea) definition.area();
        Visitor visitor = new Visitor();
        area.accept(visitor);
        return visitor.result;
    }

    public void open() {
        centerShellOnNearestMonitor(shell);
        shell.open();
    }

    public EditorWindow createEditorWindow(EditorWindowModel model, EditorWindowCallback callback) {
        return new EditorWindowImpl(model, callback, tabFolder);
    }

    public void forceClose() {
        shell.dispose();
    }
    
}
