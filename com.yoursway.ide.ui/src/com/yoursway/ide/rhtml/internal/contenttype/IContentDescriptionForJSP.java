package com.yoursway.ide.rhtml.internal.contenttype;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.wst.sse.core.internal.encoding.ICodedResourcePlugin;

public interface IContentDescriptionForJSP {
    /**
     * Extra properties as part of ContentDescription, if the content is JSP.
     */
    public final static QualifiedName CONTENT_TYPE_ATTRIBUTE = new QualifiedName(ICodedResourcePlugin.ID,
            "contentTypeAttribute"); //$NON-NLS-1$
    public final static QualifiedName LANGUAGE_ATTRIBUTE = new QualifiedName(ICodedResourcePlugin.ID,
            "languageAttribute"); //$NON-NLS-1$
    public final static QualifiedName CONTENT_FAMILY_ATTRIBUTE = new QualifiedName(ICodedResourcePlugin.ID,
            "contentFamilyAttribute"); //$NON-NLS-1$;
    
}
