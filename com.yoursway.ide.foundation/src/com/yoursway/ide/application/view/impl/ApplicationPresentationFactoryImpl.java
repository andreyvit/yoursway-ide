package com.yoursway.ide.application.view.impl;

import com.yoursway.ide.application.view.application.ApplicationPresentation;
import com.yoursway.ide.application.view.application.ApplicationPresentationCallback;
import com.yoursway.ide.application.view.application.ApplicationPresentationFactory;
import com.yoursway.ide.platforms.api.PlatformSupport;

public class ApplicationPresentationFactoryImpl implements ApplicationPresentationFactory {

    private final PlatformSupport platformSupport;
    private final ApplicationMenuFactory menuFactory;

    public ApplicationPresentationFactoryImpl(PlatformSupport platformSupport, ApplicationMenuFactory menuFactory) {
        if (platformSupport == null)
            throw new NullPointerException("platformSupport is null");
        if (menuFactory == null)
            throw new NullPointerException("menuFactory is null");
        this.platformSupport = platformSupport;
        this.menuFactory = menuFactory;
    }

    public ApplicationPresentation createPresentation(ApplicationPresentationCallback callback) {
        return new ApplicationPresentationImpl(callback, platformSupport, menuFactory);
    }
    
}
