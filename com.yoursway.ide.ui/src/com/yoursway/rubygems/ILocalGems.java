package com.yoursway.rubygems;

public interface ILocalGems {
    String[] parseGemVersions(String serializedGems);
    
    IGem[] parseGemsInfo(String serializedGems);
}
