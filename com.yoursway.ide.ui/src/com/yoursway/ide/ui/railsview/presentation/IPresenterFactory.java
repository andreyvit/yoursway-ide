package com.yoursway.ide.ui.railsview.presentation;

public interface IPresenterFactory {
    
    public abstract IElementPresenter createPresenter(Object element);
    
}