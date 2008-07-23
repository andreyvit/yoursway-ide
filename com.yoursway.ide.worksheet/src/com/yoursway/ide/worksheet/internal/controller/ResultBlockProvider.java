package com.yoursway.ide.worksheet.internal.controller;

import com.yoursway.ide.worksheet.internal.view.ResultBlock;
import com.yoursway.utils.annotations.UseFromUIThread;

public interface ResultBlockProvider {
    
    @UseFromUIThread
    ResultBlock get();
    
}
