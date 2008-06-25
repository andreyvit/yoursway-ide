package com.yoursway.ide.application.view;

public enum ViewSiteRole {
    
    PROJECT_LIST {

        @Override
        public void accept(ViewSiteRoleVisitor visitor) {
            visitor.visitProjectListRole();
        }

        @Override
        public boolean allowsMultiple() {
            return false;
        }
        
    },
    
    ;
    
    public abstract boolean allowsMultiple();
    
    public abstract void accept(ViewSiteRoleVisitor visitor);
    
}
