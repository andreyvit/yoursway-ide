package com.yoursway.ide.ui.railsview;

import com.yoursway.model.repository.IResolver;

public interface IResolverProvider {
    IResolver getModelResolver();
}
