/**
 * 
 */
package com.yoursway.ide.ui.railsview.presenters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;

import com.yoursway.common.SegmentedName;
import com.yoursway.ide.ui.railsview.presentation.AbstractPresenter;
import com.yoursway.ide.ui.railsview.presentation.IContextMenuContext;
import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;
import com.yoursway.ide.ui.railsview.presentation.IProvidesTreeItem;
import com.yoursway.rails.core.controllers.PerProjectRailsControllersCollection;
import com.yoursway.rails.core.controllers.RailsController;
import com.yoursway.rails.core.controllers.RailsControllersCollection;
import com.yoursway.rails.core.migrations.RailsMigrationsCollection;
import com.yoursway.rails.core.models.PerProjectRailsModelsCollection;
import com.yoursway.rails.core.models.RailsModel;
import com.yoursway.rails.core.models.RailsModelsCollection;
import com.yoursway.rails.core.projects.RailsProject;
import com.yoursway.utils.collections.MultiMap;

public class ProjectPresenter extends AbstractPresenter {
    
    private final RailsProject railsProject;
    
    public ProjectPresenter(IPresenterOwner owner, RailsProject railsProject) {
        super(owner);
        this.railsProject = railsProject;
    }
    
    public boolean canEditInPlace() {
        return false;
    }
    
    public String getCaption() {
        return null;
    }
    
    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        addControllers(children);
        addModels(children);
        children.add(new MigrationsCollectionPresenter(getOwner(), RailsMigrationsCollection.instance().get(
                railsProject)));
        //        addControllers(oldRailsProject.getControllersCollection().getRootFolder(), children);
        //        children.add(new NewModelPresenter(getOwner(), oldRailsProject));
        //        children.addAll(oldRailsProject.getModelsCollection().getItems());
        return children.toArray();
    }
    
    private void addModels(Collection<Object> children) {
        PerProjectRailsModelsCollection perProjectRailsModeslCollection = RailsModelsCollection.instance()
                .get(railsProject);
        MultiMap<SegmentedName, RailsModel> packages = new MultiMap<SegmentedName, RailsModel>();
        for (RailsModel railsModel : perProjectRailsModeslCollection.getAll())
            packages.put(railsModel.getNamespace(), railsModel);
        for (Map.Entry<SegmentedName, Collection<RailsModel>> entry : packages.entrySet()) {
            children.add(new ModelPackagePresenter(getOwner(), entry.getKey(), entry.getValue()));
            List<RailsModel> list = new ArrayList<RailsModel>(entry.getValue());
            Collections.sort(list, new RailsModelsComparator());
            children.addAll(list);
        }
    }
    
    private void addControllers(Collection<Object> children) {
        PerProjectRailsControllersCollection perProjectRailsControllersCollection = RailsControllersCollection
                .instance().get(railsProject);
        MultiMap<SegmentedName, RailsController> packages = new MultiMap<SegmentedName, RailsController>();
        for (RailsController railsController : perProjectRailsControllersCollection.getAll())
            packages.put(railsController.getNamespace(), railsController);
        
        for (Map.Entry<SegmentedName, Collection<RailsController>> entry : packages.entrySet()) {
            children.add(new ControllerPackagePresenter(getOwner(), entry.getKey(), entry.getValue()));
            List<RailsController> list = new ArrayList<RailsController>(entry.getValue());
            Collections.sort(list, new RailsControllersComparator());
            children.addAll(list);
        }
    }
    
    //    private void addControllers(IRailsControllersFolder folder, Collection<Object> children) {
    //        children.add(folder);
    //        children.add(new NewControllerPresenter(getOwner(), oldRailsProject));
    //        IRailsFolderControllersCollection controllersC = folder.getControllersCollection();
    //        List<? extends IRailsController> controllers = new ArrayList<IRailsController>(controllersC
    //                .getControllers());
    //        Collections.sort(controllers, new Comparator<IRailsController>() {
    //            
    //            public int compare(IRailsController o1, IRailsController o2) {
    //                return o1.getFile().getName().compareTo(o2.getFile().getName());
    //            }
    //            
    //        });
    //        children.addAll(controllers);
    //        List<? extends IRailsControllersFolder> subfolders = new ArrayList<IRailsControllersFolder>(folder
    //                .getSubfoldersCollection().getSubfolders());
    //        Collections.sort(subfolders, new Comparator<IRailsControllersFolder>() {
    //            
    //            public int compare(IRailsControllersFolder o1, IRailsControllersFolder o2) {
    //                return o1.getCorrespondingFolder().getName().compareTo(o2.getCorrespondingFolder().getName());
    //            }
    //            
    //        });
    //        for (IRailsControllersFolder subfolder : subfolders) {
    //            addControllers(subfolder, children);
    //        }
    //    }
    
    public ImageDescriptor getImage() {
        return null;
    }
    
    public Object getParent() {
        return null;
    }
    
    public boolean hasChildren() {
        return true;
    }
    
    public void handleDoubleClick(IProvidesTreeItem context) {
    }
    
    public void fillContextMenu(IContextMenuContext context) {
    }
    
}