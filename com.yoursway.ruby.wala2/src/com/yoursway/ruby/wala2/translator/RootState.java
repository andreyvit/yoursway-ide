/**
 * 
 */
package com.yoursway.ruby.wala2.translator;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;

import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.yoursway.rails.commons.RubyAstVisitor;

class RootState extends AbstractState<ASTNode> {
    
    private CAstEntity moduleEntity;
	private final String scriptName;
    
    public RootState(String scriptName, Context context) {
        super(null, context);
		this.scriptName = scriptName;
    }
    
    @Override
    protected RubyAstVisitor<?> enterModuleDeclaration(ModuleDeclaration node) {
        return new ModuleState(this, scriptName);
    }
    
    protected void addChildNode(CAstNode node) {
        
    }
    
    protected void addChildEntity(CAstEntity entity) {
        this.moduleEntity = entity;
    }
    
    public CAstEntity getModuleEntity() {
        return moduleEntity;
    }
     
}