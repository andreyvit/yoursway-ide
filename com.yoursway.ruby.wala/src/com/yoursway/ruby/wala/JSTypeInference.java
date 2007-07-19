package com.yoursway.ruby.wala;

import com.ibm.wala.analysis.typeInference.ConeType;
import com.ibm.wala.analysis.typeInference.PointType;
import com.ibm.wala.analysis.typeInference.TypeAbstraction;
import com.ibm.wala.cast.analysis.typeInference.AstTypeInference;
import com.ibm.wala.fixpoint.IVariable;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.types.TypeReference;

public class JSTypeInference extends AstTypeInference {

  public JSTypeInference(IR ir, IClassHierarchy cha) {
    super(ir, cha, new PointType(cha.lookupClass(JavaScriptTypes.Boolean)), true);
  }

  protected void initialize() {
    class JSTypeOperatorFactory extends AstTypeOperatorFactory implements XInstructionVisitor {
      public void visitJavaScriptInvoke(JavaScriptInvoke inst) {
        result = new DeclaredTypeOperator(new ConeType(cha.getRootClass()));
      }

      public void visitJavaScriptPropertyRead(JavaScriptPropertyRead inst) {
        result = new DeclaredTypeOperator(new ConeType(cha.getRootClass()));
      }

      public void visitTypeOf(JavaScriptTypeOfInstruction inst) {
        result = new DeclaredTypeOperator(new PointType(cha.lookupClass(JavaScriptTypes.String)));
      }

      public void visitJavaScriptPropertyWrite(JavaScriptPropertyWrite inst) {
      }
    }
    ;

    class JSTypeVarFactory extends TypeVarFactory {

      private TypeAbstraction make(TypeReference typeRef) {
        return new PointType(cha.lookupClass(typeRef));
      }

      public IVariable makeVariable(int vn) {
        if (ir.getSymbolTable().isStringConstant(vn)) {
          return new TypeVariable(make(JavaScriptTypes.String), 1331 * vn);
        } else if (ir.getSymbolTable().isBooleanConstant(vn)) {
          return new TypeVariable(make(JavaScriptTypes.Boolean), 4197 * vn);
        } else if (ir.getSymbolTable().isNullConstant(vn)) {
          return new TypeVariable(make(JavaScriptTypes.Null), 4077 * vn);
        } else if (ir.getSymbolTable().isNumberConstant(vn)) {
          return new TypeVariable(make(JavaScriptTypes.Number), 797 * vn);
        } else {
          return super.makeVariable(vn);
        }
      }
    }
    ;

    init(ir, new JSTypeVarFactory(), new JSTypeOperatorFactory());
  }

  public TypeAbstraction getConstantType(int valueNumber) {
    SymbolTable st = ir.getSymbolTable();
    if (st.isStringConstant(valueNumber)) {
      return new PointType(cha.lookupClass(JavaScriptTypes.String));
    } else if (st.isBooleanConstant(valueNumber)) {
      return new PointType(cha.lookupClass(JavaScriptTypes.Boolean));
    } else if (st.isNullConstant(valueNumber)) {
      return new PointType(cha.lookupClass(JavaScriptTypes.Null));
    } else {
      return new PointType(cha.lookupClass(JavaScriptTypes.Number));
    }
  }
}
