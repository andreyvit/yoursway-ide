/*
 *  Copyright 2001-2004 The Apache Software Foundation
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
package org.apache.commons.collections;

import java.util.Map;
import java.util.TreeMap;

import junit.framework.Test;

/**
 * Test FastTreeMap in <strong>fast</strong> mode.
 * 
 * @version $Revision: 155406 $ $Date: 2005-02-26 12:55:26 +0000 (Sat, 26 Feb 2005) $
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 */
public class TestFastTreeMap1 extends TestFastTreeMap {
    
    public TestFastTreeMap1(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestFastTreeMap1.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestFastTreeMap1.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public Map makeEmptyMap() {
        FastTreeMap ftm = new FastTreeMap();
        ftm.setFast(true);
        return (ftm);
    }

    public void setUp() {
        map = (TreeMap) makeEmptyMap();
    }

}
