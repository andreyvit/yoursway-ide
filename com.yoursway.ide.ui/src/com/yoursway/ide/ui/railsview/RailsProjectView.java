package com.yoursway.ide.ui.railsview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

public class RailsProjectView extends ViewPart implements IRailsProjectTreeOwner {
    
    private Text searchTextControl;
    private RailsProjectTree projectTree;
    
    @Override
    public void createPartControl(Composite parent) {
        Composite body = new Composite(parent, SWT.NONE);
        GridLayout layout = createLayout();
        body.setLayout(layout);
        
        createSearchTextControl(body);
        
        projectTree = new RailsProjectTree(body, null, this);
        
        createBottomControls(body);
        
    }
    
    private void createBottomControls(Composite body) {
        // TODO Auto-generated method stub
        
    }
    
    private void createSearchTextControl(Composite parent) {
        searchTextControl = new Text(parent, SWT.SEARCH | SWT.CANCEL);
        searchTextControl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL));
        searchTextControl.setText("");
        searchTextControl.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                if (e.getSource() == searchTextControl) {
                    Text text = (Text) e.getSource();
                    text.redraw();
                    projectTree.setFilteringPattern(text.getText());
                    projectTree.refresh();
                }
            }
            
        });
        searchTextControl.addPaintListener(new PaintListener() {
            
            public void paintControl(PaintEvent e) {
                if (e.getSource() == searchTextControl && searchTextControl.getText().length() == 0
                        && !searchTextControl.isFocusControl()) {
                    GC gc = e.gc;
                    Color oldForeground = gc.getForeground();
                    gc.setForeground(e.display.getSystemColor(SWT.COLOR_GRAY));
                    int y = (e.height - gc.getFontMetrics().getHeight()) / 2 - 3;
                    int x = e.height / 2;
                    gc.drawText("Pattern matching", x, y);
                    gc.setForeground(oldForeground);
                }
            }
            
        });
    }
    
    private GridLayout createLayout() {
        GridLayout layout = new GridLayout();
        layout.makeColumnsEqualWidth = true;
        layout.numColumns = 1;
        layout.verticalSpacing = 1;
        return layout;
    }
    
    @Override
    public void setFocus() {
        projectTree.setFocus();
    }
    
    public IWorkbenchPage getWorkbenchPage() {
        return getSite().getPage();
    }
    
}
