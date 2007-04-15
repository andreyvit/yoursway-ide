package com.yoursway.utils;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.dltk.core.ModelException;

public class ExceptionUtils {
    
    public static Throwable unwrap(Throwable possibleWrapper) {
        if (possibleWrapper instanceof InvocationTargetException)
            return ((InvocationTargetException) possibleWrapper).getCause();
        else if (possibleWrapper instanceof ModelException) {
            ModelException modelException = (ModelException) possibleWrapper;
            if (modelException.getCause() != null)
                return modelException.getCause();
            else if (modelException.getStatus().getException() != null)
                return modelException.getStatus().getException();
        } else if (possibleWrapper.getClass() == RuntimeException.class) {
            if (((RuntimeException) possibleWrapper).getCause() != null)
                return ((RuntimeException) possibleWrapper).getCause();
        }
        return possibleWrapper;
    }
    
}
