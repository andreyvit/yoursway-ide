package com.yoursway.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class YSCollections {
    
    public static <T> List<T> sortedArrayListOf(Collection<T> collection, Comparator<? super T> comparator) {
        ArrayList<T> list = new ArrayList<T>(collection);
        Collections.sort(list, comparator);
        return list;
    }
    
}
