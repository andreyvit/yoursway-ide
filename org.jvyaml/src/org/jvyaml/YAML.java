/* Copyright (c) 2006 Ola Bini
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"), to deal in 
 * the Software without restriction, including without limitation the rights to 
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE.
 */
/**
 * $Id: YAML.java,v 1.5 2006/09/24 16:32:35 olabini Exp $
 */
package org.jvyaml;

import java.io.Reader;
import java.io.Writer;
import java.io.StringWriter;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * The combinatorial explosion class.
 *
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.5 $
 */
public class YAML {
    public static final String DEFAULT_SCALAR_TAG = "tag:yaml.org,2002:str";
    public static final String DEFAULT_SEQUENCE_TAG = "tag:yaml.org,2002:seq";
    public static final String DEFAULT_MAPPING_TAG = "tag:yaml.org,2002:map";

    public static final Map ESCAPE_REPLACEMENTS = new HashMap();

    static {
        ESCAPE_REPLACEMENTS.put(new Character('\0'),"0");
        ESCAPE_REPLACEMENTS.put(new Character('\u0007'),"a");
        ESCAPE_REPLACEMENTS.put(new Character('\u0008'),"b");
        ESCAPE_REPLACEMENTS.put(new Character('\u0009'),"t");
        ESCAPE_REPLACEMENTS.put(new Character('\n'),"n");
        ESCAPE_REPLACEMENTS.put(new Character('\u000B'),"v");
        ESCAPE_REPLACEMENTS.put(new Character('\u000C'),"f");
        ESCAPE_REPLACEMENTS.put(new Character('\r'),"r");
        ESCAPE_REPLACEMENTS.put(new Character('\u001B'),"e");
        ESCAPE_REPLACEMENTS.put(new Character('"'),"\"");
        ESCAPE_REPLACEMENTS.put(new Character('\\'),"\\");
        ESCAPE_REPLACEMENTS.put(new Character('\u0085'),"N");
        ESCAPE_REPLACEMENTS.put(new Character('\u00A0'),"_");
    }

    public static YAMLConfig config() {
        return defaultConfig();
    }

    public static YAMLConfig defaultConfig() {
        return new DefaultYAMLConfig();
    }

    public static String dump(final Object data) {
        return dump(data,config());
    }

    public static String dump(final Object data, final YAMLFactory fact) {
        return dump(data,fact, config());
    }

    public static String dump(final Object data, final YAMLConfig cfg) {
        final List lst = new ArrayList(1);
        lst.add(data);
        return dumpAll(lst,cfg);
    }

    public static String dump(final Object data, final YAMLFactory fact, final YAMLConfig cfg) {
        final List lst = new ArrayList(1);
        lst.add(data);
        return dumpAll(lst,fact,cfg);
    }

    public static String dumpAll(final List data) {
        return dumpAll(data,config());
    }

    public static String dumpAll(final List data, final YAMLFactory fact) {
        return dumpAll(data,fact,config());
    }

    public static String dumpAll(final List data, final YAMLConfig cfg) {
        final StringWriter swe = new StringWriter();
        dumpAll(data,swe,cfg);
        return swe.toString();
    }

    public static String dumpAll(final List data, final YAMLFactory fact, final YAMLConfig cfg) {
        final StringWriter swe = new StringWriter();
        dumpAll(data,swe,fact,cfg);
        return swe.toString();
    }

    public static void dump(final Object data, final Writer output) {
        dump(data,output,config());
    }

    public static void dump(final Object data, final Writer output, YAMLFactory fact) {
        dump(data,output,fact,config());
    }

    public static void dump(final Object data, final Writer output, final YAMLConfig cfg) {
        final List lst = new ArrayList(1);
        lst.add(data);
        dumpAll(lst,output,cfg);
    }

    public static void dump(final Object data, final Writer output, final YAMLFactory fact, final YAMLConfig cfg) {
        final List lst = new ArrayList(1);
        lst.add(data);
        dumpAll(lst,output,fact,cfg);
    }

    public static void dumpAll(final List data, final Writer output) {
        dumpAll(data,output,config());
    }

    public static void dumpAll(final List data, final Writer output, final YAMLFactory fact) {
        dumpAll(data,output,fact,config());
    }

    public static void dumpAll(final List data, final Writer output, final YAMLConfig cfg) {
        dumpAll(data,output,new DefaultYAMLFactory(),cfg);
    }

    public static void dumpAll(final List data, final Writer output, final YAMLFactory fact, final YAMLConfig cfg) {
        final Serializer s = fact.createSerializer(fact.createEmitter(output,cfg),fact.createResolver(),cfg);
        try {
            s.open();
            final Representer r = fact.createRepresenter(s,cfg);
            for(final Iterator iter = data.iterator(); iter.hasNext();) {
                r.represent(iter.next());
            }
        } catch(final java.io.IOException e) {
            throw new YAMLException(e);
        } finally {
            try { s.close(); } catch(final java.io.IOException e) {/*Nothing to do in this situation*/}
        }
    }

    public static Object load(final String io) {
        return load(io, new DefaultYAMLFactory(),config());
    }

    public static Object load(final Reader io) {
        return load(io, new DefaultYAMLFactory(),config());
    }

    public static Object load(final String io, final YAMLConfig cfg) {
        return load(io, new DefaultYAMLFactory(),cfg);
    }

    public static Object load(final Reader io, final YAMLConfig cfg) {
        return load(io, new DefaultYAMLFactory(),cfg);
    }

    public static Object load(final String io, final YAMLFactory fact, final YAMLConfig cfg) {
        final Constructor ctor = fact.createConstructor(fact.createComposer(fact.createParser(fact.createScanner(io),cfg),fact.createResolver()));
        if(ctor.checkData()) {
            return ctor.getData();
        } else {
            return null;
        }
    }

    public static Object load(final Reader io, final YAMLFactory fact, final YAMLConfig cfg) {
        final Constructor ctor = fact.createConstructor(fact.createComposer(fact.createParser(fact.createScanner(io),cfg),fact.createResolver()));
        if(ctor.checkData()) {
            return ctor.getData();
        } else {
            return null;
        }
    }

    public static List loadAll(final String io) {
        return loadAll(io, new DefaultYAMLFactory(),config());
    }

    public static List loadAll(final Reader io) {
        return loadAll(io, new DefaultYAMLFactory(),config());
    }

    public static List loadAll(final String io, final YAMLConfig cfg) {
        return loadAll(io, new DefaultYAMLFactory(),cfg);
    }

    public static List loadAll(final Reader io, final YAMLConfig cfg) {
        return loadAll(io, new DefaultYAMLFactory(),cfg);
    }

    public static List loadAll(final String io, final YAMLFactory fact, final YAMLConfig cfg) {
        final List result = new ArrayList();
        final Constructor ctor = fact.createConstructor(fact.createComposer(fact.createParser(fact.createScanner(io),cfg),fact.createResolver()));
        while(ctor.checkData()) {
            result.add(ctor.getData());
        }
        return result;
    }

    public static List loadAll(final Reader io, final YAMLFactory fact, final YAMLConfig cfg) {
        final List result = new ArrayList();
        final Constructor ctor = fact.createConstructor(fact.createComposer(fact.createParser(fact.createScanner(io),cfg),fact.createResolver()));
        while(ctor.checkData()) {
            result.add(ctor.getData());
        }
        return result;
    }
}// YAML
