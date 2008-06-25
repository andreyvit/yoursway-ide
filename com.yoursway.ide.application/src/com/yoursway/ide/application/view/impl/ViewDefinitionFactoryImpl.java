package com.yoursway.ide.application.view.impl;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.yoursway.ide.application.view.ViewDefinition;
import com.yoursway.ide.application.view.ViewDefinitionFactory;
import com.yoursway.utils.UniqueId;

public class ViewDefinitionFactoryImpl implements ViewDefinitionFactory {
    
    private final Collection<ViewDefinitionImpl> viewDefinitions = newArrayList();

    public Collection<? extends ViewDefinition> filterByArea(final ViewArea area) {
        return newArrayList(filter(viewDefinitions, new Predicate<ViewDefinitionImpl>() {

            public boolean apply(ViewDefinitionImpl t) {
                return t.area() == area;
            }
            
        }));
    }
    
//    public ViewDefinition findOneByRole(final ViewSiteRole role) {
//        Collection<? extends ViewDefinition> result = filterByArea(role);
//        if (result.isEmpty())
//            return null;
//        if (result.size() > 1)
//            throw new AssertionError("Should not have allowed registering multiple views with role " + role);
//        return result.iterator().next();
//    }

    public ViewDefinition defineView(UniqueId uniqueId, ViewArea area) {
        if (!area.areMultipleContributionsAllowed() && !filterByArea(area).isEmpty())
            throw new IllegalStateException("Cannot register multiple views in area " + area);
        ViewDefinitionImpl result = new ViewDefinitionImpl(uniqueId, area);
        viewDefinitions.add(result);
        return result;
    }
    
}
