package com.yoursway.ide.application.view.impl;

import com.yoursway.ide.application.view.application.ApplicationPresentation;
import com.yoursway.ide.application.view.application.ApplicationPresentationCallback;
import com.yoursway.ide.application.view.application.ApplicationPresentationFactory;

public class ApplicationPresentationFactoryImpl implements ApplicationPresentationFactory {

    public ApplicationPresentation createPresentation(ApplicationPresentationCallback callback) {
        return new ApplicationPresentationImpl(callback);
    }
    
}
