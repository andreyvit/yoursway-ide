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
 * $Id: SafeConstructorImpl.java,v 1.4 2006/09/30 14:13:35 olabini Exp $
 */
package org.jvyaml;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jvyaml.nodes.Node;

import org.jvyaml.util.Base64Coder;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.4 $
 */
public class SafeConstructorImpl extends BaseConstructorImpl {
    private final static Map yamlConstructors = new HashMap();
    private final static Map yamlMultiConstructors = new HashMap();
    private final static Map yamlMultiRegexps = new HashMap();
    public YamlConstructor getYamlConstructor(final Object key) {
        YamlConstructor mine = (YamlConstructor)yamlConstructors.get(key);
        if(mine == null) {
            mine = super.getYamlConstructor(key);
        }
        return mine;
    }

    public YamlMultiConstructor getYamlMultiConstructor(final Object key) {
        YamlMultiConstructor mine = (YamlMultiConstructor)yamlMultiConstructors.get(key);
        if(mine == null) {
            mine = super.getYamlMultiConstructor(key);
        }
        return mine;
    }

    public Pattern getYamlMultiRegexp(final Object key) {
        Pattern mine = (Pattern)yamlMultiRegexps.get(key);
        if(mine == null) {
            mine = super.getYamlMultiRegexp(key);
        }
        return mine;
    }

    public Set getYamlMultiRegexps() {
        final Set all = new HashSet(super.getYamlMultiRegexps());
        all.addAll(yamlMultiRegexps.keySet());
        return all;
    }

    public static void addConstructor(final String tag, final YamlConstructor ctor) {
        yamlConstructors.put(tag,ctor);
    }

    public static void addMultiConstructor(final String tagPrefix, final YamlMultiConstructor ctor) {
        yamlMultiConstructors.put(tagPrefix,ctor);
        yamlMultiRegexps.put(tagPrefix,Pattern.compile("^"+tagPrefix));
    }

    public SafeConstructorImpl(final Composer composer) {
        super(composer);
    }

    private final static Map BOOL_VALUES = new HashMap();
    static {
        //        BOOL_VALUES.put("y",Boolean.TRUE);
        //        BOOL_VALUES.put("n",Boolean.FALSE);
        BOOL_VALUES.put("yes",Boolean.TRUE);
        BOOL_VALUES.put("no",Boolean.FALSE);
        BOOL_VALUES.put("true",Boolean.TRUE);
        BOOL_VALUES.put("false",Boolean.FALSE);
        BOOL_VALUES.put("on",Boolean.TRUE);
        BOOL_VALUES.put("off",Boolean.FALSE);
    }

    public static Object constructYamlNull(final Constructor ctor, final Node node) {
        return null;
    }
    
    public static Object constructYamlBool(final Constructor ctor, final Node node) {
        final String val = (String)ctor.constructScalar(node);
        return BOOL_VALUES.get(val.toLowerCase());
    }

    public static Object constructYamlOmap(final Constructor ctor, final Node node) {
        return ctor.constructPairs(node);
    }

    public static Object constructYamlPairs(final Constructor ctor, final Node node) {
        return constructYamlOmap(ctor,node);
    }

    public static Object constructYamlSet(final Constructor ctor, final Node node) {
        return ((Map)ctor.constructMapping(node)).keySet();
    }

    public static Object constructYamlStr(final Constructor ctor, final Node node) {
        final String value = (String)ctor.constructScalar(node);
        return value.length() == 0 ? (String)null : value;
    }

    public static Object constructYamlSeq(final Constructor ctor, final Node node) {
        return ctor.constructSequence(node);
    }

    public static Object constructYamlMap(final Constructor ctor, final Node node) {
        return ctor.constructMapping(node);
    }

    public static Object constructUndefined(final Constructor ctor, final Node node) {
        throw new ConstructorException(null,"could not determine a constructor for the tag " + node.getTag(),null);
    }

    private final static Pattern TIMESTAMP_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)(?:(?:[Tt]|[ \t]+)([0-9][0-9]?):([0-9][0-9]):([0-9][0-9])(?:\\.([0-9]*))?(?:[ \t]*(?:Z|([-+][0-9][0-9]?)(?::([0-9][0-9])?)?))?)?$");
    private final static Pattern YMD_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)$");
    public static Object constructYamlTimestamp(final Constructor ctor, final Node node) {
        Matcher match = YMD_REGEXP.matcher((String)node.getValue());
        if(match.matches()) {
            final String year_s = match.group(1);
            final String month_s = match.group(2);
            final String day_s = match.group(3);
            final Calendar cal = Calendar.getInstance();
            cal.clear();
            if(year_s != null) {
                cal.set(Calendar.YEAR,Integer.parseInt(year_s));
            }
            if(month_s != null) {
                cal.set(Calendar.MONTH,Integer.parseInt(month_s)-1); // Java's months are zero-based...
            }
            if(day_s != null) {
                cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(day_s));
            }
            return cal.getTime();
        }
        match = TIMESTAMP_REGEXP.matcher((String)node.getValue());
        if(!match.matches()) {
            return ctor.constructPrivateType(node);
        }
        final String year_s = match.group(1);
        final String month_s = match.group(2);
        final String day_s = match.group(3);
        final String hour_s = match.group(4);
        final String min_s = match.group(5);
        final String sec_s = match.group(6);
        final String fract_s = match.group(7);
        final String timezoneh_s = match.group(8);
        final String timezonem_s = match.group(9);
        
        int usec = 0;
        if(fract_s != null) {
            usec = Integer.parseInt(fract_s);
            if(usec != 0) {
                while(10*usec < 1000) {
                    usec *= 10;
                }
            }
        }
        final Calendar cal = Calendar.getInstance();
        if(year_s != null) {
            cal.set(Calendar.YEAR,Integer.parseInt(year_s));
        }
        if(month_s != null) {
            cal.set(Calendar.MONTH,Integer.parseInt(month_s)-1); // Java's months are zero-based...
        }
        if(day_s != null) {
            cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(day_s));
        }
        if(hour_s != null) {
            cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hour_s));
        }
        if(min_s != null) {
            cal.set(Calendar.MINUTE,Integer.parseInt(min_s));
        }
        if(sec_s != null) {
            cal.set(Calendar.SECOND,Integer.parseInt(sec_s));
        }
        cal.set(Calendar.MILLISECOND,usec);
        if(timezoneh_s != null || timezonem_s != null) {
            int zone = 0;
            int sign = +1;
            if(timezoneh_s != null) {
                if(timezoneh_s.startsWith("-")) {
                    sign = -1;
                }
                zone += Integer.parseInt(timezoneh_s.substring(1))*3600000;
            }
            if(timezonem_s != null) {
                zone += Integer.parseInt(timezonem_s)*60000;
            }
            cal.set(Calendar.ZONE_OFFSET,sign*zone);
        }
        return cal.getTime();
    }

    public static Object constructYamlInt(final Constructor ctor, final Node node) {
        String value = ctor.constructScalar(node).toString().replaceAll("_","");
        int sign = +1;
        char first = value.charAt(0);
        if(first == '-') {
            sign = -1;
            value = value.substring(1);
        } else if(first == '+') {
            value = value.substring(1);
        }
        int base = 10;
        if(value.equals("0")) {
            return new Long(0);
        } else if(value.startsWith("0b")) {
            value = value.substring(2);
            base = 2;
        } else if(value.startsWith("0x")) {
            value = value.substring(2);
            base = 16;
        } else if(value.startsWith("0")) {
            value = value.substring(1);
            base = 8;
        } else if(value.indexOf(':') != -1) {
            final String[] digits = value.split(":");
            int bes = 1;
            int val = 0;
            for(int i=0,j=digits.length;i<j;i++) {
                val += (Long.parseLong(digits[(j-i)-1])*bes);
                bes *= 60;
            }
            return new Integer(sign*val);
        } else {
            return new Long(sign * Long.parseLong(value));
        }
        return new Long(sign * Long.parseLong(value,base));
    }

    private final static Double INF_VALUE_POS = new Double(Double.POSITIVE_INFINITY);
    private final static Double INF_VALUE_NEG = new Double(Double.NEGATIVE_INFINITY);
    private final static Double NAN_VALUE = new Double(Double.NaN);

    public static Object constructYamlFloat(final Constructor ctor, final Node node) {
        String value = ctor.constructScalar(node).toString().replaceAll("_","");
        int sign = +1;
        char first = value.charAt(0);
        if(first == '-') {
            sign = -1;
            value = value.substring(1);
        } else if(first == '+') {
            value = value.substring(1);
        }
        final String valLower = value.toLowerCase();
        if(valLower.equals(".inf")) {
            return sign == -1 ? INF_VALUE_NEG : INF_VALUE_POS;
        } else if(valLower.equals(".nan")) {
            return NAN_VALUE;
        } else if(value.indexOf(':') != -1) {
            final String[] digits = value.split(":");
            int bes = 1;
            double val = 0.0;
            for(int i=0,j=digits.length;i<j;i++) {
                val += (Double.parseDouble(digits[(j-i)-1])*bes);
                bes *= 60;
            }
            return new Double(sign*val);
        } else {
            return Double.valueOf(value);
        }
    }    

    public static Object constructYamlBinary(final Constructor ctor, final Node node) {
        final String[] values = ctor.constructScalar(node).toString().split("[\n\u0085]|(?:\r[^\n])");
        final StringBuffer vals = new StringBuffer();
        for(int i=0,j=values.length;i<j;i++) {
            vals.append(values[i].trim());
        }
        return Base64Coder.decode(vals.toString());
    }

    public static Object constructSpecializedSequence(final Constructor ctor, final String pref, final Node node) {
        List outp = null;
        try {
            final Class seqClass = Class.forName(pref);
            outp = (List)seqClass.newInstance();
        } catch(final Exception e) {
            throw new YAMLException("Can't construct a sequence from class " + pref + ": " + e.toString());
        }
        outp.addAll((List)ctor.constructSequence(node));
        return outp;
    }

    public static Object constructSpecializedMap(final Constructor ctor, final String pref, final Node node) {
        Map outp = null;
        try {
            final Class mapClass = Class.forName(pref);
            outp = (Map)mapClass.newInstance();
        } catch(final Exception e) {
            throw new YAMLException("Can't construct a mapping from class " + pref + ": " + e.toString());
        }
        outp.putAll((Map)ctor.constructMapping(node));
        return outp;
    }

    private static Object fixValue(final Object inp, final Class outp) {
        if(inp == null) {
            return null;
        }
        final Class inClass = inp.getClass();
        if(outp.isAssignableFrom(inClass)) {
            return inp;
        }
        if(inClass == Long.class && (outp == Integer.class || outp == Integer.TYPE)) {
            return new Integer(((Long)inp).intValue());
        }
        if(inClass == Long.class && (outp == Short.class || outp == Short.TYPE)) {
            return new Short((short)((Long)inp).intValue());
        }
        if(inClass == Long.class && (outp == Character.class || outp == Character.TYPE)) {
            return new Character((char)((Long)inp).intValue());
        }
        if(inClass == Double.class && (outp == Float.class || outp == Float.TYPE)) {
            return new Float(((Double)inp).floatValue());
        }
        return inp;
    }

    public static Object constructJava(final Constructor ctor, final String pref, final Node node) {
        Object outp = null;
        try {
            final Class cl = Class.forName(pref);
            outp = cl.newInstance();
            final Map values = (Map)ctor.constructMapping(node);
            java.lang.reflect.Method[] ems = cl.getMethods();
            for(final Iterator iter = values.keySet().iterator();iter.hasNext();) {
                final Object key = iter.next();
                final Object value = values.get(key);
                final String keyName = key.toString();
                final String mName = "set" + Character.toUpperCase(keyName.charAt(0)) + keyName.substring(1);
                for(int i=0,j=ems.length;i<j;i++) {
                    if(ems[i].getName().equals(mName) && ems[i].getParameterTypes().length == 1) {
                        ems[i].invoke(outp, new Object[]{fixValue(value, ems[i].getParameterTypes()[0])});
                        break;
                    }
                }
            }
        } catch(final Exception e) {
            throw new YAMLException("Can't construct a java object from class " + pref + ": " + e.toString());
        }
        return outp;
    }

    static {
        BaseConstructorImpl.addConstructor("tag:yaml.org,2002:null",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlNull(self,node);
                }
            });
        addConstructor("tag:yaml.org,2002:bool",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlBool(self,node);
                }
            });
        addConstructor("tag:yaml.org,2002:omap",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlOmap(self,node);
                }
            });
        addConstructor("tag:yaml.org,2002:pairs",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlPairs(self,node);
                }
            });
        addConstructor("tag:yaml.org,2002:set",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlSet(self,node);
                }
            });
        addConstructor("tag:yaml.org,2002:int",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlInt(self,node);
                }
            });
        addConstructor("tag:yaml.org,2002:float",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlFloat(self,node);
                }
            });
        addConstructor("tag:yaml.org,2002:timestamp",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlTimestamp(self,node);
                }
            });
        addConstructor("tag:yaml.org,2002:timestamp#ymd",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlTimestamp(self,node);
                }
            });
        addConstructor("tag:yaml.org,2002:str",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlStr(self,node);
                }
            });
        addConstructor("tag:yaml.org,2002:binary",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlBinary(self,node);
                }
            });
        addConstructor("tag:yaml.org,2002:seq",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlSeq(self,node);
                }
            });
        addConstructor("tag:yaml.org,2002:map",new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return constructYamlMap(self,node);
                }
            });
        addConstructor(null,new YamlConstructor() {
                public Object call(final Constructor self, final Node node) {
                    return self.constructPrivateType(node);
                }
            });

        addMultiConstructor("tag:yaml.org,2002:seq:",new YamlMultiConstructor() {
                public Object call(final Constructor self, final String pref, final Node node) {
                    return constructSpecializedSequence(self,pref,node);
                }
            });
        addMultiConstructor("tag:yaml.org,2002:map:",new YamlMultiConstructor() {
                public Object call(final Constructor self, final String pref, final Node node) {
                    return constructSpecializedMap(self,pref,node);
                }
            });
        addMultiConstructor("!java/object:",new YamlMultiConstructor() {
                public Object call(final Constructor self, final String pref, final Node node) {
                    return constructJava(self,pref,node);
                }
            });
    }

    public static void main(final String[] args) throws Exception {
        final String filename = args[0];
        System.out.println("Reading of file: \"" + filename + "\"");

        final StringBuffer input = new StringBuffer();
        final java.io.Reader reader = new java.io.FileReader(filename);
        char[] buff = new char[1024];
        int read = 0;
        while(true) {
            read = reader.read(buff);
            input.append(buff,0,read);
            if(read < 1024) {
                break;
            }
        }
        reader.close();
        final String str = input.toString();
        //        final long before = System.currentTimeMillis();
        //        for(int i=0;i<1;i++) {
            final Constructor ctor = new SafeConstructorImpl(new ComposerImpl(new ParserImpl(new ScannerImpl(str)),new ResolverImpl()));
            for(final Iterator iter = ctor.eachDocument();iter.hasNext();) {
                System.out.println(iter.next());
            }
            //        }
            //        final long after = System.currentTimeMillis();
            //        final long time = after-before;
            //        final double timeS = (after-before)/1000.0;
            //        System.out.println("Walking through the nodes for the file: " + filename + " took " + time + "ms, or " + timeS + " seconds"); 
    }
}// SafeConstructorImpl
