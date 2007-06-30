/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters.model;


public class ModelPresenter {//extends AbstractPresenter {
//    
//    private final IRailsModel railsModel;
//    
//    public ModelPresenter(IPresenterOwner owner, IRailsModel railsModel) {
//        super(owner);
//        this.railsModel = railsModel;
//    }
//    
//    public boolean canEditInPlace() {
//        return false;
//    }
//    
//    public String getCaption() {
//        return railsModel.getName();
//    }
//    
//    public Object[] getChildren() {
//        IRailsTable table = railsModel.getRailsProject().getSchema().findByName(railsModel.getTableName());
//        Collection<Object> children = new ArrayList<Object>();
//        if (table != null)
//            children.addAll(table.getFields().getItems());
//        return children.toArray();
//    }
//    
//    public ImageDescriptor getImage() {
//        return RailsViewImages.MODEL_ICON;
//    }
//    
//    public Object getParent() {
//        return railsModel.getRailsProject();
//    }
//    
//    public boolean hasChildren() {
//        return getChildren().length > 0;
//    }
//    
//    public void handleDoubleClick(IProvidesTreeItem context) {
//        openEditor(railsModel.getCorrespondingFile());
//    }
//    
//    public void fillContextMenu(IContextMenuContext context) {
//        context.getMenuManager().add(new DeleteFileAction(railsModel.getCorrespondingFile()));
//    }

}