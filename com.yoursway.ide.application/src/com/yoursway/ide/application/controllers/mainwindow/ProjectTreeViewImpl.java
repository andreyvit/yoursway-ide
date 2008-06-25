package com.yoursway.ide.application.controllers.mainwindow;

import static com.yoursway.swt.additions.YsSwtUtils.applyMiniSize;
import static com.yoursway.swt.additions.YsSwtUtils.applySmallSize;
import static com.yoursway.utils.YsFileUtils.isBogusFile;

import java.io.File;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.carbon.GDevice;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.yoursway.ide.application.model.Project;
import com.yoursway.ide.application.view.View;
import com.yoursway.ide.application.view.ViewPresentation;

public class ProjectTreeViewImpl implements ProjectTreeView {

    private Composite composite;
    private final ProjectTreeViewCallback callback;
    private Tree tree;
    private TreeViewer treeViewer;

    public ProjectTreeViewImpl(ViewPresentation presentation, ProjectTreeViewCallback callback) {
        if (callback == null)
            throw new NullPointerException("callback is null");
        
        this.composite = presentation.composite();
        this.callback = callback;
        createWidgets();
    }

    private void createWidgets() {
        composite.setLayout(GridLayoutFactory.fillDefaults().create());
        
        tree = new Tree(composite, SWT.NONE);
        applySmallSize(tree);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        composite.layout();
        
        TreeItem item = new TreeItem(tree, SWT.NONE);
        item.setText("Special street magic");
        treeViewer = new TreeViewer(tree);
        treeViewer.setLabelProvider(new FileLabelProvider());
        treeViewer.setContentProvider(new FileContentProvider());
        treeViewer.addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                File file = (File) element;
                return !isBogusFile(file.getName());
            }
            
        });
        treeViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                File file = (File) selection.getFirstElement();
                if (file != null) {
                    callback.openFile(file);
                }
            }
        });
    }

    public void show(Project project) {
        treeViewer.setInput(project.getLocation());
    }
    
}
