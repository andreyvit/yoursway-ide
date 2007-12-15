package com.yoursway.ide.ui.railsview.shit.rails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.yoursway.ide.ui.railsview.RailsViewImages;
import com.yoursway.ide.ui.railsview.shit.IPresentableItem;
import com.yoursway.ide.ui.railsview.shit.IViewInfoProvider;
import com.yoursway.ide.ui.railsview.shit.SimpleProjectElement;
import com.yoursway.model.rails.IRailsController;
import com.yoursway.model.rails.IRailsControllerAction;
import com.yoursway.model.rails.IRailsPartial;
import com.yoursway.model.rails.IRailsView;
import com.yoursway.model.repository.IResolver;

public class ControllerElement extends SimpleProjectElement {
    
    private final IRailsController controller;
    
    public ControllerElement(ControllersCategory parent, IRailsController controller,
            IViewInfoProvider infoProvider) {
        super(parent, "Bublik", infoProvider);
        this.controller = controller;
        IResolver resolver = getResolver();
        if (resolver != null) {
            String name = resolver.get(controller.name());
            this.setName(name);
        }
    }
    
    public Collection<IPresentableItem> getChildren() {
        IResolver resolver = getResolver();
        if (resolver != null) {
            List<IPresentableItem> result = new ArrayList<IPresentableItem>();
            
            Collection<IRailsControllerAction> actions = resolver.get(controller.actions());
            for (IRailsControllerAction a : actions) {
                result.add(new ActionElement(this, a, this.infoProvider));
            }
//            Collection<IRailsPartial> partials = resolver.get(controller.getPartials());
//            Collection<IRailsView> views = resolver.get(controller.getViews());
            return result;
        }
        return null;
    }
    
    public Image getImage() {
        return RailsViewImages.CONTROLLER_ICON_IMG;
    }
    
    public boolean hasChildren() {
        IResolver resolver = getResolver();
        if (resolver != null) {
            Collection<IRailsControllerAction> actions = resolver.get(controller.actions());
            Collection<IRailsPartial> partials = resolver.get(controller.partials());
            Collection<IRailsView> views = resolver.get(controller.views());
            return (views.size() + partials.size() + actions.size()) > 0;
        }
        return false;
    }
    
}
