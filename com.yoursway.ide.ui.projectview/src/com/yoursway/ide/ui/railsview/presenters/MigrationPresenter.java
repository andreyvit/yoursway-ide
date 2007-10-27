/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.presentation.AbstractPresenter;
import com.yoursway.ide.ui.railsview.presentation.IContextMenuContext;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.presentation.IProvidesTreeItem;

public class MigrationPresenter extends AbstractPresenter {
    
    private final RailsMigration railsMigration;
    
    public MigrationPresenter(IPresenterOwner owner, RailsMigration railsMigration) {
        super(owner);
        this.railsMigration = railsMigration;
    }
    
    public boolean canEditInPlace() {
        return false;
    }
    
    public String getCaption() {
        return MessageFormat.format("{0,number,000}) {1}", railsMigration.getOrdinal(), railsMigration
                .getExpectedClassName());
    }
    
    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        return children.toArray();
    }
    
    public ImageDescriptor getImage() {
        return RailsViewImages.CONTROLLER_ICON;
    }
    
    public Object getParent() {
        return null;
    }
    
    public boolean hasChildren() {
        return false;
    }
    
    public void handleDoubleClick(IProvidesTreeItem context) {
        openEditor(railsMigration.getFile());
    }
    
    public void fillContextMenu(final IContextMenuContext context) {
        context.getMenuManager().add(new DeleteFileAction(railsMigration.getFile()));
    }
}
