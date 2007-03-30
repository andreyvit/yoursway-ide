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
 * $Id: EmitterImpl.java,v 1.5 2006/09/30 14:13:35 olabini Exp $
 */
package org.jvyaml;

import java.io.IOException;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import java.util.regex.Pattern;

import org.jvyaml.events.Event;
import org.jvyaml.events.StreamStartEvent;
import org.jvyaml.events.StreamEndEvent;
import org.jvyaml.events.DocumentStartEvent;
import org.jvyaml.events.DocumentEndEvent;
import org.jvyaml.events.CollectionStartEvent;
import org.jvyaml.events.CollectionEndEvent;
import org.jvyaml.events.MappingStartEvent;
import org.jvyaml.events.SequenceStartEvent;
import org.jvyaml.events.MappingEndEvent;
import org.jvyaml.events.SequenceEndEvent;
import org.jvyaml.events.AliasEvent;
import org.jvyaml.events.ScalarEvent;
import org.jvyaml.events.NodeEvent;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.5 $
 */
public class EmitterImpl implements Emitter {
    private static class ScalarAnalysis {
        public String scalar;
        public boolean empty;
        public boolean multiline;
        public boolean allowFlowPlain;
        public boolean allowBlockPlain;
        public boolean allowSingleQuoted;
        public boolean allowDoubleQuoted;
        public boolean allowBlock;
        public ScalarAnalysis(final String scalar, final boolean empty, final boolean multiline, final boolean allowFlowPlain, final boolean allowBlockPlain, final boolean allowSingleQuoted, final boolean allowDoubleQuoted, final boolean allowBlock) {
            this.scalar = scalar;
            this.empty = empty;
            this.multiline = multiline;
            this.allowFlowPlain = allowFlowPlain;
            this.allowBlockPlain = allowBlockPlain;
            this.allowSingleQuoted = allowSingleQuoted;
            this.allowDoubleQuoted = allowDoubleQuoted;
            this.allowBlock = allowBlock;
        }
    }

    private static interface EmitterState {
        void expect(final EmitterEnvironment env) throws IOException;
    }

    private static final int STREAM_START = 0;
    private static final int FIRST_DOCUMENT_START = 1;
    private static final int DOCUMENT_ROOT = 2;
    private static final int NOTHING = 3;
    private static final int DOCUMENT_START = 4;
    private static final int DOCUMENT_END = 5;
    private static final int FIRST_FLOW_SEQUENCE_ITEM = 6;
    private static final int FLOW_SEQUENCE_ITEM = 7;
    private static final int FIRST_FLOW_MAPPING_KEY = 8;
    private static final int FLOW_MAPPING_SIMPLE_VALUE = 9;
    private static final int FLOW_MAPPING_VALUE = 10;
    private static final int FLOW_MAPPING_KEY = 11;
    private static final int BLOCK_SEQUENCE_ITEM = 12;
    private static final int FIRST_BLOCK_MAPPING_KEY = 13;
    private static final int BLOCK_MAPPING_SIMPLE_VALUE = 14;
    private static final int BLOCK_MAPPING_VALUE = 15;
    private static final int BLOCK_MAPPING_KEY = 16;
    private static final int FIRST_BLOCK_SEQUENCE_ITEM = 17;
    
    private static final EmitterState[] STATES = new EmitterState[18];
    static {
        STATES[STREAM_START] = new EmitterState() {
                public void expect(final EmitterEnvironment env) {
                    env.expectStreamStart();
                }
            };
        STATES[FIRST_DOCUMENT_START] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectDocumentStart(true);
                }
            };
        STATES[DOCUMENT_ROOT] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectDocumentRoot();
                }
            };
        STATES[NOTHING] = new EmitterState() {
                public void expect(final EmitterEnvironment env) {
                    env.expectNothing();
                }
            };
        STATES[DOCUMENT_START] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectDocumentStart(false);
                }
            };
        STATES[DOCUMENT_END] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectDocumentEnd();
                }
            };
        STATES[FIRST_FLOW_SEQUENCE_ITEM] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectFirstFlowSequenceItem();
                }
            };
        STATES[FLOW_SEQUENCE_ITEM] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectFlowSequenceItem();
                }
            };
        STATES[FIRST_FLOW_MAPPING_KEY] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectFirstFlowMappingKey();
                }
            };
        STATES[FLOW_MAPPING_SIMPLE_VALUE] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectFlowMappingSimpleValue();
                }
            };
        STATES[FLOW_MAPPING_VALUE] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectFlowMappingValue();
                }
            };
        STATES[FLOW_MAPPING_KEY] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectFlowMappingKey();
                }
            };
        STATES[BLOCK_SEQUENCE_ITEM] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectBlockSequenceItem(false);
                }
            };
        STATES[FIRST_BLOCK_MAPPING_KEY] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectFirstBlockMappingKey();
                }
            };
        STATES[BLOCK_MAPPING_SIMPLE_VALUE] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectBlockMappingSimpleValue();
                }
            };
        STATES[BLOCK_MAPPING_VALUE] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectBlockMappingValue();
                }
            };
        STATES[BLOCK_MAPPING_KEY] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectBlockMappingKey(false);
                }
            };
        STATES[FIRST_BLOCK_SEQUENCE_ITEM] = new EmitterState() {
                public void expect(final EmitterEnvironment env) throws IOException {
                    env.expectBlockSequenceItem(true);
                }
            };
    }

    private final static Map DEFAULT_TAG_PREFIXES_1_0;
    private final static Map DEFAULT_TAG_PREFIXES_1_1;
    static {
        final Map defInit0 = new HashMap();
        defInit0.put("tag:yaml.org,2002:","!");
        DEFAULT_TAG_PREFIXES_1_0 = java.util.Collections.unmodifiableMap(defInit0);
        final Map defInit = new HashMap();
        defInit.put("!","!");
        defInit.put("tag:yaml.org,2002:","!!");
        DEFAULT_TAG_PREFIXES_1_1 = java.util.Collections.unmodifiableMap(defInit);
    }


    private Writer stream;
    private YAMLConfig options;
    private EmitterEnvironment env;

    public EmitterImpl(final Writer stream, final YAMLConfig opts) {
        this.stream = stream;
        this.options = opts;
        this.env = new EmitterEnvironment();
        this.env.emitter = this;
        this.env.canonical = this.options.canonical();
        final int propIndent = this.options.indent();
        if(propIndent>=2 && propIndent<10) {
            this.env.bestIndent = propIndent;
        }
        final int propWidth = this.options.bestWidth();
        if(propWidth != 0 && propWidth > (this.env.bestIndent*2)) {
            this.env.bestWidth = propWidth;
        }
    }

    public YAMLConfig getOptions() {
        return options;
    }

    public void emit(final Event event) throws IOException {
        this.env.events.add(event);
        while(!this.env.needMoreEvents()) {
            this.env.event = (Event)this.env.events.remove(0);
            STATES[this.env.state].expect(env);
            this.env.event = null;
        }
    }

    private static class EmitterEnvironment {
        public List states = new ArrayList();
        public int state = STREAM_START;
        public List events = new ArrayList();
        public Event event;
        public int flowLevel = 0;
        public List indents = new ArrayList();
        public int indent = -1;
        public boolean rootContext = false;
        public boolean sequenceContext = false;
        public boolean mappingContext = false;
        public boolean simpleKeyContext = false;

        public int line = 0;
        public int column = 0;
        public boolean whitespace = true;
        public boolean indentation = true;
        
        public boolean canonical = false;
        public int bestIndent = 2;
        public int bestWidth = 80;

        public String bestLinebreak = "\n";

        public Map tagPrefixes;

        public String preparedAnchor;
        public String preparedTag;
        
        public ScalarAnalysis analysis;
        public char style = 0;

        public EmitterImpl emitter;

        public String bestLineBreak = "\n";

        public boolean isVersion10 = false;

        public boolean needMoreEvents() {
            if(events.isEmpty()) {
                return true;
            }
            event = (Event)events.get(0);
            if(event instanceof DocumentStartEvent) {
                return needEvents(1);
            } else if(event instanceof SequenceStartEvent) {
                return needEvents(2);
            } else if(event instanceof MappingStartEvent) {
                return needEvents(3);
            } else {
                return false;
            }
        }

        private boolean needEvents(final int count) {
            int level = 0;
            final Iterator iter = events.iterator();
            iter.next();
            for(;iter.hasNext();) {
                final Object curr = iter.next();
                if(curr instanceof DocumentStartEvent || curr instanceof CollectionStartEvent) {
                    level++;
                } else if(curr instanceof DocumentEndEvent || curr instanceof CollectionEndEvent) {
                    level--;
                } else if(curr instanceof StreamEndEvent) {
                    level = -1;
                }
                if(level<0) {
                    return false;
                }
            }
            return events.size() < count+1;
        }

        private void increaseIndent(final boolean flow, final boolean indentless) {
            indents.add(0,new Integer(indent));
            if(indent == -1) {
                if(flow) {
                    indent = bestIndent;
                } else {
                    indent = 0;
                }
            } else if(!indentless) {
                indent += bestIndent;
            }
        }

        public void expectStreamStart() {
            if(this.event instanceof StreamStartEvent) {
                emitter.writeStreamStart();
                this.state = FIRST_DOCUMENT_START;
            } else {
                throw new EmitterException("expected StreamStartEvent, but got " + this.event);
            }
        }
        
        public void expectNothing() {
            throw new EmitterException("expecting nothing, but got " + this.event);
        }

        public void expectDocumentStart(final boolean first) throws IOException {
            if(event instanceof DocumentStartEvent) {
                final DocumentStartEvent ev = (DocumentStartEvent)event;
                if(first) {
                    if(null != ev.getVersion()) {
                        emitter.writeVersionDirective(prepareVersion(ev.getVersion()));
                    }

                    if((null != ev.getVersion() && ev.getVersion()[1] == 0) || emitter.getOptions().version().equals("1.0")) {
                        isVersion10 = true;
                        tagPrefixes = new HashMap(DEFAULT_TAG_PREFIXES_1_0);
                    } else {
                        tagPrefixes = new HashMap(DEFAULT_TAG_PREFIXES_1_1);
                    }

                    if(null != ev.getTags()) {
                        final Set handles = new TreeSet();
                        handles.addAll(ev.getTags().keySet());
                        for(final Iterator iter = handles.iterator();iter.hasNext();) {
                            final String handle = (String)iter.next();
                            final String prefix = (String)ev.getTags().get(handle);
                            tagPrefixes.put(prefix,handle);
                            final String handleText = prepareTagHandle(handle);
                            final String prefixText = prepareTagPrefix(prefix);
                            emitter.writeTagDirective(handleText,prefixText);
                        }
                    }
                }

                final boolean implicit = first && !ev.getExplicit() && !canonical && ev.getVersion() == null && ev.getTags() == null && !checkEmptyDocument();
                if(!implicit) {
                    emitter.writeIndent();
                    emitter.writeIndicator("--- ",true,true,false);
                    if(canonical) {
                        emitter.writeIndent();
                    }
                }
                state = DOCUMENT_ROOT;
            } else if(event instanceof StreamEndEvent) {
                emitter.writeStreamEnd();
                state = NOTHING;
            } else {
                throw new EmitterException("expected DocumentStartEvent, but got " + event);
            }
        }

        public void expectDocumentRoot() throws IOException {
            states.add(0,new Integer(DOCUMENT_END));
            expectNode(true,false,false,false);
        }

        public void expectDocumentEnd() throws IOException {
            if(event instanceof DocumentEndEvent) {
                emitter.writeIndent();
                if(((DocumentEndEvent)event).getExplicit()) {
                    emitter.writeIndicator("...",true,false,false);
                    emitter.writeIndent();
                }
                emitter.flushStream();
                state = DOCUMENT_START;
            } else {
                throw new EmitterException("expected DocumentEndEvent, but got " + event);
            }
        }

        public void expectFirstFlowSequenceItem() throws IOException {
            if(event instanceof SequenceEndEvent) {
                indent = ((Integer)indents.remove(0)).intValue();
                flowLevel--;
                emitter.writeIndicator("]",false,false,false);
                state = ((Integer)states.remove(0)).intValue();
            } else {
                if(canonical || column > bestWidth) {
                    emitter.writeIndent();
                }
                states.add(0,new Integer(FLOW_SEQUENCE_ITEM));
                expectNode(false,true,false,false);
            }
        }

        public void expectFlowSequenceItem() throws IOException {
            if(event instanceof SequenceEndEvent) {
                indent = ((Integer)indents.remove(0)).intValue();
                flowLevel--;
                if(canonical) {
                    emitter.writeIndicator(",",false,false,false);
                    emitter.writeIndent();
                }
                emitter.writeIndicator("]",false,false,false);
                state = ((Integer)states.remove(0)).intValue();
            } else {
                emitter.writeIndicator(",",false,false,false);
                if(canonical || column > bestWidth) {
                    emitter.writeIndent();
                }
                states.add(0,new Integer(FLOW_SEQUENCE_ITEM));
                expectNode(false,true,false,false);
            }
        }

        public void expectFirstFlowMappingKey() throws IOException {
            if(event instanceof MappingEndEvent) {
                indent = ((Integer)indents.remove(0)).intValue();
                flowLevel--;
                emitter.writeIndicator("}",false,false,false);
                state = ((Integer)states.remove(0)).intValue();
            } else {
                if(canonical || column > bestWidth) {
                    emitter.writeIndent();
                }
                if(!canonical && checkSimpleKey()) {
                    states.add(0,new Integer(FLOW_MAPPING_SIMPLE_VALUE));
                    expectNode(false,false,true,true);
                } else {
                    emitter.writeIndicator("?",true,false,false);
                    states.add(0,new Integer(FLOW_MAPPING_VALUE));
                    expectNode(false,false,true,false);
                }
            }
        }

        public void expectFlowMappingSimpleValue() throws IOException {
            emitter.writeIndicator(": ",false,true,false);
            states.add(0,new Integer(FLOW_MAPPING_KEY));
            expectNode(false,false,true,false);
        }

        public void expectFlowMappingValue() throws IOException {
            if(canonical || column > bestWidth) {
                emitter.writeIndent();
            }
            emitter.writeIndicator(": ",false,true,false);
            states.add(0,new Integer(FLOW_MAPPING_KEY));
            expectNode(false,false,true,false);
        }

        public void expectFlowMappingKey() throws IOException {
            if(event instanceof MappingEndEvent) {
                indent = ((Integer)indents.remove(0)).intValue();
                flowLevel--;
                if(canonical) {
                    emitter.writeIndicator(",",false,false,false);
                    emitter.writeIndent();
                }
                emitter.writeIndicator("}",false,false,false);
                state = ((Integer)states.remove(0)).intValue();
            } else {
                emitter.writeIndicator(",",false,false,false);
                if(canonical || column > bestWidth) {
                    emitter.writeIndent();
                }
                if(!canonical && checkSimpleKey()) {
                    states.add(0,new Integer(FLOW_MAPPING_SIMPLE_VALUE));
                    expectNode(false,false,true,true);
                } else {
                    emitter.writeIndicator("?",true,false,false);
                    states.add(0,new Integer(FLOW_MAPPING_VALUE));
                    expectNode(false,false,true,false);
                }
            }
        }

        public void expectBlockSequenceItem(final boolean first) throws IOException {
            if(!first && event instanceof SequenceEndEvent) {
                indent = ((Integer)indents.remove(0)).intValue();
                state = ((Integer)states.remove(0)).intValue();
            } else {
                emitter.writeIndent();
                emitter.writeIndicator("-",true,false,true);
                states.add(0,new Integer(BLOCK_SEQUENCE_ITEM));
                expectNode(false,true,false,false);
            }
        }

        public void expectFirstBlockMappingKey() throws IOException {
            expectBlockMappingKey(true);
        }

        public void expectBlockMappingSimpleValue() throws IOException {
            emitter.writeIndicator(": ",false,true,false);
            states.add(0,new Integer(BLOCK_MAPPING_KEY));
            expectNode(false,false,true,false);
        }

        public void expectBlockMappingValue() throws IOException {
            emitter.writeIndent();
            emitter.writeIndicator(": ",true,true,true);
            states.add(0,new Integer(BLOCK_MAPPING_KEY));
            expectNode(false,false,true,false);
        }

        public void expectBlockMappingKey(final boolean first) throws IOException {
            if(!first && event instanceof MappingEndEvent) {
                indent = ((Integer)indents.remove(0)).intValue();
                state = ((Integer)states.remove(0)).intValue();
            } else {
                emitter.writeIndent();
                if(checkSimpleKey()) {
                    states.add(0,new Integer(BLOCK_MAPPING_SIMPLE_VALUE));
                    expectNode(false,false,true,true);
                } else {
                    emitter.writeIndicator("?",true,false,true);
                    states.add(0,new Integer(BLOCK_MAPPING_VALUE));
                    expectNode(false,false,true,false);
                }
            }
        }

        private void expectNode(final boolean root, final boolean sequence, final boolean mapping, final boolean simpleKey) throws IOException {
            rootContext = root;
            sequenceContext = sequence;
            mappingContext = mapping;
            simpleKeyContext = simpleKey;
            if(event instanceof AliasEvent) {
                expectAlias();
            } else if(event instanceof ScalarEvent || event instanceof CollectionStartEvent) {
                processAnchor("&");
                processTag();
                if(event instanceof ScalarEvent) {
                    expectScalar();
                } else if(event instanceof SequenceStartEvent) {
                    if(flowLevel != 0 || canonical || ((SequenceStartEvent)event).getFlowStyle() || checkEmptySequence()) {
                        expectFlowSequence();
                    } else {
                        expectBlockSequence();
                    }
                } else if(event instanceof MappingStartEvent) {
                    if(flowLevel != 0 || canonical || ((MappingStartEvent)event).getFlowStyle() || checkEmptyMapping()) {
                        expectFlowMapping();
                    } else {
                        expectBlockMapping();
                    }
                }
            } else {
                throw new EmitterException("expected NodeEvent, but got " + event);
            }
        }
        
        private void expectAlias() throws IOException {
            if(((NodeEvent)event).getAnchor() == null) {
                throw new EmitterException("anchor is not specified for alias");
            }
            processAnchor("*");
            state = ((Integer)states.remove(0)).intValue();
        }

        private void expectScalar() throws IOException {
            increaseIndent(true,false);
            processScalar();
            indent = ((Integer)indents.remove(0)).intValue();
            state = ((Integer)states.remove(0)).intValue();
        }

        private void expectFlowSequence() throws IOException {
            emitter.writeIndicator("[",true,true,false);
            flowLevel++;
            increaseIndent(true,false);
            state = FIRST_FLOW_SEQUENCE_ITEM;
        }

        private void expectBlockSequence() throws IOException {
            increaseIndent(false, mappingContext && !indentation);
            state = FIRST_BLOCK_SEQUENCE_ITEM;
        }

        private void expectFlowMapping() throws IOException {
            emitter.writeIndicator("{",true,true,false);
            flowLevel++;
            increaseIndent(true,false);
            state = FIRST_FLOW_MAPPING_KEY;
        }

        private void expectBlockMapping() throws IOException { 
            increaseIndent(false,false);
            state = FIRST_BLOCK_MAPPING_KEY;
        }

        private boolean checkEmptySequence() {
            return event instanceof SequenceStartEvent && !events.isEmpty() && events.get(0) instanceof SequenceEndEvent;
        }

        private boolean checkEmptyMapping() {
            return event instanceof MappingStartEvent && !events.isEmpty() && events.get(0) instanceof MappingEndEvent;
        }

        private boolean checkEmptyDocument() {
            if(!(event instanceof DocumentStartEvent) || events.isEmpty()) {
                return false;
            }
            final Event ev = (Event)events.get(0);
            return ev instanceof ScalarEvent && ((ScalarEvent)ev).getAnchor() == null && ((ScalarEvent)ev).getTag() == null && ((ScalarEvent)ev).getImplicit() != null && ((ScalarEvent)ev).getValue().equals("");
        }

        private boolean checkSimpleKey() {
            int length = 0;
            if(event instanceof NodeEvent && null != ((NodeEvent)event).getAnchor()) {
                if(null == preparedAnchor) {
                    preparedAnchor = prepareAnchor(((NodeEvent)event).getAnchor());
                }
                length += preparedAnchor.length();
            }
            String tag = null;
            if(event instanceof ScalarEvent) {
                tag = ((ScalarEvent)event).getTag();
            } else if(event instanceof CollectionStartEvent) {
                tag = ((CollectionStartEvent)event).getTag();
            }
            if(tag != null) {
                if(null == preparedTag) {
                    preparedTag = emitter.prepareTag(tag);
                }
                length += preparedTag.length();
            }
            if(event instanceof ScalarEvent) {
                if(null == analysis) {
                    analysis = analyzeScalar(((ScalarEvent)event).getValue());
                    length += analysis.scalar.length();
                }
            }

            return (length < 128 && (event instanceof AliasEvent || (event instanceof ScalarEvent && !analysis.empty && !analysis.multiline) || checkEmptySequence() || checkEmptyMapping()));
        }
        
        private void processAnchor(final String indicator) throws IOException {
            final NodeEvent ev = (NodeEvent)event;
            if(null == ev.getAnchor()) {
                preparedAnchor = null;
                return;
            }
            if(null == preparedAnchor) {
                preparedAnchor = prepareAnchor(ev.getAnchor());
            }
            if(preparedAnchor != null && !"".equals(preparedAnchor)) {
                emitter.writeIndicator(indicator+preparedAnchor,true,false,false);
            }
            preparedAnchor = null;
        }
        
        private void processTag() throws IOException {
            String tag = null;
            if(event instanceof ScalarEvent) {
                final ScalarEvent ev = (ScalarEvent)event;
                tag = ev.getTag();
                if(style == 0) {
                    style = chooseScalarStyle();
                }
                if(((!canonical || tag == null) && ((0 == style && ev.getImplicit()[0]) || (0 != style && ev.getImplicit()[1])))) {
                    preparedTag = null;
                    return;
                }
                if(ev.getImplicit()[0] && null == tag) {
                    tag = "!";
                    preparedTag = null;
                }
            } else {
                final CollectionStartEvent ev = (CollectionStartEvent)event;
                tag = ev.getTag();
                if((!canonical || tag == null) && ev.getImplicit()) {
                    preparedTag = null;
                    return;
                }
            }
            if(tag == null) {
                throw new EmitterException("tag is not specified");
            }
            if(null == preparedTag) {
                preparedTag = emitter.prepareTag(tag);
            }
            if(preparedTag != null && !"".equals(preparedTag)) {
                emitter.writeIndicator(preparedTag,true,false,false);
            }
            preparedTag = null;
        }

        private char chooseScalarStyle() {
            final ScalarEvent ev = (ScalarEvent)event;

            if(null == analysis) {
                analysis = analyzeScalar(ev.getValue());
            }

            if(ev.getStyle() == '"' || this.canonical) {
                return '"';
            }
            
            //            if(ev.getStyle() == 0 && ev.getImplicit()[0]) {
            if(ev.getStyle() == 0) {
                if(!(simpleKeyContext && (analysis.empty || analysis.multiline)) && ((flowLevel != 0 && analysis.allowFlowPlain) || (flowLevel == 0 && analysis.allowBlockPlain))) {
                    return 0;
                }
            }
            if(ev.getStyle() == 0 && ev.getImplicit()[0] && (!(simpleKeyContext && (analysis.empty || analysis.multiline)) && (flowLevel!=0 && analysis.allowFlowPlain || (flowLevel == 0 && analysis.allowBlockPlain)))) {
                return 0;
            }
            if((ev.getStyle() == '|' || ev.getStyle() == '>') && flowLevel == 0 && analysis.allowBlock) {
                return '\'';
            }
            if((ev.getStyle() == 0 || ev.getStyle() == '\'') && (analysis.allowSingleQuoted && !(simpleKeyContext && analysis.multiline))) {
                return '\'';
            }
            return '"';
        }

        private void processScalar() throws IOException {
            final ScalarEvent ev = (ScalarEvent)event;

            if(null == analysis) {
                analysis = analyzeScalar(ev.getValue());
            }
            if(0 == style) {
                style = chooseScalarStyle();
            }
            final boolean split = !simpleKeyContext;
            if(style == '"') {
                emitter.writeDoubleQuoted(analysis.scalar,split);
            } else if(style == '\'') {
                emitter.writeSingleQuoted(analysis.scalar,split);
            } else if(style == '>') {
                emitter.writeFolded(analysis.scalar);
            } else if(style == '|') {
                emitter.writeLiteral(analysis.scalar);
            } else {
                emitter.writePlain(analysis.scalar,split);
            }
            analysis = null;
            style = 0;
        }
    }

    void writeStreamStart() {
    }

    void writeStreamEnd() throws IOException {
        flushStream();
    }
    
    void writeIndicator(final String indicator, final boolean needWhitespace, final boolean whitespace, final boolean indentation) throws IOException {
        String data = null;
        if(env.whitespace || !needWhitespace) {
            data = indicator;
        } else {
            data = " " + indicator;
        }
        env.whitespace = whitespace;
        env.indentation = env.indentation && indentation;
        env.column += data.length();
        stream.write(data);
    }

    void writeIndent() throws IOException {
        int indent = 0;
        if(env.indent != -1) {
            indent = env.indent;
        }

        if(!env.indentation || env.column > indent || (env.column == indent && !env.whitespace)) {
            writeLineBreak(null);
        }

        if(env.column < indent) {
            env.whitespace = true;
            final StringBuffer data = new StringBuffer();
            for(int i=0,j=(indent-env.column);i<j;i++) {
                data.append(" ");
            }
            env.column = indent;
            stream.write(data.toString());
        }
    }

    void writeVersionDirective(final String version_text) throws IOException {
        stream.write("%YAML " + version_text);
        writeLineBreak(null);
    }
    
    void writeTagDirective(final String handle, final String prefix) throws IOException {
        stream.write("%TAG " + handle + " " + prefix);
        writeLineBreak(null);
    }

    void writeDoubleQuoted(final String text, final boolean split) throws IOException {
        writeIndicator("\"",true,false,false);
        int start = 0;
        int ending = 0;
        String data = null;
        while(ending <= text.length()) {
            char ch = 0;
            if(ending < text.length()) {
                ch = text.charAt(ending);
            }
            if(ch==0 || "\"\\\u0085".indexOf(ch) != -1 || !('\u0020' <= ch && ch <= '\u007E')) {
                if(start < ending) {
                    data = text.substring(start,ending);
                    env.column+=data.length();
                    stream.write(data);
                    start = ending;
                }
                if(ch != 0) {
                    if(YAML.ESCAPE_REPLACEMENTS.containsKey(new Character(ch))) {
                        data = "\\" + YAML.ESCAPE_REPLACEMENTS.get(new Character(ch));
                    } else if(ch <= '\u00FF') {
                        String str = Integer.toString(ch,16);
                        if(str.length() == 1) {
                            str = "0" + str;
                        }
                        data = "\\x" + str;
                    }
                    env.column += data.length();
                    stream.write(data);
                    start = ending+1;
                }
            }
            if((0 < ending && ending < (text.length()-1)) && (ch == ' ' || start >= ending) && (env.column+(ending-start)) > env.bestWidth && split) {
                data = text.substring(start,ending) + "\\";
                if(start < ending) {
                    start = ending;
                }
                env.column += data.length();
                stream.write(data);
                writeIndent();
                env.whitespace = false;
                env.indentation = false;
                if(text.charAt(start) == ' ') {
                    data = "\\";
                    env.column += data.length();
                    stream.write(data);
                }
            }
            ending += 1;
        }

        writeIndicator("\"",false,false,false);
    }

    void writeSingleQuoted(final String text, final boolean split) throws IOException {
        writeIndicator("'",true,false,false);
        boolean spaces = false;
        boolean breaks = false;
        int start=0,ending=0;
        char ceh = 0;
        String data = null;
        while(ending <= text.length()) {
            ceh = 0;
            if(ending < text.length()) {
                ceh = text.charAt(ending);
            }
            if(spaces) {
                if(ceh == 0 || ceh != 32) {
                    if(start+1 == ending && env.column > env.bestWidth && split && start != 0 && ending != text.length()) {
                        writeIndent();
                    } else {
                        data = text.substring(start,ending);
                        env.column += data.length();
                        stream.write(data);
                    }
                    start = ending;
                }
            } else if(breaks) {
                if(ceh == 0 || !('\n' == ceh || '\u0085' == ceh)) {
                    data = text.substring(start,ending);
                    for(int i=0,j=data.length();i<j;i++) {
                        char cha = data.charAt(i);
                        if('\n' == cha) {
                            writeLineBreak(null);
                        } else {
                            writeLineBreak(""+cha);
                        }
                    }
                    writeIndent();
                    start = ending;
                }
            } else {
                if(ceh == 0 || !('\n' == ceh || '\u0085' == ceh)) {
                    if(start < ending) {
                        data = text.substring(start,ending);
                        env.column += data.length();
                        stream.write(data);
                        start = ending;
                    }
                    if(ceh == '\'') {
                        data = "''";
                        env.column += 2;
                        stream.write(data);
                        start = ending + 1;
                    }
                }
            }
            if(ceh != 0) {
                spaces = ceh == ' ';
                breaks = ceh == '\n' || ceh == '\u0085';
            }
            ending++;
        }
        writeIndicator("'",false,false,false);
    }

    void writeFolded(final String text) throws IOException {
        String chomp = determineChomp(text);
        writeIndicator(">" + chomp, true, false, false);
        writeIndent();
        boolean leadingSpace = false;
        boolean spaces = false;
        boolean breaks = false;
        int start=0,ending=0;
        String data = null;
        while(ending <= text.length()) {
            char ceh = 0;
            if(ending < text.length()) {
                ceh = text.charAt(ending);
            }
            if(breaks) {
                if(ceh == 0 || !('\n' == ceh || '\u0085' == ceh)) {
                    if(!leadingSpace && ceh != 0 && ceh != ' ' && text.charAt(start) == '\n') {
                        writeLineBreak(null);
                    }
                    leadingSpace = ceh == ' ';
                    data = text.substring(start,ending);
                    for(int i=0,j=data.length();i<j;i++) {
                        char cha = data.charAt(i);
                        if('\n' == cha) {
                            writeLineBreak(null);
                        } else {
                            writeLineBreak(""+cha);
                        }
                    }
                    if(ceh != 0) {
                        writeIndent();
                    }
                    start = ending;
                }
            } else if(spaces) {
                if(ceh != ' ') {
                    if(start+1 == ending && env.column > env.bestWidth) {
                        writeIndent();
                    } else {
                        data = text.substring(start,ending);
                        env.column += data.length();
                        stream.write(data);
                    }
                    start = ending;
                }
            } else {
                if(ceh == 0 || ' ' == ceh || '\n' == ceh || '\u0085' == ceh) {
                    data = text.substring(start,ending);
                    stream.write(data);
                    if(ceh == 0) {
                        writeLineBreak(null);
                    } 
                    start = ending;
                }
            }
            if(ceh != 0) {
                breaks = '\n' == ceh || '\u0085' == ceh;
                spaces = ceh == ' ';
            }
            ending++;
        }
    }

    void writeLiteral(final String text) throws IOException {
        String chomp = determineChomp(text);
        writeIndicator("|" + chomp, true, false, false);
        writeIndent();
        boolean breaks = false;
        int start=0,ending=0;
        String data = null;
        while(ending <= text.length()) {
            char ceh = 0;
            if(ending < text.length()) {
                ceh = text.charAt(ending);
            }
            if(breaks) {
                if(ceh == 0 || !('\n' == ceh || '\u0085' == ceh)) {
                    data = text.substring(start,ending);
                    for(int i=0,j=data.length();i<j;i++) {
                        char cha = data.charAt(i);
                        if('\n' == cha) {
                            writeLineBreak(null);
                        } else {
                            writeLineBreak(""+cha);
                        }
                    }
                    if(ceh != 0) {
                        writeIndent();
                    }
                    start = ending;
                }
            } else {
                if(ceh == 0 || '\n' == ceh || '\u0085' == ceh) {
                    data = text.substring(start,ending);
                    stream.write(data);
                    if(ceh == 0) {
                        writeLineBreak(null);
                    }
                    start = ending;
                }
            }
            if(ceh != 0) {
                breaks = '\n' == ceh || '\u0085' == ceh;
            }
            ending++;
        }
    }

    void writePlain(final String text, final boolean split) throws IOException {
        if(text == null || "".equals(text)) {
            return;
        }
        String data = null;
        if(!env.whitespace) {
            data = " ";
            env.column += data.length();
            stream.write(data);
        }
        env.whitespace = false;
        env.indentation = false;
        boolean spaces=false, breaks = false;
        int start=0,ending=0;
        while(ending <= text.length()) {
            char ceh = 0;
            if(ending < text.length()) {
                ceh = text.charAt(ending);
            }
            if(spaces) {
                if(ceh != ' ') {
                    if(start+1 == ending && env.column > env.bestWidth && split) {
                        writeIndent();
                        env.whitespace = false;
                        env.indentation = false;
                    } else {
                        data = text.substring(start,ending);
                        env.column += data.length();
                        stream.write(data);
                    }
                    start = ending;
                }
            } else if(breaks) {
                if(ceh != '\n' && ceh != '\u0085') {
                    if(text.charAt(start) == '\n') {
                        writeLineBreak(null);
                    }
                    data = text.substring(start,ending);
                    for(int i=0,j=data.length();i<j;i++) {
                        char cha = data.charAt(i);
                        if('\n' == cha) {
                            writeLineBreak(null);
                        } else {
                            writeLineBreak(""+cha);
                        }
                    }
                    writeIndent();
                    env.whitespace = false;
                    env.indentation = false;
                    start = ending;
                }
            } else {
                if(ceh == 0 || ' ' == ceh || '\n' == ceh || '\u0085' == ceh) {
                    data = text.substring(start,ending);
                    env.column += data.length();
                    stream.write(data);
                    start = ending;
                }
            }
            if(ceh != 0) {
                spaces = ceh == ' ';
                breaks = ceh == '\n' || ceh == '\u0085';
            }
            ending++;
        }
    }

    void writeLineBreak(final String data) throws IOException {
        String xdata = data;
        if(xdata == null) {
            xdata = env.bestLineBreak;
        }
        env.whitespace = true;
        env.indentation = true;
        env.line++;
        env.column = 0;
        stream.write(xdata);
    }

    void flushStream() throws IOException {
        stream.flush();
    }

    static String prepareVersion(final int[] version) {
        if(version[0] != 1) {
            throw new EmitterException("unsupported YAML version: " + version[0] + "." + version[1]);
        }
        return ""+version[0] + "." + version[1];
    }
    private final static Pattern HANDLE_FORMAT = Pattern.compile("^![-\\w]*!$");
    static String prepareTagHandle(final String handle) {
        if(handle == null || "".equals(handle)) {
            throw new EmitterException("tag handle must not be empty");
        } else if(handle.charAt(0) != '!' || handle.charAt(handle.length()-1) != '!') {
            throw new EmitterException("tag handle must start and end with '!': " + handle);
        } else if(!"!".equals(handle) && !HANDLE_FORMAT.matcher(handle).matches()) {
            throw new EmitterException("invalid syntax for tag handle: " + handle);
        }
        return handle;
    }

    static String prepareTagPrefix(final String prefix) {
        if(prefix == null || "".equals(prefix)) {
            throw new EmitterException("tag prefix must not be empty");
        }
        final StringBuffer chunks = new StringBuffer();
        int start=0,ending=0;
        if(prefix.charAt(0) == '!') {
            ending = 1;
        }
        while(ending < prefix.length()) {
            ending++;
        }
        if(start < ending) {
            chunks.append(prefix.substring(start,ending));
        }
        return chunks.toString();
    }

    private final static Pattern ANCHOR_FORMAT = Pattern.compile("^[-\\w]*$");
    static String prepareAnchor(final String anchor) {
        if(anchor == null || "".equals(anchor)) {
            throw new EmitterException("anchor must not be empty");
        }
        if(!ANCHOR_FORMAT.matcher(anchor).matches()) {
            throw new EmitterException("invalid syntax for anchor: " + anchor);
        }
        return anchor;
    }

    String prepareTag(final String tag) {
        if(tag == null || "".equals(tag)) {
            throw new EmitterException("tag must not be empty");
        }
        if(tag.equals("!")) {
            return tag;
        }
        String handle = null;
        String suffix = tag;
        for(final Iterator iter = env.tagPrefixes.keySet().iterator();iter.hasNext();) {
            String prefix = (String)iter.next();
            if(Pattern.matches("^" + prefix + ".+$", tag) && (prefix.equals("!") || prefix.length() < tag.length())) {
                handle = (String)env.tagPrefixes.get(prefix);
                suffix = tag.substring(prefix.length());
            }
        }
        final StringBuffer chunks = new StringBuffer();
        int start=0,ending=0;
        while(ending < suffix.length()) {
            ending++;
        }
        if(start < ending) {
            chunks.append(suffix.substring(start,ending));
        }
        String suffixText = chunks.toString();
        if(tag.charAt(0) == '!' && env.isVersion10) {
            return tag;
        }
        if(handle != null) {
            return handle + suffixText;
        } else {
            return "!<" + suffixText + ">";
        }
    }

    private final static Pattern DOC_INDIC = Pattern.compile("^(---|\\.\\.\\.)");
    private final static String NULL_BL_T_LINEBR = "\0 \t\r\n\u0085";
    private final static String SPECIAL_INDIC = "#,[]{}#&*!|>'\"%@`";
    private final static String FLOW_INDIC = ",?[]{}";
    static ScalarAnalysis analyzeScalar(final String scalar) {
        if(scalar == null || "".equals(scalar)) {
            return new ScalarAnalysis(scalar,true,false,false,true,true,true,false);
        }
        boolean blockIndicators = false;
        boolean flowIndicators = false;
        boolean lineBreaks = false;
        boolean specialCharacters = false;

        // Whitespaces.
        boolean inlineSpaces = false;          // non-space space+ non-space
        boolean inlineBreaks = false;          // non-space break+ non-space
        boolean leadingSpaces = false;         // ^ space+ (non-space | $)
        boolean leadingBreaks = false;         // ^ break+ (non-space | $)
        boolean trailingSpaces = false;        // (^ | non-space) space+ $
        boolean trailingBreaks = false;        // (^ | non-space) break+ $
        boolean inlineBreaksSpaces = false;   // non-space break+ space+ non-space
        boolean mixedBreaksSpaces = false;    // anything else
        
        if(DOC_INDIC.matcher(scalar).matches()) {
            blockIndicators = true;
            flowIndicators = true;
        }

        boolean preceededBySpace = true;
        boolean followedBySpace = scalar.length() == 1 || NULL_BL_T_LINEBR.indexOf(scalar.charAt(1)) != -1;

        boolean spaces = false;
        boolean breaks = false;
        boolean mixed = false;
        boolean leading = false;
        
        int index = 0;

        while(index < scalar.length()) {
            char ceh = scalar.charAt(index);
            if(index == 0) {
                if(SPECIAL_INDIC.indexOf(ceh) != -1) {
                    flowIndicators = true;
                    blockIndicators = true;
                }
                if(ceh == '?' || ceh == ':') {
                    flowIndicators = true;
                    if(followedBySpace) {
                        blockIndicators = true;
                    }
                }
                if(ceh == '-' && followedBySpace) {
                    flowIndicators = true;
                    blockIndicators = true;
                }
            } else {
                if(FLOW_INDIC.indexOf(ceh) != -1) {
                    flowIndicators = true;
                }
                if(ceh == ':') {
                    flowIndicators = true;
                    if(followedBySpace) {
                        blockIndicators = true;
                    }
                }
                if(ceh == '#' && preceededBySpace) {
                    flowIndicators = true;
                    blockIndicators = true;
                }
            }
            if(ceh == '\n' || '\u0085' == ceh) {
                lineBreaks = true;
            }
            if(!(ceh == '\n' || ('\u0020' <= ceh && ceh <= '\u007E'))) {
                specialCharacters = true;

            }
            if(' ' == ceh || '\n' == ceh || '\u0085' == ceh) {
                if(spaces && breaks) {
                    if(ceh != ' ') {
                        mixed = true;
                    }
                } else if(spaces) {
                    if(ceh != ' ') {
                        breaks = true;
                        mixed = true;
                    }
                } else if(breaks) {
                    if(ceh == ' ') {
                        spaces = true;
                    }
                } else {
                    leading = (index == 0);
                    if(ceh == ' ') {
                        spaces = true;
                    } else {
                        breaks = true;
                    }
                }
            } else if(spaces || breaks) {
                if(leading) {
                    if(spaces && breaks) {
                        mixedBreaksSpaces = true;
                    } else if(spaces) {
                        leadingSpaces = true;
                    } else if(breaks) {
                        leadingBreaks = true;
                    }
                } else {
                    if(mixed) {
                        mixedBreaksSpaces = true;
                    } else if(spaces && breaks) {
                        inlineBreaksSpaces = true;
                    } else if(spaces) {
                        inlineSpaces = true;
                    } else if(breaks) {
                        inlineBreaks = true;
                    }
                }
                spaces = breaks = mixed = leading = false;
            }

            if((spaces || breaks) && (index == scalar.length()-1)) {
                if(spaces && breaks) {
                    mixedBreaksSpaces = true;
                } else if(spaces) {
                    trailingSpaces = true;
                    if(leading) {
                        leadingSpaces = true;
                    }
                } else if(breaks) {
                    trailingBreaks = true;
                    if(leading) {
                        leadingBreaks = true;
                    }
                }
                spaces = breaks = mixed = leading = false;
            }
            index++;
            preceededBySpace = NULL_BL_T_LINEBR.indexOf(ceh) != -1;
            followedBySpace = index+1 >= scalar.length() || NULL_BL_T_LINEBR.indexOf(scalar.charAt(index+1)) != -1;
        }
        boolean allowFlowPlain = true;
        boolean allowBlockPlain = true;
        boolean allowSingleQuoted = true;
        boolean allowDoubleQuoted = true;
        boolean allowBlock = true;
        
        if(leadingSpaces || leadingBreaks || trailingSpaces) {
            allowFlowPlain = allowBlockPlain = allowBlock = false;
        }

        if(trailingBreaks) {
            allowFlowPlain = allowBlockPlain = false;
        }

        if(inlineBreaksSpaces) {
            allowFlowPlain = allowBlockPlain = allowSingleQuoted = false;
        }

        if(mixedBreaksSpaces || specialCharacters) {
            allowFlowPlain = allowBlockPlain = allowSingleQuoted = allowBlock = false;
        }

        if(inlineBreaks) {
            allowFlowPlain = allowBlockPlain = allowSingleQuoted = false;
        }
        
        if(trailingBreaks) {
            allowSingleQuoted = false;
        }

        if(lineBreaks) {
            allowFlowPlain = allowBlockPlain = false;
        }

        if(flowIndicators) {
            allowFlowPlain = false;
        }
        
        if(blockIndicators) {
            allowBlockPlain = false;
        }

        return new ScalarAnalysis(scalar,false,lineBreaks,allowFlowPlain,allowBlockPlain,allowSingleQuoted,allowDoubleQuoted,allowBlock);
    }

    static String determineChomp(final String text) {
        String tail = text.substring(text.length()-2,text.length()-1);
        while(tail.length() < 2) {
            tail = " " + tail;
        }
        char ceh = tail.charAt(tail.length()-1);
        char ceh2 = tail.charAt(tail.length()-2);
        return (ceh == '\n' || ceh == '\u0085') ? ((ceh2 == '\n' || ceh2 == '\u0085') ? "+" : "") : "-";
    }

    public static void main(final String[] args) throws IOException {
        final String filename = args[0]; // filename to test against
        System.out.println("File contents:");
        final BufferedReader read = new BufferedReader(new FileReader(filename));
        String last = null;
        while((last = read.readLine()) != null) {
            System.out.println(last);
        }
        read.close();
        System.out.println("--------------------------------");
        final Emitter emitter = new EmitterImpl(new java.io.OutputStreamWriter(System.out),YAML.config());
        final Parser pars = new ParserImpl(new ScannerImpl(new FileReader(filename)));
        for(final Iterator iter = pars.eachEvent();iter.hasNext();) {
            emitter.emit((Event)iter.next());
        }
    }
}// EmitterImpl
