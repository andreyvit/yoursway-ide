package com.yoursway.rails.model.internal;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.ObservableSet;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.set.SetDiff;
import org.eclipse.core.runtime.Assert;

public abstract class MappedObservableSet<K, V> extends ObservableSet {
    
    private IObservableSet sourceSet;
    
    private Map<K, V> projects;
    
    private final class Contents extends AbstractSet<V> {
        
        @Override
        public int size() {
            initialize();
            return projects.size();
        }
        
        @Override
        public Iterator<V> iterator() {
            initialize();
            return new HashSet<V>(projects.values()).iterator();
        }
        
    }
    
    private ISetChangeListener setChangeListener = new ISetChangeListener() {

        public void handleSetChange(SetChangeEvent event) {
            doHandleSetChange(event);
        }
        
    };
    
    public MappedObservableSet(Realm realm, Class<V> valueType, IObservableSet sourceSet) {
        super(realm, null, valueType);
        Assert.isTrue(sourceSet.getRealm() == realm);
        setWrappedSet(new Contents());
        this.sourceSet = sourceSet; 
        realm.asyncExec(new Runnable() {

            public void run() {
                initialize();
            }
            
        });
    }
    
    public V lookup(K project) {
        getterCalled();
        initialize();
        return projects.get(project);
    }
    
    @Override
    protected void firstListenerAdded() {
        sourceSet.addSetChangeListener(setChangeListener);
    }
    
    @Override
    protected void lastListenerRemoved() {
        sourceSet.removeSetChangeListener(setChangeListener);
    }
    
    @SuppressWarnings("unchecked")
    private void initialize() {
        if (projects == null) {
            projects = new HashMap<K, V>();
            for (Object project : sourceSet) {
                V mapping = createMapping((K) project);
                if (mapping != null)
                    projects.put((K) project, mapping);
            }
        }
    }
    
    /**
     * <code>initialize()</code> scheduled in the contructor is known to
     * have been executed, because this method will be called from inside
     * another <code>asyncExec</code>.
     */
    @SuppressWarnings("unchecked")
    void doHandleSetChange(SetChangeEvent event) {
        Assert.isNotNull(projects);
        Set<K> addedProjects = event.diff.getAdditions();
        Set<K> removedProjects = event.diff.getRemovals();
        final Set<V> added = new HashSet<V>(addedProjects.size());
        final Set<V> removed = new HashSet<V>(removedProjects.size());
        for (K project : addedProjects) {
            V mapping = createMapping(project);
            if (mapping != null) {
                projects.put(project, mapping);
                added.add(mapping);
            }
        }
        for (K project : removedProjects) {
            V oldMapping = projects.remove(project);
            if (oldMapping != null)
                removed.add(oldMapping);
        }
        if (added.isEmpty() && removed.isEmpty())
            return;
        fireSetChange(new SetDiff() {
            
            @Override
            public Set getAdditions() {
                return added;
            }
            
            @Override
            public Set getRemovals() {
                return removed;
            }
            
        });
    }
    
    protected abstract V createMapping(K project);

}
