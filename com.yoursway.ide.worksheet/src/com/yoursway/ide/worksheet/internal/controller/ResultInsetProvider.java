package com.yoursway.ide.worksheet.internal.controller;

import com.yoursway.ide.worksheet.internal.view.ResultInset;
import com.yoursway.utils.annotations.UseFromUIThread;

public interface ResultInsetProvider {
    
    @UseFromUIThread
    ResultInset get();
    
}
