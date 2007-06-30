/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;


public class ActionPresenter { //extends AbstractPresenter {
//    
//    private final IRailsAction railsAction;
//    
//    public ActionPresenter(IPresenterOwner owner, IRailsAction railsAction) {
//        super(owner);
//        this.railsAction = railsAction;
//    }
//    
//    public boolean canEditInPlace() {
//        return false;
//    }
//    
//    public String getCaption() {
//        return railsAction.getName();
//    }
//    
//    public Object[] getChildren() {
//        Collection<Object> children = new ArrayList<Object>();
//        return children.toArray();
//    }
//    
//    public ImageDescriptor getImage() {
//        return RailsViewImages.ACTION_ICON;
//    }
//    
//    public Object getParent() {
//        return null;
//    }
//    
//    public boolean hasChildren() {
//        return false;
//    }
//    
//    public void handleDoubleClick(IProvidesTreeItem context) {
//        RubyMethod method = railsAction.getMethod();
//        if (method != null)
//            openEditor(method.getDLTKMethod());
//    }
//    
//    public void fillContextMenu(IContextMenuContext context) {
//        // TODO Auto-generated method stub
//        
//    }
//    
}