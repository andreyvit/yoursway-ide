/**
 * 
 */
package com.yoursway.ide.projects.editor;

import java.util.regex.Pattern;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public final class ProjectNameValidator implements IValidator {
    public IStatus validate(Object value) {
        String name = (String) value;
        if (name != name.trim())
            return ValidationStatus.error("Leading and trailing whitespace not allowed");
        if (name.length() == 0)
            return ValidationStatus.error("Empty name is not allowed");
        if (!Pattern.matches("^[a-zA-Z0-9. _-]+$", name))
            return ValidationStatus.error("Illegal character");
        return Status.OK_STATUS;
        //            return ValidationStatus.info("Test");
    }
}