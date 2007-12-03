package com.yoursway.ide.ui.railsview.shit;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class ProjectPresentationProvider implements ITreeContentProvider, ILabelProvider, Listener {
    
    private final class InPlaceEditorListener implements KeyListener {
        private final Tree tree;
        private final TreeEditor editor;
        
        private InPlaceEditorListener(Tree tree, TreeEditor editor) {
            this.tree = tree;
            this.editor = editor;
        }
        
        public void magic(TreeItem item) {
            if (item == null)
                return;
            
            IPresentableItem presentableItem = extractPresentableItem(item);
            if (!(presentableItem instanceof IInPlaceRenameable))
                return;
            final IInPlaceRenameable renameable = (IInPlaceRenameable) presentableItem;
            
            // Clean up any previous editor control
            Control oldEditor = editor.getEditor();
            if (oldEditor != null)
                oldEditor.dispose();
            
            // The control that will be the editor must be a child of the Tree
            final Text newEditor = new Text(tree, SWT.NONE);
            newEditor.setText(renameable.getInitialName());
            newEditor.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    Text text = (Text) editor.getEditor();
                    renameable.setNewName(text.getText());
                }
            });
            newEditor.addKeyListener(new KeyListener() {
                
                public void keyPressed(KeyEvent e) {
                    Text text = (Text) editor.getEditor();
                    if (e.keyCode == '\r') {
                        renameable.setNewName(text.getText());
                        text.dispose();
                        tree.setFocus();
                    } else if (e.keyCode == SWT.ESC) {
                        text.dispose();
                        tree.setFocus();
                    }
                }
                
                public void keyReleased(KeyEvent e) {
                }
                
            });
            newEditor.addFocusListener(new FocusListener() {
                
                public void focusGained(FocusEvent e) {
                }
                
                public void focusLost(FocusEvent e) {
                    Text text = (Text) editor.getEditor();
                    text.dispose();
                }
                
            });
            
            newEditor.selectAll();
            newEditor.setFocus();
            editor.setEditor(newEditor, item);
        }
        
        public void keyPressed(KeyEvent e) {
            if (e.keyCode == '\r') {
                TreeItem[] selection = tree.getSelection();
                if (selection != null && selection.length == 1) {
                    magic(selection[0]);
                }
            }
        }
        
        public void keyReleased(KeyEvent e) {
        }
    }
    
    private final ISearchPatternProvider searchProvider;
    private boolean showAllResults;
    private TreeEditor editor;
    
    public ProjectPresentationProvider(ISearchPatternProvider searchProvider) {
        this.searchProvider = searchProvider;
        this.showAllResults = false;
    }
    
    public boolean isShowAllResults() {
        return showAllResults;
    }
    
    public void setShowAllResults(boolean showAllResults) {
        this.showAllResults = showAllResults;
    }
    
    public Object[] getChildren(Object parentElement) {
        if (!(parentElement instanceof IPresentableItem)) {
            return new Object[0];
        }
        IPresentableItem item = (IPresentableItem) parentElement;
        if (item instanceof ElementsCategory) {
            ElementsCategory elementsCategory = (ElementsCategory) item;
            if (elementsCategory.headerOnly())
                return new Object[0];
        }
        String pattern = searchProvider.getPattern();
        if (pattern == null || pattern.length() == 0) {
            Collection<IPresentableItem> children = item.getChildren();
            if (children == null)
                return null;
            return children.toArray(new IPresentableItem[children.size()]);
        } else {
            Collection<IPresentableItem> children = item.getChildren(); //BIGTODO
            if (children == null)
                return null;
            for (Iterator<IPresentableItem> iterator = children.iterator(); iterator.hasNext();) {
                IPresentableItem i = (IPresentableItem) iterator.next();
                if (hasMatchingChild(i, pattern))
                    continue;
                int matches = i.matches(pattern);
                if (matches < 0) {
                    iterator.remove();
                }
            }
            return children.toArray(new IPresentableItem[children.size()]);
        }
    }
    
    private boolean hasMatchingChild(IPresentableItem item, String pattern) { //TODO : performance optimizations
        Collection<IPresentableItem> children = item.getChildren();
        if (children == null)
            return false;
        Queue<IPresentableItem> q = new LinkedList<IPresentableItem>();
        q.addAll(children);
        while (!q.isEmpty()) {
            IPresentableItem next = q.poll();
            if (next.matches(pattern) >= 0)
                return true;
            Collection<IPresentableItem> ch2 = next.getChildren();
            if (ch2 != null)
                q.addAll(ch2);
        }
        return false;
    }
    
    public Object getParent(Object element) {
        if (element instanceof ProjectElement) {
            ProjectElement projectElement = (ProjectElement) element;
            return projectElement.getParent();
        }
        return null;
    }
    
    public boolean hasChildren(Object element) {
        Object[] children = getChildren(element);
        return children != null && children.length > 0;
    }
    
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }
    
    public void dispose() {
    }
    
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
    
    public Image getImage(Object element) {
        if (element instanceof IPresentableItem) {
            IPresentableItem presentableItem = (IPresentableItem) element;
            return presentableItem.getImage();
        }
        return null;
    }
    
    public String getText(Object element) {
        if (element instanceof IPresentableItem) {
            IPresentableItem presentableItem = (IPresentableItem) element;
            return presentableItem.getCaption();
        }
        return null;
    }
    
    public void addListener(ILabelProviderListener listener) {
    }
    
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }
    
    public void removeListener(ILabelProviderListener listener) {
    }
    
    private IPresentableItem extractPresentableItem(TreeItem item) {
        if (item != null && item.getData() instanceof IPresentableItem) {
            return (IPresentableItem) item.getData();
        }
        return null;
    }
    
    public void handleEvent(Event event) {
        try {
            TreeItem item = (TreeItem) event.item;
            IPresentableItem presentable = extractPresentableItem(item);
            if (presentable != null) {
                switch (event.type) {
                case SWT.PaintItem:
                    presentable.paintItem(item, event);
                    break;
                case SWT.MeasureItem:
                    presentable.measureItem(item, event);
                    break;
                case SWT.EraseItem:
                    presentable.eraseItem(item, event);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void install(TreeViewer viewer) {
        final Tree tree = viewer.getTree();
        
        editor = new TreeEditor(tree);
        //The editor must have the same size as the cell and must
        //not be any smaller than 50 pixels.
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        
        tree.addKeyListener(new InPlaceEditorListener(tree, editor));
              
        viewer.setContentProvider(this);
        viewer.setLabelProvider(this);
        viewer.getTree().addListener(SWT.PaintItem, this);
        viewer.getTree().addListener(SWT.MeasureItem, this);
        viewer.getTree().addListener(SWT.EraseItem, this);
        
        viewer.getTree().addListener(SWT.MouseDoubleClick, this);
        viewer.getTree().addListener(SWT.MouseUp, this);
        viewer.getTree().addListener(SWT.KeyDown, this);
    }
    
    
}
