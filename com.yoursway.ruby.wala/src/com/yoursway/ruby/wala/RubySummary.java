package com.yoursway.ruby.wala;

import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.ipa.summaries.MethodSummary;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

public class RubySummary extends MethodSummary {
    
    private final int declaredParameters;
    
    public RubySummary(MethodReference ref, int declaredParameters) {
        super(ref);
        this.declaredParameters = declaredParameters;
        addStatement(new RubyNewInstruction(declaredParameters + 1, NewSiteReference.make(
                getNextProgramCounter(), RubyTypes.Array)));
        
    }
    
    public int getNumberOfParameters() {
        return declaredParameters;
    }
    
    public TypeReference getParameterType(int i) {
        return RubyTypes.Root;
    }
    
}
