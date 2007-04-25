package com.yoursway.ide.ui.railsview.presentation;

import com.yoursway.ide.ui.railsview.presenters.ActionPresenter;
import com.yoursway.ide.ui.railsview.presenters.ControllersFolderPresenter;
import com.yoursway.ide.ui.railsview.presenters.FieldPresenter;
import com.yoursway.ide.ui.railsview.presenters.PartialPresenter;
import com.yoursway.ide.ui.railsview.presenters.ProjectPresenter;
import com.yoursway.ide.ui.railsview.presenters.ViewPresenter;
import com.yoursway.ide.ui.railsview.presenters.controller.ControllerPresenter;
import com.yoursway.ide.ui.railsview.presenters.model.ModelPresenter;
import com.yoursway.rails.model.IRailsAction;
import com.yoursway.rails.model.IRailsController;
import com.yoursway.rails.model.IRailsControllersFolder;
import com.yoursway.rails.model.IRailsField;
import com.yoursway.rails.model.IRailsModel;
import com.yoursway.rails.model.IRailsPartial;
import com.yoursway.rails.model.IRailsProject;
import com.yoursway.rails.model.IRailsView;

public class ElementPresenterFactory implements IPresenterFactory {
    
    private final IPresenterOwner owner;
    
    public ElementPresenterFactory(IPresenterOwner owner) {
        this.owner = owner;
    }
    
    public IElementPresenter createPresenter(Object element) {
        if (element instanceof IElementPresenter)
            return (IElementPresenter) element;
        if (element instanceof IRailsProject)
            return new ProjectPresenter(owner, (IRailsProject) element);
        if (element instanceof IRailsController)
            return new ControllerPresenter(owner, (IRailsController) element);
        if (element instanceof IRailsControllersFolder)
            return new ControllersFolderPresenter(owner, (IRailsControllersFolder) element);
        if (element instanceof IRailsAction)
            return new ActionPresenter(owner, (IRailsAction) element);
        if (element instanceof IRailsView)
            return new ViewPresenter(owner, (IRailsView) element);
        if (element instanceof IRailsPartial)
            return new PartialPresenter(owner, (IRailsPartial) element);
        if (element instanceof IRailsModel)
            return new ModelPresenter(owner, (IRailsModel) element);
        if (element instanceof IRailsField)
            return new FieldPresenter(owner, (IRailsField) element);
        throw new AssertionError("Unknown tree item type " + element.getClass().getName() + " found.");
    }
    
}
