/**
 * 
 */
package com.yoursway.ruby.wala2.translator;

import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;

import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstNode;
import com.yoursway.rails.commons.RubyAstVisitor;
import com.yoursway.ruby.wala2.entities.ScriptEntity;

class ModuleState extends AbstractModuleOrFuncOrClassState<ModuleDeclaration> {
    
    private final String scriptName;

	public ModuleState(RubyAstVisitor<?> parentVisitor, String scriptName) {
        super(parentVisitor);
		this.scriptName = scriptName;
    }
    
    @Override
    protected RubyAstVisitor<?> enterTypeDeclaration(TypeDeclaration node) {
    	return new ClassState(this);
    }

    protected void createEntity(Map<CAstNode, Set<CAstEntity>> subs, CAstNode ast) {
        getParentVisitor().addChildEntity(new ScriptEntity(scriptName, ast, subs, pos(), cfg()));
    }
}