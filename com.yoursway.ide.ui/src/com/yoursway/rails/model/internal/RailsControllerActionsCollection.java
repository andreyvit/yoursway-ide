package com.yoursway.rails.model.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;

import com.yoursway.ide.ui.Activator;
import com.yoursway.rails.model.Caching;
import com.yoursway.rails.model.IRailsAction;
import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsControllerActionsCollection;
import com.yoursway.ruby.model.RubyFactory;
import com.yoursway.ruby.model.RubyMethod;

public class RailsControllerActionsCollection extends RailsElement implements
        IRailsControllerActionsCollection {
    
    private Collection<RailsAction> actions;
    private final IRailsController railsController;
    private boolean itemsKnown = false;
    
    public RailsControllerActionsCollection(IRailsController railsController) {
        this.railsController = railsController;
    }
    
    public void refresh() {
        IType type = railsController.getCorrespondingType(Caching.NORMAL);
        actions = new ArrayList<RailsAction>();
        if (type != null) {
            IMethod[] methods;
            try {
                methods = type.getMethods();
            } catch (ModelException e) {
                Activator.log(e);
                return;
            }
            System.out.println("RailsControllerActionsCollection.refresh()");
            for (IMethod method : methods) {
                RubyMethod rubyMethod = RubyFactory.instance().createMethod(method);
                // XXX: reactivate this check when DLTK begins to report visibility correctly
                if (true || rubyMethod.isPublic()) {
                    RailsAction railsAction = new RailsAction(this, rubyMethod);
                    actions.add(railsAction);
                }
            }
        }
        itemsKnown = true;
    }
    
    private RailsAction findActionFor(RubyMethod method) {
        for (RailsAction action : actions) {
            RubyMethod amethod = action.getMethod();
            if (amethod.equals(method))
                return action;
        }
        return null;
    }
    
    private void addAction(RailsDeltaBuilder deltaBuilder, RubyMethod method) {
        RailsAction action = new RailsAction(this, method);
        actions.add(action);
        if (deltaBuilder != null)
            deltaBuilder.somethingChanged();
    }
    
    public void reconcile(RailsDeltaBuilder deltaBuilder) {
        refresh();
        deltaBuilder.somethingChanged();
    }
    
    public Collection<? extends IRailsAction> getActions() {
        ensureItemsKnown();
        return actions;
    }
    
    private void ensureItemsKnown() {
        if (!itemsKnown)
            refresh();
    }
    
    public boolean hasItems() {
        return true;
    }
    
}
