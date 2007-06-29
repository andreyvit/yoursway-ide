package com.yoursway.common.rcp.name;

import org.eclipse.core.runtime.Assert;

public class VendorProductName {
    
    private final String longVendorName;
    private final String longProductNameWithoutVendor;
    
    public VendorProductName(String longVendorName, String longProductNameWithoutVendor) {
        Assert.isNotNull(longVendorName);
        Assert.isNotNull(longProductNameWithoutVendor);
        
        this.longVendorName = longVendorName;
        this.longProductNameWithoutVendor = longProductNameWithoutVendor;
    }
    
    public String getLongVendorName() {
        return longVendorName;
    }
    
    public String getLongProductNameWithoutVendor() {
        return longProductNameWithoutVendor;
    }
    
}
