/*
 * Copyright (c) 2006 Ola Bini
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
 * $Id: RepresenterImpl.java,v 1.2 2006/09/24 16:32:35 olabini Exp $
 */
package org.jvyaml;

import java.io.IOException;
import java.io.Serializable;

import java.lang.reflect.Method;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.Date;
import java.util.Calendar;

import org.jvyaml.nodes.Node;
import org.jvyaml.nodes.CollectionNode;
import org.jvyaml.nodes.MappingNode;
import org.jvyaml.nodes.ScalarNode;
import org.jvyaml.nodes.SequenceNode;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.2 $
 */
public class RepresenterImpl implements Representer {
    private final Serializer serializer;
    private final char defaultStyle;
    private final Map representedObjects;

    public RepresenterImpl(final Serializer serializer, final YAMLConfig opts) {
        this.serializer = serializer;
        this.defaultStyle = opts.useDouble() ? '"' : (opts.useSingle() ? '\'' : 0);
        this.representedObjects = new HashMap();
    }

    private Node representData(final Object data) throws IOException {
        String aliasKey = null;
        Node node = null;

        if(!ignoreAliases(data)) {
            aliasKey = ""+System.identityHashCode(data);
        }

        if(null != aliasKey) {
            if(this.representedObjects.containsKey(aliasKey)) {
                node = (Node)this.representedObjects.get(aliasKey);
                if(null == node) {
                    throw new RepresenterException("recursive objects are not allowed: " + data);
                }
                return node;
            }
            this.representedObjects.put(aliasKey,null);
        }

        node = getNodeCreatorFor(data).toYamlNode(this);

        if(aliasKey != null) {
            this.representedObjects.put(aliasKey,node);
        }

        return node;
    }

    public Node scalar(final String tag, final String value, char style) throws IOException {
        return representScalar(tag,value,style);
    }

    public Node representScalar(final String tag, final String value, char style) throws IOException {
        char realStyle = style == 0 ? this.defaultStyle : style;
        return new ScalarNode(tag,value,style);
    }

    public Node seq(final String tag, final List sequence, final boolean flowStyle) throws IOException {
        return representSequence(tag,sequence,flowStyle);
    }

    public Node representSequence(final String tag, final List sequence, final boolean flowStyle) throws IOException {
        List value = new ArrayList(sequence.size());
        for(final Iterator iter = sequence.iterator();iter.hasNext();) {
            value.add(representData(iter.next()));
        }
        return new SequenceNode(tag,value,flowStyle);
    }

    public Node map(final String tag, final Map mapping, final boolean flowStyle) throws IOException {
        return representMapping(tag,mapping,flowStyle);
    }

    public Node representMapping(final String tag, final Map mapping, final boolean flowStyle) throws IOException {
        Map value = new HashMap();
        for(final Iterator iter = mapping.keySet().iterator();iter.hasNext();) {
            final Object itemKey = iter.next();
            final Object itemValue = mapping.get(itemKey);
            value.put(representData(itemKey),representData(itemValue));
        }
        return new MappingNode(tag,value,flowStyle);
    }

    public void represent(final Object data) throws IOException {
        Node node = representData(data);
        this.serializer.serialize(node);
        this.representedObjects.clear();
    }

    protected boolean ignoreAliases(final Object data) {
        return false;
    }

    protected YAMLNodeCreator getNodeCreatorFor(final Object data) {
        if(data instanceof YAMLNodeCreator) {
            return (YAMLNodeCreator)data;
        } else if(data instanceof Map) {
            return new MappingYAMLNodeCreator(data);
        } else if(data instanceof List) {
            return new SequenceYAMLNodeCreator(data);
        } else if(data instanceof Set) {
            return new SetYAMLNodeCreator(data);
        } else if(data instanceof Date) {
            return new DateYAMLNodeCreator(data);
        } else if(data instanceof String) {
            return new StringYAMLNodeCreator(data);
        } else if(data instanceof Number) {
            return new NumberYAMLNodeCreator(data);
        } else if(data instanceof Boolean) {
            return new ScalarYAMLNodeCreator("tag:yaml.org,2002:bool",data);
        } else if(data == null) {
            return new ScalarYAMLNodeCreator("tag:yaml.org,2002:null","");
        } else if(data.getClass().isArray()) {
            return new ArrayYAMLNodeCreator(data);
        } else { // Fallback, handles JavaBeans and other
            return new JavaBeanYAMLNodeCreator(data);
        }
    }

    public static class DateYAMLNodeCreator implements YAMLNodeCreator {
        private final Date data;
        public DateYAMLNodeCreator(final Object data) {
            this.data = (Date)data;
        }

        public String taguri() {
            return "tag:yaml.org,2002:timestamp";
        }

        private static DateFormat dateOutput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        private static DateFormat dateOutputUsec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        public Node toYamlNode(final Representer representer) throws IOException {
            final Calendar c = Calendar.getInstance();
            c.setTime(data);
            String out = null;
            if(c.get(Calendar.MILLISECOND) != 0) {
                out = dateOutputUsec.format(data);
            } else {
                out = dateOutput.format(data);
            }
            out = out.substring(0, 23) + ":" + out.substring(23);
            return representer.scalar(taguri(), out, (char)0);
        }
    }

    public static class SetYAMLNodeCreator implements YAMLNodeCreator {
        private final Set data;
        public SetYAMLNodeCreator(final Object data) {
            this.data = (Set)data;
        }

        public String taguri() {
            return "tag:yaml.org,2002:set";
        }

        public Node toYamlNode(final Representer representer) throws IOException {
            final Map entries = new HashMap();
            for(final Iterator iter = data.iterator();iter.hasNext();) {
                entries.put(iter.next(),null);
            }
            return representer.map(taguri(), entries, false);
        }
    }

    public static class ArrayYAMLNodeCreator implements YAMLNodeCreator {
        private final Object data;
        public ArrayYAMLNodeCreator(final Object data) {
            this.data = data;
        }

        public String taguri() {
            return "tag:yaml.org,2002:seq";
        }

        public Node toYamlNode(final Representer representer) throws IOException {
            final int l = java.lang.reflect.Array.getLength(data);
            final List lst = new ArrayList(l);
            for(int i=0;i<l;i++) {
                lst.add(java.lang.reflect.Array.get(data,i));
            }
            return representer.seq(taguri(), lst, false);
        }
    }

    public static class NumberYAMLNodeCreator implements YAMLNodeCreator {
        private final Number data;
        public NumberYAMLNodeCreator(final Object data) {
            this.data = (Number)data;
        }

        public String taguri() {
            if(data instanceof Float || data instanceof Double || data instanceof java.math.BigDecimal) {
                return "tag:yaml.org,2002:float";
            } else {
                return "tag:yaml.org,2002:int";
            }
        }

        public Node toYamlNode(Representer representer) throws IOException {
            String str = data.toString();
            if(str.equals("Infinity")) {
                str = ".inf";
            } else if(str.equals("-Infinity")) {
                str = "-.inf";
            } else if(str.equals("NaN")) {
                str = ".nan";
            }
            return representer.scalar(taguri(), str, (char)0);
        }
    }

    public static class ScalarYAMLNodeCreator implements YAMLNodeCreator {
        private final String tag;
        private final Object data;
        public ScalarYAMLNodeCreator(final String tag, final Object data) {
            this.tag = tag;
            this.data = data;
        }

        public String taguri() {
            return this.tag;
        }

        public Node toYamlNode(Representer representer) throws IOException {
            return representer.scalar(taguri(), data.toString(), (char)0);
        }
    }

    public static class StringYAMLNodeCreator implements YAMLNodeCreator {
        private final Object data;
        public StringYAMLNodeCreator(final Object data) {
            this.data = data;
        }

        public String taguri() {
            if(data instanceof String) {
                return "tag:yaml.org,2002:str";
            } else {
                return "tag:yaml.org,2002:str:"+data.getClass().getName();
            }
        }

        public Node toYamlNode(Representer representer) throws IOException {
            return representer.scalar(taguri(), data.toString(), (char)0);
        }
    }

    public static class SequenceYAMLNodeCreator implements YAMLNodeCreator {
        private final List data;
        public SequenceYAMLNodeCreator(final Object data) {
            this.data = (List)data;
        }

        public String taguri() {
            if(data instanceof ArrayList) {
                return "tag:yaml.org,2002:seq";
            } else {
                return "tag:yaml.org,2002:seq:"+data.getClass().getName();
            }
        }

        public Node toYamlNode(Representer representer) throws IOException {
            return representer.seq(taguri(), data, false);
        }
    }

    public static class MappingYAMLNodeCreator implements YAMLNodeCreator {
        private final Map data;
        public MappingYAMLNodeCreator(final Object data) {
            this.data = (Map)data;
        }

        public String taguri() {
            if(data instanceof HashMap) {
                return "tag:yaml.org,2002:map";
            } else {
                return "tag:yaml.org,2002:map:"+data.getClass().getName();
            }
        }

        public Node toYamlNode(Representer representer) throws IOException {
            return representer.map(taguri(), data, false);
        }
    }

    public static class JavaBeanYAMLNodeCreator implements YAMLNodeCreator {
        private final Object data;
        public JavaBeanYAMLNodeCreator(final Object data) {
            this.data = data;
        }

        public String taguri() {
            return "!java/object:" + data.getClass().getName();
        }

        public Node toYamlNode(Representer representer) throws IOException {
            final Map values = new HashMap();
            final Method[] ems = data.getClass().getMethods();
            for(int i=0,j=ems.length;i<j;i++) {
                if(ems[i].getParameterTypes().length == 0) {
                    final String name = ems[i].getName();
                    if(name.equals("getClass")) {
                        continue;
                    }
                    String pname = null;
                    if(name.startsWith("get")) {
                        pname = "" + Character.toLowerCase(name.charAt(3)) + name.substring(4);
                    } else if(name.startsWith("is")) {
                        pname = "" + Character.toLowerCase(name.charAt(2)) + name.substring(3);
                    }
                    if(null != pname) {
                        try {
                            values.put(pname, ems[i].invoke(data,null));
                        } catch(final Exception exe) {
                            values.put(pname, null);
                        }
                    }
                }
            }
            return representer.map(taguri(),values,false);
        }
    }

    public static void main(final String[] args) throws IOException {
        final YAMLConfig cfg = YAML.config();
        final Serializer s = new SerializerImpl(new EmitterImpl(new java.io.OutputStreamWriter(System.out),cfg),new ResolverImpl(),cfg);
        s.open();
        final Representer r = new RepresenterImpl(s, cfg);
        final Map test1 = new HashMap();
        final List test1Val = new LinkedList();
        test1Val.add("hello");
        test1Val.add(Boolean.TRUE);
        test1Val.add(new Integer(31337));
        test1.put("val1",test1Val);
        final List test2Val = new ArrayList();
        test2Val.add("hello");
        test2Val.add(Boolean.FALSE);
        test2Val.add(new Integer(31337));
        test1.put("val2",test2Val);
        test1.put("afsdf", "hmm");
        TestJavaBean bean1 = new TestJavaBean();
        bean1.setName("Ola");
        bean1.setSurName("Bini");
        bean1.setAge(24);
        test1.put(new Integer(25),bean1);
        r.represent(test1);
        s.close();
    }
    private static class TestJavaBean implements Serializable {
        private String val1;
        private String val2;
        private int val3;
        public TestJavaBean() {
        }
        public void setName(final String name) {
            this.val1 = name;
        }
        public String getName() {
            return this.val1;
        }

        public void setSurName(final String sname) {
            this.val2 = sname;
        }
        public String getSurName() {
            return this.val2;
        }

        public void setAge(final int age) {
            this.val3 = age;
        }
        public int getAge() {
            return this.val3;
        }
    }
}// RepresenterImpl

