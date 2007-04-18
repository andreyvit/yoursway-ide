/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchWindow;

import com.yoursway.ide.ui.Activator;
import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.presentation.AbstractPresenter;
import com.yoursway.ide.ui.railsview.presentation.IContextMenuContext;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.presentation.TreeItemSnaps;
import com.yoursway.ide.ui.railsview.presentation.TreeSnaps;
import com.yoursway.rails.model.IRailsController;
import com.yoursway.ui.popup.IPopupHost;
import com.yoursway.ui.popup.SnapPosition;
import com.yoursway.ui.popup.addons.PartDeactivationHider;
import com.yoursway.ui.popup.addons.PopupSnapPositionPersister;
import com.yoursway.ui.popup.addons.ShellDeactivationCloser;
import com.yoursway.ui.popup.anchors.Anchor;
import com.yoursway.ui.popup.anchors.AnchoredPointProvider;
import com.yoursway.ui.popup.anchors.CompositeClientAreaProvider;
import com.yoursway.ui.popup.anchors.IRectangleProvider;
import com.yoursway.ui.popup.menu.SnapToMenuItems;
import com.yoursway.ui.popup.trash.RenamePopup;
import com.yoursway.utils.RailsNamingConventions;

public class ControllerPresenter extends AbstractPresenter {
    
    private final class PopupHost implements IPopupHost {
        private final IContextMenuContext context;
        
        private PopupHost(IContextMenuContext context) {
            this.context = context;
        }
        
        public Composite getParent() {
            return getOwner().getTree();
        }
        
        public Shell getShell() {
            return getOwner().getWorkbenchPage().getWorkbenchWindow().getShell();
        }
        
        public Point getSnapPosition(int snapPosition) {
            TreeItem item = context.getTreeItem();
            Rectangle bounds = item.getBounds();
            return new Point(bounds.x, bounds.y + bounds.height);
        }
        
        public IWorkbenchWindow getWorkbenchWindow() {
            return getOwner().getWorkbenchPage().getWorkbenchWindow();
        }
    }
    
    private final IRailsController railsController;
    
    public ControllerPresenter(IPresenterOwner owner, IRailsController railsController) {
        super(owner);
        this.railsController = railsController;
    }
    
    public boolean canEditInPlace() {
        return false;
    }
    
    public String getCaption() {
        String[] classNameComponents = railsController.getExpectedClassName();
        return RailsNamingConventions.joinNamespaces(classNameComponents);
    }
    
    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        children.addAll(railsController.getActionsCollection().getActions());
        children.addAll(railsController.getViewsCollection().getItems());
        return children.toArray();
    }
    
    public ImageDescriptor getImage() {
        return RailsViewImages.CONTROLLER_ICON;
    }
    
    public Object getParent() {
        return railsController.getRailsProject();
    }
    
    public boolean hasChildren() {
        return railsController.getActionsCollection().hasItems()
                || railsController.getViewsCollection().hasItems();
    }
    
    public void handleDoubleClick() {
        openEditor(railsController.getFile());
    }
    
    public void fillContextMenu(final IContextMenuContext context) {
        context.getMenuManager().add(
                new Action("Rename " + railsController.getFile().getProjectRelativePath()) {
                    
                    @Override
                    public void runWithEvent(Event event) {
                        IPopupHost owner = new PopupHost(context);
                        Collection<SnapPosition> snaps = new ArrayList<SnapPosition>();
                        TreeItemSnaps treeItemSnaps = new TreeItemSnaps(context.getTreeItem());
                        TreeSnaps treeSnaps = new TreeSnaps(getOwner().getTree());
                        treeItemSnaps.addTo(snaps);
                        treeSnaps.addTo(snaps);
                        RenamePopup popup = new RenamePopup(owner, snaps);
                        new PopupSnapPositionPersister(popup, Activator
                                .getDialogSettingsSection("RenamePopup"));
                        new ShellDeactivationCloser(popup, popup.getMenuSupport());
                        SnapToMenuItems snapToMenu = new SnapToMenuItems(popup, popup.getMenuSupport());
                        treeItemSnaps.addTo(snapToMenu);
                        snapToMenu.addSeparator();
                        treeSnaps.addTo(snapToMenu);
                        new PartDeactivationHider(popup, getOwner().getWorkbenchPage().getActivePart());
                        popup.open();
                    }
                    
                    private void addTreeSnaps(Collection<SnapPosition> snaps) {
                        IRectangleProvider boundsProvider = new CompositeClientAreaProvider(getOwner()
                                .getTree());
                        snaps.add(new SnapPosition(new AnchoredPointProvider(boundsProvider,
                                Anchor.BOTTOM_RIGHT), Anchor.BOTTOM_RIGHT, "treeBottomRight"));
                        snaps.add(new SnapPosition(
                                new AnchoredPointProvider(boundsProvider, Anchor.TOP_RIGHT),
                                Anchor.TOP_RIGHT, "treeTopRight"));
                    }
                    
                });
    }
}
