package com.yoursway.ide.ui.railsview.presenters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.yoursway.ide.ui.railsview.presentation.IPresenterOwner;

public class MigrationsCollectionPresenter extends PackagePresenter {
    
    private final PerProjectRailsMigrationsCollection migrations;
    
    public MigrationsCollectionPresenter(IPresenterOwner owner, PerProjectRailsMigrationsCollection migrations) {
        super(owner);
        this.migrations = migrations;
    }
    
    public String getCaption() {
        return "Migrations";
    }
    
    @Override
    public boolean hasChildren() {
        return true;
    }
    
    @Override
    public Object[] getChildren() {
        Collection<Object> children = new ArrayList<Object>();
        final java.util.List<RailsMigration> all = new ArrayList<RailsMigration>(migrations.getAll());
        Collections.sort(all, new Comparator<RailsMigration>() {
            
            public int compare(RailsMigration o1, RailsMigration o2) {
                return o1.getOrdinal() - o2.getOrdinal();
            }
            
        });
        children.addAll(all);
        for (Object child : children)
            if (child == null)
                throw new AssertionError("Null child!");
        return children.toArray();
    }
}
