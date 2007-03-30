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
 * $Id: ResolverImpl.java,v 1.3 2006/09/30 14:13:35 olabini Exp $
 */
package org.jvyaml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

import java.util.regex.Pattern;

import org.jvyaml.nodes.MappingNode;
import org.jvyaml.nodes.Node;
import org.jvyaml.nodes.ScalarNode;
import org.jvyaml.nodes.SequenceNode;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.3 $
 */
public class ResolverImpl implements Resolver {
    private final static Map yamlImplicitResolvers = new HashMap();
    private final static Map yamlPathResolvers = new HashMap();

    private List resolverExactPaths = new LinkedList();
    private List resolverPrefixPaths = new LinkedList();

    public static void addImplicitResolver(final String tag, final Pattern regexp, final String first) {
        final String firstVal = first;
        if(firstVal == null || "".equals("firstVal")) {
            List curr = (List)yamlImplicitResolvers.get("");
            if(curr == null) {
                curr = new LinkedList();
                yamlImplicitResolvers.put("",curr);
            }
            curr.add(new Object[]{tag,regexp});
        } else {
            final char[] chrs = firstVal.toCharArray();
            for(int i=0,j=chrs.length;i<j;i++) {
                final Character theC = new Character(chrs[i]);
                List curr = (List)yamlImplicitResolvers.get(theC);
                if(curr == null) {
                    curr = new LinkedList();
                    yamlImplicitResolvers.put(theC,curr);
                }
                curr.add(new Object[]{tag,regexp});
            }
        }
    }

    public static void addPathResolver(final String tag, final List path, final Class kind) {
        final List newPath = new LinkedList();
        Object nodeCheck=null;
        Object indexCheck=null;
        for(final Iterator iter = path.iterator();iter.hasNext();) {
            final Object element = iter.next();
            if(element instanceof List) {
                final List eList = (List)element;
                if(eList.size() == 2) {
                    nodeCheck = eList.get(0);
                    indexCheck = eList.get(1);
                } else if(eList.size() == 1) {
                    nodeCheck = eList.get(0);
                    indexCheck = Boolean.TRUE;
                } else {
                    throw new ResolverException("Invalid path element: " + element);
                }
            } else {
                nodeCheck = null;
                indexCheck = element;
            }

            if(nodeCheck instanceof String) {
                nodeCheck = ScalarNode.class;
            } else if(nodeCheck instanceof List) {
                nodeCheck = SequenceNode.class;
            } else if(nodeCheck instanceof Map) {
                nodeCheck = MappingNode.class;
            } else if(null != nodeCheck && !ScalarNode.class.equals(nodeCheck) && !SequenceNode.class.equals(nodeCheck) && !MappingNode.class.equals(nodeCheck)) {
                throw new ResolverException("Invalid node checker: " + nodeCheck);
            }
            if(!(indexCheck instanceof String || indexCheck instanceof Integer) && null != indexCheck) {
                throw new ResolverException("Invalid index checker: " + indexCheck);
            }
            newPath.add(new Object[]{nodeCheck,indexCheck});
        }
        Class newKind = null;
        if(String.class.equals(kind)) {
            newKind = ScalarNode.class;
        } else if(List.class.equals(kind)) {
            newKind = SequenceNode.class;
        } else if(Map.class.equals(kind)) {
            newKind = MappingNode.class;
        } else if(kind != null && !ScalarNode.class.equals(kind) && !SequenceNode.class.equals(kind) && !MappingNode.class.equals(kind)) {
            throw new ResolverException("Invalid node kind: " + kind);
        } else {
            newKind = kind;
        }
        final List x = new ArrayList(1);
        x.add(newPath);
        final List y = new ArrayList(2);
        y.add(x);
        y.add(kind);
        yamlPathResolvers.put(y,tag);
    }

    public void descendResolver(final Node currentNode, final Object currentIndex) {
        final Map exactPaths = new HashMap();
        final List prefixPaths = new LinkedList();
        if(null != currentNode) {
            final int depth = resolverPrefixPaths.size();
            for(final Iterator iter = ((List)resolverPrefixPaths.get(0)).iterator();iter.hasNext();) {
                final Object[] obj = (Object[])iter.next();
                final List path = (List)obj[0];
                if(checkResolverPrefix(depth,path,(Class)obj[1],currentNode,currentIndex)) {
                    if(path.size() > depth) {
                        prefixPaths.add(new Object[] {path,obj[1]});
                    } else {
                        final List resPath = new ArrayList(2);
                        resPath.add(path);
                        resPath.add(obj[1]);
                        exactPaths.put(obj[1],yamlPathResolvers.get(resPath));
                    }
                }
            }
        } else {
            for(final Iterator iter = yamlPathResolvers.keySet().iterator();iter.hasNext();) {
                final List key = (List)iter.next();
                final List path = (List)key.get(0);
                final Class kind = (Class)key.get(1);
                if(null == path) {
                    exactPaths.put(kind,yamlPathResolvers.get(key));
                } else {
                    prefixPaths.add(key);
                }
            }
        }
        resolverExactPaths.add(0,exactPaths);
        resolverPrefixPaths.add(0,prefixPaths);
    }

    public void ascendResolver() {
        resolverExactPaths.remove(0);
        resolverPrefixPaths.remove(0);
    }

    public boolean checkResolverPrefix(final int depth, final List path, final Class kind, final Node currentNode, final Object currentIndex) {
        final Object[] check = (Object[])path.get(depth-1);
        final Object nodeCheck = check[0];
        final Object indexCheck = check[1];
        if(nodeCheck instanceof String) {
            if(!currentNode.getTag().equals(nodeCheck)) {
                return false;
            }
        } else if(null != nodeCheck) {
            if(!((Class)nodeCheck).isInstance(currentNode)) {
                return false;
            }
        }
        if(indexCheck == Boolean.TRUE && currentIndex != null) {
            return false;
        }
        if(indexCheck == Boolean.FALSE && currentIndex == null) {
            return false;
        }
        if(indexCheck instanceof String) {
            if(!(currentIndex instanceof ScalarNode && indexCheck.equals(((ScalarNode)currentIndex).getValue()))) {
                return false;
            }
        } else if(indexCheck instanceof Integer) {
            if(!currentIndex.equals(indexCheck)) {
                return false;
            }
        }
        return true;
    }
    
    public String resolve(final Class kind, final String value, final boolean[] implicit) {
        List resolvers = null;
        if(kind.equals(ScalarNode.class) && implicit[0]) {
            if("".equals(value)) {
                resolvers = (List)yamlImplicitResolvers.get("");
            } else {
                resolvers = (List)yamlImplicitResolvers.get(new Character(value.charAt(0)));
            }
            if(resolvers == null) {
                resolvers = new LinkedList();
            }
            if(yamlImplicitResolvers.containsKey(null)) {
                resolvers.addAll((List)yamlImplicitResolvers.get(null));
            }
            for(final Iterator iter = resolvers.iterator();iter.hasNext();) {
                final Object[] val = (Object[])iter.next();
                if(((Pattern)val[1]).matcher(value).matches()) {
                    return (String)val[0];
                }
            }
        }
        final Map exactPaths = (Map)resolverExactPaths.get(0);
        if(exactPaths.containsKey(kind)) {
            return (String)exactPaths.get(kind);
        }
        if(exactPaths.containsKey(null)) {
            return (String)exactPaths.get(null);
        }
        if(kind.equals(ScalarNode.class)) {
            return YAML.DEFAULT_SCALAR_TAG;
        } else if(kind.equals(SequenceNode.class)) {
            return YAML.DEFAULT_SEQUENCE_TAG;
        } else if(kind.equals(MappingNode.class)) {
            return YAML.DEFAULT_MAPPING_TAG;
        }
        return null;
    } 

    static {
        addImplicitResolver("tag:yaml.org,2002:bool",Pattern.compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$"),"yYnNtTfFoO");
        addImplicitResolver("tag:yaml.org,2002:float",Pattern.compile("^(?:[-+]?(?:[0-9][0-9_]*)\\.[0-9_]*(?:[eE][-+][0-9]+)?|[-+]?(?:[0-9][0-9_]*)?\\.[0-9_]+(?:[eE][-+][0-9]+)?|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*|[-+]?\\.(?:inf|Inf|INF)|\\.(?:nan|NaN|NAN))$"),"-+0123456789.");
        addImplicitResolver("tag:yaml.org,2002:int",Pattern.compile("^(?:[-+]?0b[0-1_]+|[-+]?0[0-7_]+|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x[0-9a-fA-F_]+|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+)$"),"-+0123456789");
        addImplicitResolver("tag:yaml.org,2002:merge",Pattern.compile("^(?:<<)$"),"<");
        addImplicitResolver("tag:yaml.org,2002:null",Pattern.compile("^(?:~|null|Null|NULL| )$"),"~nN\0");
        addImplicitResolver("tag:yaml.org,2002:null",Pattern.compile("^$"),null);
        addImplicitResolver("tag:yaml.org,2002:timestamp",Pattern.compile("^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$"),"0123456789");
        addImplicitResolver("tag:yaml.org,2002:value",Pattern.compile("^(?:=)$"),"=");
      // The following implicit resolver is only for documentation purposes. It cannot work
      // because plain scalars cannot start with '!', '&', or '*'.
        addImplicitResolver("tag:yaml.org,2002:yaml",Pattern.compile("^(?:!|&|\\*)$"),"!&*");
    }
}// ResolverImpl
