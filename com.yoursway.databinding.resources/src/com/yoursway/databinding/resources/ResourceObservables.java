package com.yoursway.databinding.resources;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.Assert;

import com.yoursway.databinding.commons.YourSwayRealm;
import com.yoursway.databinding.resources.internal.ShallowChildrenObservable;

public class ResourceObservables {

//    public static IContainerObservableSet observeChildren(IContainer container) {
//        return observeChildren(Realm.getDefault(), container);
//    }
    
    public static IContainerObservableSet observeChildren(YourSwayRealm realm, IContainer container) {
        Assert.isNotNull(container);
        return new ShallowChildrenObservable(realm, container);
    }
    
}
