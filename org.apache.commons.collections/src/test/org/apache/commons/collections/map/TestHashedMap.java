/*
 *  Copyright 2001-2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.commons.collections.map;

import java.util.Map;

import junit.framework.Test;
import junit.textui.TestRunner;

import org.apache.commons.collections.BulkTest;

/**
 * JUnit tests.
 * 
 * @version $Revision: 171349 $ $Date: 2005-05-22 18:48:56 +0100 (Sun, 22 May 2005) $
 * 
 * @author Stephen Colebourne
 */
public class TestHashedMap extends AbstractTestIterableMap {

    public TestHashedMap(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        return BulkTest.makeSuite(TestHashedMap.class);
    }

    public Map makeEmptyMap() {
        return new HashedMap();
    }
    
    public String getCompatibilityVersion() {
        return "3";
    }

    public void testClone() {
        HashedMap map = new HashedMap(10);
        map.put("1", "1");
        Map cloned = (Map) map.clone();
        assertEquals(map.size(), cloned.size());
        assertSame(map.get("1"), cloned.get("1"));
    }

    public void testInternalState() {
        HashedMap map = new HashedMap(42, 0.75f);
        assertEquals(0.75f, map.loadFactor, 0.1f);
        assertEquals(0, map.size);
        assertEquals(64, map.data.length);
        assertEquals(48, map.threshold);
        assertEquals(0, map.modCount);
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) map, "D:/dev/collections/data/test/HashedMap.emptyCollection.version3.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) map, "D:/dev/collections/data/test/HashedMap.fullCollection.version3.obj");
//    }
}
