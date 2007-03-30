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
 * $Id: ComposerImpl.java,v 1.1 2006/06/06 19:19:11 olabini Exp $
 */
package org.jvyaml;

import java.io.FileReader;
import java.io.Reader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.jvyaml.events.AliasEvent;
import org.jvyaml.events.Event;
import org.jvyaml.events.NodeEvent;
import org.jvyaml.events.MappingEndEvent;
import org.jvyaml.events.MappingStartEvent;
import org.jvyaml.events.ScalarEvent;
import org.jvyaml.events.SequenceStartEvent;
import org.jvyaml.events.SequenceEndEvent;
import org.jvyaml.events.StreamStartEvent;
import org.jvyaml.events.StreamEndEvent;

import org.jvyaml.nodes.Node;
import org.jvyaml.nodes.ScalarNode;
import org.jvyaml.nodes.SequenceNode;
import org.jvyaml.nodes.MappingNode;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.1 $
 */
public class ComposerImpl implements Composer {
    private Parser parser;
    private Resolver resolver;
    private Map anchors;

    public ComposerImpl(final Parser parser, final Resolver resolver) {
        this.parser = parser;
        this.resolver = resolver;
        this.anchors = new HashMap();
    }

    public boolean checkNode() {
        return !(parser.peekEvent() instanceof StreamEndEvent);
    }
    
    public Node getNode() {
        return checkNode() ? composeDocument() : (Node)null;
    }

    private class NodeIterator implements Iterator {
        public boolean hasNext() {return checkNode();}
        public Object next() {return getNode();}
        public void remove() {}
    }

    public Iterator eachNode() {
        return new NodeIterator();
    }

    public Iterator iterator() {
        return eachNode();
    }

    public Node composeDocument() {
        if(parser.peekEvent() instanceof StreamStartEvent) {
            //Drop STREAM-START event
            parser.getEvent();
        }
        //Drop DOCUMENT-START event
        parser.getEvent();
        final Node node = composeNode(null,null);
        //Drop DOCUMENT-END event
        parser.getEvent();
        this.anchors.clear();
        return node;
    }

    private final static boolean[] FALS = new boolean[]{false};
    private final static boolean[] TRU = new boolean[]{true};

    public Node composeNode(final Node parent, final Object index) {
        if(parser.peekEvent() instanceof AliasEvent) {
            final AliasEvent event = (AliasEvent)parser.getEvent();
            final String anchor = event.getAnchor();
            if(!anchors.containsKey(anchor)) {
                System.err.println(" for aliasEvent: " + event);
                throw new ComposerException(null,"found undefined alias " + anchor,null);
            }
            return (Node)anchors.get(anchor);
        }
        final Event event = parser.peekEvent();
        String anchor = null;
        if(event instanceof NodeEvent) {
            anchor = ((NodeEvent)event).getAnchor();
        }
        if(null != anchor) {
            if(anchors.containsKey(anchor)) {
                throw new ComposerException("found duplicate anchor "+anchor+"; first occurence",null,null);
            }
        }
        resolver.descendResolver(parent,index);
        Node node = null;
        if(event instanceof ScalarEvent) {
            final ScalarEvent ev = (ScalarEvent)parser.getEvent();
            String tag = ev.getTag();
            if(tag == null || tag.equals("!")) {
                tag = resolver.resolve(ScalarNode.class,ev.getValue(),ev.getImplicit());
            }
            node = new ScalarNode(tag,ev.getValue(),ev.getStyle());
            if(null != anchor) {
                anchors.put(anchor,node);
            }
        } else if(event instanceof SequenceStartEvent) {
            final SequenceStartEvent start = (SequenceStartEvent)parser.getEvent();
            String tag = start.getTag();
            if(tag == null || tag.equals("!")) {
                tag = resolver.resolve(SequenceNode.class,null,start.getImplicit()  ? TRU : FALS);
            }
            node = new SequenceNode(tag,new ArrayList(),start.getFlowStyle());
            if(null != anchor) {
                anchors.put(anchor,node);
            }
            int ix = 0;
            while(!(parser.peekEvent() instanceof SequenceEndEvent)) {
                ((List)node.getValue()).add(composeNode(node,new Integer(ix++)));
            }
            parser.getEvent();
        } else if(event instanceof MappingStartEvent) {
            final MappingStartEvent start = (MappingStartEvent)parser.getEvent();
            String tag = start.getTag();
            if(tag == null || tag.equals("!")) {
                tag = resolver.resolve(MappingNode.class,null, start.getImplicit() ? TRU : FALS);
            }
            node = new MappingNode(tag, new HashMap(), start.getFlowStyle());
            if(null != anchor) {
                anchors.put(anchor,node);
            }
            while(!(parser.peekEvent() instanceof MappingEndEvent)) {
                final Event key = parser.peekEvent();
                final Node itemKey = composeNode(node,null);
                if(((Map)node.getValue()).containsKey(itemKey)) {
                    composeNode(node,itemKey);
                } else {
                    ((Map)node.getValue()).put(itemKey,composeNode(node,itemKey));
                }
            }
            parser.getEvent();
        }
        resolver.ascendResolver();
        return node;
    }
    
    public static void main(final String[] args) throws Exception {
        final String filename = args[0];
        System.out.println("Reading of file: \"" + filename + "\"");

        final StringBuffer input = new StringBuffer();
        final Reader reader = new FileReader(filename);
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
        final long before = System.currentTimeMillis();
        for(int i=0;i<1;i++) {
            final Composer cmp = new ComposerImpl(new ParserImpl(new ScannerImpl(str)),new ResolverImpl());
            for(final Iterator iter = cmp.eachNode();iter.hasNext();) {
                System.out.println(iter.next());
            }
        }
        final long after = System.currentTimeMillis();
        final long time = after-before;
        final double timeS = (after-before)/1000.0;
        System.out.println("Walking through the nodes for the file: " + filename + " took " + time + "ms, or " + timeS + " seconds"); 
    }
}// ComposerImpl
