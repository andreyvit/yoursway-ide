package com.yoursway.ide.application.view.impl;

import com.yoursway.ide.application.view.application.ApplicationPresentation;
import com.yoursway.ide.application.view.application.ApplicationPresentationCallback;
import com.yoursway.ide.application.view.application.ApplicationPresentationFactory;
import com.yoursway.ide.platforms.api.PlatformSupport;

public class ApplicationPresentationFactoryImpl implements ApplicationPresentationFactory {

    private final PlatformSupport platformSupport;

    public ApplicationPresentationFactoryImpl(PlatformSupport platformSupport) {
        if (platformSupport == null)
            throw new NullPointerException("platformSupport is null");
        this.platformSupport = platformSupport;
    }

    public ApplicationPresentation createPresentation(ApplicationPresentationCallback callback) {
        return new ApplicationPresentationImpl(callback, platformSupport);
    }
    
}
