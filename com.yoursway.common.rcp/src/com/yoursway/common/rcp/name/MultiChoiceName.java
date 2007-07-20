package com.yoursway.common.rcp.name;

import org.eclipse.core.runtime.Assert;

public class MultiChoiceName {
    
    private final String shortLowercaseName;
    private final String longNameWithSpaces;
    private final VendorProductName vendorProductName;
    
    public MultiChoiceName(String shortLowercaseName, String longNameWithSpaces,
            VendorProductName vendorProductName) {
        Assert.isNotNull(shortLowercaseName);
        Assert.isNotNull(longNameWithSpaces);
        Assert.isNotNull(vendorProductName);
        
        this.shortLowercaseName = shortLowercaseName;
        this.longNameWithSpaces = longNameWithSpaces;
        this.vendorProductName = vendorProductName;
    }
    
    public String getShortLowercaseName() {
        return shortLowercaseName;
    }
    
    public String getLongNameWithSpaces() {
        return longNameWithSpaces;
    }
    
    public VendorProductName getVendorProductName() {
        return vendorProductName;
    }
    
}
