package com.yoursway.ide.rhtml.core.java;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;

class TagTranslationAdapter extends JSPTranslationAdapter {
    TagTranslationAdapter(IDOMModel xmlModel) {
        super(xmlModel);
    }
    
    @Override
    JSPTranslator createTranslator() {
        return new TagTranslator();
    }
}
