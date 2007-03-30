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
 * $Id: ScannerImpl.java,v 1.5 2006/09/23 21:43:30 olabini Exp $
 */
package org.jvyaml;

import java.io.FileReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;

import java.nio.CharBuffer;
import java.nio.channels.Channels;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jvyaml.tokens.*;

/**
 * <p>A Java implementation of the RbYAML scanner.</p>
 *
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.5 $
 */
public class ScannerImpl implements Scanner {
    private final static String LINEBR = "\n\u0085\u2028\u2029";
    private final static String NULL_BL_LINEBR = "\0 \r\n\u0085";
    private final static String NULL_BL_T_LINEBR = "\0 \t\r\n\u0085";
    private final static String NULL_OR_OTHER = NULL_BL_T_LINEBR;
    private final static String NULL_OR_LINEBR = "\0\r\n\u0085";
    private final static String FULL_LINEBR = "\r\n\u0085";
    private final static String BLANK_OR_LINEBR = " \r\n\u0085";
    private final static String S4 = "\0 \t\r\n\u0028[]{}";    
    private final static String ALPHA = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
    private final static String STRANGE_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][-';/?:@&=+$,.!~*()%";
    private final static String RN = "\r\n";
    private final static String BLANK_T = " \t";
    private final static String SPACES_AND_STUFF = "'\"\\\0 \t\r\n\u0085";
    private final static String DOUBLE_ESC = "\"\\";
    private final static String NON_ALPHA_OR_NUM = "\0 \t\r\n\u0085?:,]}%@`";
    private final static Pattern NON_PRINTABLE = Pattern.compile("[^\u0009\n\r\u0020-\u007E\u0085\u00A0-\u00FF]");
    private final static Pattern NOT_HEXA = Pattern.compile("[^0-9A-Fa-f]");
    private final static Pattern NON_ALPHA = Pattern.compile("[^-0-9A-Za-z_]");
    private final static Pattern R_FLOWZERO = Pattern.compile("[\0 \t\r\n\u0085]|(:[\0 \t\r\n\u0028])");
    private final static Pattern R_FLOWNONZERO = Pattern.compile("[\0 \t\r\n\u0085\\[\\]{},:?]");
    private final static Pattern LINE_BR_REG = Pattern.compile("[\n\u0085]|(?:\r[^\n])");
    private final static Pattern END_OR_START = Pattern.compile("^(---|\\.\\.\\.)[\0 \t\r\n\u0085]$");
    private final static Pattern ENDING = Pattern.compile("^---[\0 \t\r\n\u0085]$");
    private final static Pattern START = Pattern.compile("^\\.\\.\\.[\0 \t\r\n\u0085]$");
    private final static Pattern BEG = Pattern.compile("^([^\0 \t\r\n\u0085\\-?:,\\[\\]{}#&*!|>'\"%@]|([\\-?:][^\0 \t\r\n\u0085]))");

    private final static Map ESCAPE_REPLACEMENTS = new HashMap();
    private final static Map ESCAPE_CODES = new HashMap();

    static {
        ESCAPE_REPLACEMENTS.put(new Character('0'),"\0");
        ESCAPE_REPLACEMENTS.put(new Character('a'),"\u0007");
        ESCAPE_REPLACEMENTS.put(new Character('b'),"\u0008");
        ESCAPE_REPLACEMENTS.put(new Character('t'),"\u0009");
        ESCAPE_REPLACEMENTS.put(new Character('\t'),"\u0009");
        ESCAPE_REPLACEMENTS.put(new Character('n'),"\n");
        ESCAPE_REPLACEMENTS.put(new Character('v'),"\u000B");
        ESCAPE_REPLACEMENTS.put(new Character('f'),"\u000C");
        ESCAPE_REPLACEMENTS.put(new Character('r'),"\r");
        ESCAPE_REPLACEMENTS.put(new Character('e'),"\u001B");
        ESCAPE_REPLACEMENTS.put(new Character(' '),"\u0020");
        ESCAPE_REPLACEMENTS.put(new Character('"'),"\"");
        ESCAPE_REPLACEMENTS.put(new Character('\\'),"\\");
        ESCAPE_REPLACEMENTS.put(new Character('N'),"\u0085");
        ESCAPE_REPLACEMENTS.put(new Character('_'),"\u00A0");
        ESCAPE_REPLACEMENTS.put(new Character('L'),"\u2028");
        ESCAPE_REPLACEMENTS.put(new Character('P'),"\u2029");

        ESCAPE_CODES.put(new Character('x'),new Integer(2));
        ESCAPE_CODES.put(new Character('u'),new Integer(4));
        ESCAPE_CODES.put(new Character('U'),new Integer(8));
    }

    private boolean done = false;
    private int flowLevel = 0;
    private int tokensTaken = 0;
    private int indent = -1;
    private boolean allowSimpleKey = true;
    private boolean eof = true;
    private int column = 0;
    private int pointer = 0;
    private StringBuffer buffer;
    private Reader stream;
    private List tokens;
    private List indents;
    private Map possibleSimpleKeys;

    private boolean docStart = false;

    public ScannerImpl(final Reader stream) {
        this.stream = stream;
        this.eof = false;
        this.buffer = new StringBuffer();
        this.tokens = new LinkedList();
        this.indents = new LinkedList();
        this.possibleSimpleKeys = new HashMap();
        fetchStreamStart();
    }

    public ScannerImpl(final String stream) {
        this.buffer = new StringBuffer(stream);
        this.stream = null;
        this.tokens = new LinkedList();
        this.indents = new LinkedList();
        this.possibleSimpleKeys = new HashMap();
        fetchStreamStart();
    }

    public boolean checkToken(final Class[] choices) {
        while(needMoreTokens()) {
            fetchMoreTokens();
        }
        if(!this.tokens.isEmpty()) {
            if(choices.length == 0) {
                return true;
            }
            final Object first = this.tokens.get(0);
            for(int i=0,j=choices.length;i<j;i++) {
                if(choices[i].isInstance(first)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Token peekToken() {
        while(needMoreTokens()) {
            fetchMoreTokens();
        }
        return (Token)(this.tokens.isEmpty() ? null : this.tokens.get(0));
    }

    public Token getToken() {
        while(needMoreTokens()) {
            fetchMoreTokens();
        }
        if(!this.tokens.isEmpty()) {
            this.tokensTaken++;
            return (Token)this.tokens.remove(0);
        }
        return null;
    }
    
    private class TokenIterator implements Iterator {
        public boolean hasNext() {
            return null != peekToken();
        }

        public Object next() {
            return getToken();
        }

        public void remove() {
        }
    }

    public Iterator eachToken() {
        return new TokenIterator();
    }

    public Iterator iterator() {
        return eachToken();
    }

    private char peek() {
        if(this.pointer + 1 > this.buffer.length()) {
            update(1);
        }
        return this.buffer.charAt(this.pointer);
    }

    private char peek(final int index) {
        if(this.pointer + index + 1 > this.buffer.length()) {
            update(index+1);
        }
        return this.buffer.charAt(this.pointer + index);
    }

    private String prefix() {
        return prefix(1);
    }

    private String prefix(final int length) {
        if(this.pointer + length >= this.buffer.length()) {
            update(length);
        }
        if(this.pointer+length > this.buffer.length()) {
            return this.buffer.substring(this.pointer,this.buffer.length());
        } else {
            return this.buffer.substring(this.pointer,this.pointer+length);
        }
    }

    private String prefixForward(final int length) {
        if(this.pointer + length + 1 >= this.buffer.length()) {
            update(length+1);
        }
        String buff = null;
        if(this.pointer+length > this.buffer.length()) {
            buff = this.buffer.substring(this.pointer,this.buffer.length());
        } else {
            buff = this.buffer.substring(this.pointer,this.pointer+length);
        }
        char ch = 0;
        for(int i=0,j=buff.length();i<j;i++) {
            ch = buff.charAt(i);
            this.pointer++;
            if(LINEBR.indexOf(ch) != -1 || (ch == '\r' && buff.charAt(i+1) != '\n')) {
                this.column = 0;
            } else if(ch != '\uFEFF') {
                this.column++;
            }
        }
        return buff;
    }

    private void forward() {
        if(this.pointer + 2 >= this.buffer.length()) {
            update(2);
        }
        final char ch1 = this.buffer.charAt(this.pointer);
        this.pointer++;
        if(ch1 == '\n' || ch1 == '\u0085' || (ch1 == '\r' && this.buffer.charAt(this.pointer) != '\n')) {
            this.column = 0;
        } else {
            this.column++;
        }
    }
    
    private void forward(final int length) {
        if(this.pointer + length + 1 >= this.buffer.length()) {
            update(length+1);
        }
        char ch = 0;
        for(int i=0;i<length;i++) {
            ch = this.buffer.charAt(this.pointer);
            this.pointer++;
            if(LINEBR.indexOf(ch) != -1 || (ch == '\r' && this.buffer.charAt(this.pointer) != '\n')) {
                this.column = 0;
            } else if(ch != '\uFEFF') {
                this.column++;
            }
        }
    }

    private void checkPrintable(final CharSequence data) {
        final Matcher em = NON_PRINTABLE.matcher(data);
        if(em.find()) {
            final int position = this.buffer.length() - this.pointer + em.start();
            throw new YAMLException("At " + position + " we found: " + em.group() + ". Special characters are not allowed");
        }
    }

    private void update(final int length) {
        this.buffer.delete(0,this.pointer);
        this.pointer = 0;
        while(this.buffer.length() < length) {
            String rawData = "";
            if(!this.eof) {
                char[] data = new char[1024];
                int converted = -2;
                try {
                    converted = this.stream.read(data);
                } catch(final IOException ioe) {
                    throw new YAMLException(ioe);
                }
                if(converted == -1) {
                    this.eof = true;
                } else {
                    rawData = String.valueOf(data,0,converted);
                }
            }
            checkPrintable(rawData);
            this.buffer.append(rawData);
            if(this.eof) {
                this.buffer.append('\0');
                break;
            }
        }
    }

    private boolean needMoreTokens() {
        if(this.done) {
            return false;
        }
        return this.tokens.isEmpty() || nextPossibleSimpleKey() == this.tokensTaken;
    }

    private Token fetchMoreTokens() {
        scanToNextToken();
        unwindIndent(this.column);
        final char ch =  peek();
        final boolean colz = this.column == 0;
        switch(ch) {
        case '\0': return fetchStreamEnd();
        case '\'': return fetchSingle();
        case '"': return fetchDouble();
        case '?': if(this.flowLevel != 0 || NULL_OR_OTHER.indexOf(peek(1)) != -1) { return fetchKey(); } break;
        case ':': if(this.flowLevel != 0 || NULL_OR_OTHER.indexOf(peek(1)) != -1) { return fetchValue(); } break;
        case '%': if(colz) {return fetchDirective(); } break;
        case '-': 
            if((colz || docStart) && ENDING.matcher(prefix(4)).matches()) {
                return fetchDocumentStart(); 
            } else if(NULL_OR_OTHER.indexOf(peek(1)) != -1) {
                return fetchBlockEntry(); 
            }
            break;
        case '.': 
            if(colz && START.matcher(prefix(4)).matches()) {
                return fetchDocumentEnd(); 
            }
            break;
        case '[': return fetchFlowSequenceStart();
        case '{': return fetchFlowMappingStart();
        case ']': return fetchFlowSequenceEnd();
        case '}': return fetchFlowMappingEnd();
        case ',': return fetchFlowEntry();
        case '*': return fetchAlias();
        case '&': return fetchAnchor();
        case '!': return fetchTag();
        case '|': if(this.flowLevel == 0) { return fetchLiteral(); } break;
        case '>': if(this.flowLevel == 0) { return fetchFolded(); } break;
        }
        if(BEG.matcher(prefix(2)).find()) {
            return fetchPlain();
        }
        throw new ScannerException("while scanning for the next token","found character " + ch + "(" + ((int)ch) + " that cannot start any token",null);
    }

    private int nextPossibleSimpleKey() {
        for(final Iterator iter = this.possibleSimpleKeys.values().iterator();iter.hasNext();) {
            final SimpleKey key = (SimpleKey)iter.next();
            if(key.getTokenNumber() > 0) {
                return key.getTokenNumber();
            }
        }
        return -1;
    }
    
    private void savePossibleSimpleKey() {
        if(this.allowSimpleKey) {
            this.possibleSimpleKeys.put(new Integer(this.flowLevel),new SimpleKey(this.tokensTaken+this.tokens.size(),(this.flowLevel == 0) && this.indent == this.column,-1,-1,this.column));
        }
    }
    
    private void unwindIndent(final int col) {
        if(this.flowLevel != 0) {
            return;
        }

        while(this.indent > col) {
            this.indent = ((Integer)(this.indents.remove(0))).intValue();
            this.tokens.add(Token.BLOCK_END);
        }
    }
    
    private boolean addIndent(final int col) {
        if(this.indent < col) {
            this.indents.add(0,new Integer(this.indent));
            this.indent = col;
            return true;
        }
        return false;
    }

    private Token fetchStreamStart() {
        this.docStart = true;
        this.tokens.add(Token.STREAM_START);
        return Token.STREAM_START;
    }

    private Token fetchStreamEnd() {
        unwindIndent(-1);
        this.allowSimpleKey = false;
        this.possibleSimpleKeys = new HashMap();
        this.tokens.add(Token.STREAM_END);
        this.done = true;
        return Token.STREAM_END;
    }

    private Token fetchDirective() {
        unwindIndent(-1);
        this.allowSimpleKey = false;
        final Token tok = scanDirective();
        this.tokens.add(tok);
        return tok;
    }
    
    private Token fetchDocumentStart() {
        this.docStart = false;
        return fetchDocumentIndicator(Token.DOCUMENT_START);
    }

    private Token fetchDocumentEnd() {
        return fetchDocumentIndicator(Token.DOCUMENT_END);
    }

    private Token fetchDocumentIndicator(final Token tok) {
        unwindIndent(-1);
        this.allowSimpleKey = false;
        forward(3);
        this.tokens.add(tok);
        return tok;
    }
    
    private Token fetchFlowSequenceStart() {
        return fetchFlowCollectionStart(Token.FLOW_SEQUENCE_START);
    }

    private Token fetchFlowMappingStart() {
        return fetchFlowCollectionStart(Token.FLOW_MAPPING_START);
    }

    private Token fetchFlowCollectionStart(final Token tok) {
        savePossibleSimpleKey();
        this.flowLevel++;
        this.allowSimpleKey = true;
        forward(1);
        this.tokens.add(tok);
        return tok;
    }

    private Token fetchFlowSequenceEnd() {
        return fetchFlowCollectionEnd(Token.FLOW_SEQUENCE_END);
    }
    
    private Token fetchFlowMappingEnd() {
        return fetchFlowCollectionEnd(Token.FLOW_MAPPING_END);
    }
    
    private Token fetchFlowCollectionEnd(final Token tok) {
        this.flowLevel--;
        this.allowSimpleKey = false;
        forward(1);
        this.tokens.add(tok);
        return tok;
    }
    
    private Token fetchFlowEntry() {
        this.allowSimpleKey = true;
        forward(1);
        this.tokens.add(Token.FLOW_ENTRY);
        return Token.FLOW_ENTRY;
    }

    private Token fetchBlockEntry() {
        if(this.flowLevel == 0) {
            if(!this.allowSimpleKey) {
                throw new ScannerException(null,"sequence entries are not allowed here",null);
            }
            if(addIndent(this.column)) {
                this.tokens.add(Token.BLOCK_SEQUENCE_START);
            }
        }
        this.allowSimpleKey = true;
        forward();
        this.tokens.add(Token.BLOCK_ENTRY);
        return Token.BLOCK_ENTRY;
    }        

    private Token fetchKey() {
        if(this.flowLevel == 0) {
            if(!this.allowSimpleKey) {
                throw new ScannerException(null,"mapping keys are not allowed here",null);
            }
            if(addIndent(this.column)) {
                this.tokens.add(Token.BLOCK_MAPPING_START);
            }
        }
        this.allowSimpleKey = this.flowLevel == 0;
        forward();
        this.tokens.add(Token.KEY);
        return Token.KEY;
    }

    private Token fetchValue() {
        final SimpleKey key = (SimpleKey)this.possibleSimpleKeys.get(new Integer(this.flowLevel));
        if(null == key) {
            if(this.flowLevel == 0 && !this.allowSimpleKey) {
                throw new ScannerException(null,"mapping values are not allowed here",null);
            }
        } else {
            this.possibleSimpleKeys.remove(new Integer(this.flowLevel));
            this.tokens.add(key.getTokenNumber()-this.tokensTaken,Token.KEY);
            if(this.flowLevel == 0 && addIndent(key.getColumn())) {
                this.tokens.add(key.getTokenNumber()-this.tokensTaken,Token.BLOCK_MAPPING_START);
            }
            this.allowSimpleKey = false;
        }
        forward();
        this.tokens.add(Token.VALUE);
        return Token.VALUE;
    }

    private Token fetchAlias() {
        savePossibleSimpleKey();
        this.allowSimpleKey = false;
        final Token tok = scanAnchor(new AliasToken());
        this.tokens.add(tok);
        return tok;
    }

    private Token fetchAnchor() {
        savePossibleSimpleKey();
        this.allowSimpleKey = false;
        final Token tok = scanAnchor(new AnchorToken());
        this.tokens.add(tok);
        return tok;
    }

    private Token fetchTag() {
        savePossibleSimpleKey();
        this.allowSimpleKey = false;
        final Token tok = scanTag();
        this.tokens.add(tok);
        return tok;
    }
    
    private Token fetchLiteral() {
        return fetchBlockScalar('|');
    }
    
    private Token fetchFolded() {
        return fetchBlockScalar('>');
    }
    
    private Token fetchBlockScalar(final char style) {
        this.allowSimpleKey = true;
        final Token tok = scanBlockScalar(style);
        this.tokens.add(tok);
        return tok;
    }
    
    private Token fetchSingle() {
        return fetchFlowScalar('\'');
    }
    
    private Token fetchDouble() {
        return fetchFlowScalar('"');
    }
    
    private Token fetchFlowScalar(final char style) {
        savePossibleSimpleKey();
        this.allowSimpleKey = false;
        final Token tok = scanFlowScalar(style);
        this.tokens.add(tok);
        return tok;
    }
    
    private Token fetchPlain() {
        savePossibleSimpleKey();
        this.allowSimpleKey = false;
        final Token tok = scanPlain();
        this.tokens.add(tok);
        return tok;
    }
    
    private void scanToNextToken() {
        for(;;) {
            while(peek() == ' ') {
                forward();
            }
            if(peek() == '#') {
                while(NULL_OR_LINEBR.indexOf(peek()) == -1) {
                    forward();
                }
            }
            if(scanLineBreak().length() != 0 ) {
                if(this.flowLevel == 0) {
                    this.allowSimpleKey = true;
                }
            } else {
                break;
            }
        }
    }
    
    private Token scanDirective() {
        forward();
        final String name = scanDirectiveName();
        String[] value = null;
        if(name.equals("YAML")) {
            value = scanYamlDirectiveValue();
        } else if(name.equals("TAG")) {
            value = scanTagDirectiveValue();
        } else {
            while(NULL_OR_LINEBR.indexOf(peek()) == -1) {
                forward();
            }
        }
        scanDirectiveIgnoredLine();
        return new DirectiveToken(name,value);
    }
    
    private String scanDirectiveName() {
        int length = 0;
        char ch = peek(length);
        boolean zlen = true;
        while(ALPHA.indexOf(ch) != -1) {
            zlen = false;
            length++;
            ch = peek(length);
        }
        if(zlen) {
            throw new ScannerException("while scanning a directive","expected alphabetic or numeric character, but found " + ch + "(" + ((int)ch) + ")",null);
        }
        final String value = prefixForward(length);
        //        forward(length);
        if(NULL_BL_LINEBR.indexOf(peek()) == -1) {
            throw new ScannerException("while scanning a directive","expected alphabetic or numeric character, but found " + ch + "(" + ((int)ch) + ")",null);
        }
        return value;
    }

    private String[] scanYamlDirectiveValue() {
        while(peek() == ' ') {
            forward();
        }
        final String major = scanYamlDirectiveNumber();
        if(peek() != '.') {
            throw new ScannerException("while scanning a directive","expected a digit or '.', but found " + peek() + "(" + ((int)peek()) + ")",null);
        }
        forward();
        final String minor = scanYamlDirectiveNumber();
        if(NULL_BL_LINEBR.indexOf(peek()) == -1) {
            throw new ScannerException("while scanning a directive","expected a digit or ' ', but found " + peek() + "(" + ((int)peek()) + ")",null);
        }
        return new String[] {major,minor};
    }

    private String scanYamlDirectiveNumber() {
        final char ch = peek();
        if(!Character.isDigit(ch)) {
            throw new ScannerException("while scanning a directive","expected a digit, but found " + ch + "(" + ((int)ch) + ")",null);
        }
        int length = 0;
        while(Character.isDigit(peek(length))) {
            length++;
        }
        final String value = prefixForward(length);
        //        forward(length);
        return value;
    }

    private String[] scanTagDirectiveValue()  {
        while(peek() == ' ') {
            forward();
        }
        final String handle = scanTagDirectiveHandle();
        while(peek() == ' ') {
            forward();
        }
        final String prefix = scanTagDirectivePrefix();
        return new String[] {handle,prefix};
    }

    private String scanTagDirectiveHandle() {
        final String value = scanTagHandle("directive");
        if(peek() != ' ') {
            throw new ScannerException("while scanning a directive","expected ' ', but found " + peek() + "(" + ((int)peek()) + ")",null);
        }
        return value;
    }
    
    private String scanTagDirectivePrefix() {
        final String value = scanTagUri("directive");
        if(NULL_BL_LINEBR.indexOf(peek()) == -1) {
            throw new ScannerException("while scanning a directive","expected ' ', but found " + peek() + "(" + ((int)peek()) + ")",null);
        }
        return value;
    }

    private String scanDirectiveIgnoredLine() {
        while(peek() == ' ') {
            forward();
        }
        if(peek() == '"') {
            while(NULL_OR_LINEBR.indexOf(peek()) == -1) {
                forward();
            }
        }
        final char ch = peek();
        if(NULL_OR_LINEBR.indexOf(ch) == -1) {
            throw new ScannerException("while scanning a directive","expected a comment or a line break, but found " + peek() + "(" + ((int)peek()) + ")",null);
        }
        return scanLineBreak();
    }

    private Token scanAnchor(final Token tok) {
        final char indicator = peek();
        final String name = indicator == '*' ? "alias" : "anchor";
        forward();
        int length = 0;
        int chunk_size = 16;
        Matcher m = null;
        for(;;) {
            final String chunk = prefix(chunk_size);
            if((m = NON_ALPHA.matcher(chunk)).find()) {
                break;
            }
            chunk_size+=16;
        }
        length = m.start();
        if(length == 0) {
            throw new ScannerException("while scanning an " + name,"expected alphabetic or numeric character, but found something else...",null);
        }
        final String value = prefixForward(length);
        //        forward(length);
        if(NON_ALPHA_OR_NUM.indexOf(peek()) == -1) {
            throw new ScannerException("while scanning an " + name,"expected alphabetic or numeric character, but found "+ peek() + "(" + ((int)peek()) + ")",null);

        }
        tok.setValue(value);
        return tok;
    }

    private Token scanTag() {
        char ch = peek(1);
        String handle = null;
        String suffix = null;
        if(ch == '<') {
            forward(2);
            suffix = scanTagUri("tag");
            if(peek() != '>') {
                throw new ScannerException("while scanning a tag","expected '>', but found "+ peek() + "(" + ((int)peek()) + ")",null);
            }
            forward();
        } else if(NULL_BL_T_LINEBR.indexOf(ch) != -1) {
            suffix = "!";
            forward();
        } else {
            int length = 1;
            boolean useHandle = false;
            while(NULL_BL_T_LINEBR.indexOf(ch) == -1) {
                if(ch == '!') {
                    useHandle = true;
                    break;
                }
                length++;
                ch = peek(length);
            }
            handle = "!";
            if(useHandle) {
                handle = scanTagHandle("tag");
            } else {
                handle = "!";
                forward();
            }
            suffix = scanTagUri("tag");
        }
        if(NULL_BL_LINEBR.indexOf(peek()) == -1) {
            throw new ScannerException("while scanning a tag","expected ' ', but found " + peek() + "(" + ((int)peek()) + ")",null);
        }
        return new TagToken(new String[] {handle,suffix});
    }

    private Token scanBlockScalar(final char style) {
        final boolean folded = style == '>';
        final StringBuffer chunks = new StringBuffer();
        forward();
        final Object[] chompi = scanBlockScalarIndicators();
        final boolean chomping = ((Boolean)chompi[0]).booleanValue();
        final int increment = ((Integer)chompi[1]).intValue();
        scanBlockScalarIgnoredLine();
        int minIndent = this.indent+1;
        if(minIndent < 1) {
            minIndent = 1;
        }
        String breaks = null;
        int maxIndent = 0;
        int ind = 0;
        if(increment == -1) {
            final Object[] brme = scanBlockScalarIndentation();
            breaks = (String)brme[0];
            maxIndent = ((Integer)brme[1]).intValue();
            if(minIndent > maxIndent) {
                ind = minIndent;
            } else {
                ind = maxIndent;
            }
        } else {
            ind = minIndent + increment - 1;
            breaks = scanBlockScalarBreaks(ind);
        }

        String lineBreak = "";
        while(this.column == ind && peek() != '\0') {
            chunks.append(breaks);
            final boolean leadingNonSpace = BLANK_T.indexOf(peek()) == -1;
            int length = 0;
            while(NULL_OR_LINEBR.indexOf(peek(length))==-1) {
                length++;
            }
            chunks.append(prefixForward(length));
            //            forward(length);
            lineBreak = scanLineBreak();
            breaks = scanBlockScalarBreaks(ind);
            if(this.column == ind && peek() != '\0') {
                if(folded && lineBreak.equals("\n") && leadingNonSpace && BLANK_T.indexOf(peek()) == -1) {
                    if(breaks.length() == 0) {
                        chunks.append(" ");
                    }
                } else {
                    chunks.append(lineBreak);
                }
            } else {
                break;
            }
        }

        if(chomping) {
            chunks.append(lineBreak);
            chunks.append(breaks);
        }

        return new ScalarToken(chunks.toString(),false,style);
    }

    private Object[] scanBlockScalarIndicators() {
        boolean chomping = false;
        int increment = -1;
        char ch = peek();
        if(ch == '-' || ch == '+') {
            chomping = ch == '+';
            forward();
            ch = peek();
            if(Character.isDigit(ch)) {
                increment = Integer.parseInt((""+ch));
                if(increment == 0) {
                    throw new ScannerException("while scanning a block scalar","expected indentation indicator in the range 1-9, but found 0",null);
                }
                forward();
            }
        } else if(Character.isDigit(ch)) {
            increment = Integer.parseInt((""+ch));
            if(increment == 0) {
                throw new ScannerException("while scanning a block scalar","expected indentation indicator in the range 1-9, but found 0",null);
            }
            forward();
            ch = peek();
            if(ch == '-' || ch == '+') {
                chomping = ch == '+';
                forward();
            }
        }
        if(NULL_BL_LINEBR.indexOf(peek()) == -1) {
            throw new ScannerException("while scanning a block scalar","expected chomping or indentation indicators, but found " + peek() + "(" + ((int)peek()) + ")",null);
        }
        return new Object[] {Boolean.valueOf(chomping),new Integer(increment)};
    }

    private String scanBlockScalarIgnoredLine() {
        while(peek() == ' ') {
            forward();
        }
        if(peek() == '#') {
            while(NULL_OR_LINEBR.indexOf(peek()) == -1) {
                forward();
            }
        }
        if(NULL_OR_LINEBR.indexOf(peek()) == -1) {
            throw new ScannerException("while scanning a block scalar","expected a comment or a line break, but found " + peek() + "(" + ((int)peek()) + ")",null);
        }
        return scanLineBreak();
    }

    private Object[] scanBlockScalarIndentation() {
        final StringBuffer chunks = new StringBuffer();
        int maxIndent = 0;
        while(BLANK_OR_LINEBR.indexOf(peek()) != -1) {
            if(peek() != ' ') {
                chunks.append(scanLineBreak());
            } else {
                forward();
                if(this.column > maxIndent) {
                    maxIndent = column;
                }
            }
        }
        return new Object[] {chunks.toString(),new Integer(maxIndent)};
    }

    private String scanBlockScalarBreaks(final int indent) {
        final StringBuffer chunks = new StringBuffer();
        while(this.column < indent && peek() == ' ') {
            forward();
        }
        while(FULL_LINEBR.indexOf(peek()) != -1) {
            chunks.append(scanLineBreak());
            while(this.column < indent && peek() == ' ') {
                forward();
            }
        }
        return chunks.toString();
    }

    private Token scanFlowScalar(final char style) {
        final boolean dbl = style == '"';
        final StringBuffer chunks = new StringBuffer();
        final char quote = peek();
        forward();
        chunks.append(scanFlowScalarNonSpaces(dbl));
        while(peek() != quote) {
            chunks.append(scanFlowScalarSpaces());
            chunks.append(scanFlowScalarNonSpaces(dbl));
        }
        forward();
        return new ScalarToken(chunks.toString(),false,style);
    }

    private String scanFlowScalarNonSpaces(final boolean dbl) {
        final StringBuffer chunks = new StringBuffer();
        for(;;) {
            int length = 0;
            while(SPACES_AND_STUFF.indexOf(peek(length)) == -1) {
                length++;
            }
            if(length != 0) {
                chunks.append(prefixForward(length));
                //                forward(length);
            }
            char ch = peek();
            if(!dbl && ch == '\'' && peek(1) == '\'') {
                chunks.append("'");
                forward(2);
            } else if((dbl && ch == '\'') || (!dbl && DOUBLE_ESC.indexOf(ch) != -1)) {
                chunks.append(ch);
                forward();
            } else if(dbl && ch == '\\') {
                forward();
                ch = peek();
                if(ESCAPE_REPLACEMENTS.containsKey(new Character(ch))) {
                    chunks.append(ESCAPE_REPLACEMENTS.get(new Character(ch)));
                    forward();
                } else if(ESCAPE_CODES.containsKey(new Character(ch))) {
                    length = ((Integer)ESCAPE_CODES.get(new Character(ch))).intValue();
                    forward();
                    final String val = prefix(length);
                    if(NOT_HEXA.matcher(val).find()) {
                        throw new ScannerException("while scanning a double-quoted scalar","expected escape sequence of " + length + " hexadecimal numbers, but found something else: " + val,null);
                    }
                    chunks.append(Integer.parseInt(val,16));
                    forward(length);
                } else if(FULL_LINEBR.indexOf(ch) != -1) {
                    scanLineBreak();
                    chunks.append(scanFlowScalarBreaks());
                } else {
                    throw new ScannerException("while scanning a double-quoted scalar","found unknown escape character " + ch + "(" + ((int)ch) + ")",null);
                }
            } else {
                return chunks.toString();
            }
        }
    }

    private String scanFlowScalarSpaces() {
        final StringBuffer chunks = new StringBuffer();
        int length = 0;
        while(BLANK_T.indexOf(peek(length)) != -1) {
            length++;
        }
        final String whitespaces = prefixForward(length);
        //        forward(length);
        char ch = peek();
        if(ch == '\0') {
            throw new ScannerException("while scanning a quoted scalar","found unexpected end of stream",null);
        } else if(FULL_LINEBR.indexOf(ch) != -1) {
            final String lineBreak = scanLineBreak();
            final String breaks = scanFlowScalarBreaks();
            if(!lineBreak.equals("\n")) {
                chunks.append(lineBreak);
            } else if(breaks.length() == 0) {
                chunks.append(" ");
            }
            chunks.append(breaks);
        } else {
            chunks.append(whitespaces);
        }
        return chunks.toString();
    }

    private String scanFlowScalarBreaks() {
        final StringBuffer chunks = new StringBuffer();
        String pre = null;
        for(;;) {
            pre = prefix(3);
            if((pre.equals("---") || pre.equals("...")) && NULL_BL_T_LINEBR.indexOf(peek(3)) != -1) {
                throw new ScannerException("while scanning a quoted scalar","found unexpected document separator",null);
            }
            while(BLANK_T.indexOf(peek()) != -1) {
                forward();
            }
            if(FULL_LINEBR.indexOf(peek()) != -1) {
                chunks.append(scanLineBreak());
            } else {
                return chunks.toString();
            }            
        }
    }


    private Token scanPlain() {
        /*
       See the specification for details.
       We add an additional restriction for the flow context:
         plain scalars in the flow context cannot contain ',', ':' and '?'.
       We also keep track of the `allow_simple_key` flag here.
       Indentation rules are loosed for the flow context.
         */
        final StringBuffer chunks = new StringBuffer();
        final int ind = this.indent+1;
        String spaces = "";
        boolean f_nzero = true;
        Pattern r_check = R_FLOWNONZERO;
        if(this.flowLevel == 0) {
            f_nzero = false;
            r_check = R_FLOWZERO;
        }
        while(peek() != '#') {
            int length = 0;
            int chunkSize = 32;
            Matcher m = null;
            while(!(m = r_check.matcher(prefix(chunkSize))).find()) {
                chunkSize += 32;
            }
            length = m.start();
            final char ch = peek(length);
            if(f_nzero && ch == ':' && S4.indexOf(peek(length+1)) == -1) {
                forward(length);
                throw new ScannerException("while scanning a plain scalar","found unexpected ':'","Please check http://pyyaml.org/wiki/YAMLColonInFlowContext for details.");
            }
            if(length == 0) {
                break;
            }
            this.allowSimpleKey = false;
            chunks.append(spaces);
            chunks.append(prefixForward(length));
            //            forward(length);
            spaces = scanPlainSpaces(ind);
            if(spaces == null || (this.flowLevel == 0 && this.column < ind)) {
                break;
            }
        }
        return new ScalarToken(chunks.toString(),true);
    }

    private String scanPlainSpaces(final int indent) {
        final StringBuffer chunks = new StringBuffer();
        int length = 0;
        while(peek(length) == ' ') {
            length++;
        }
        final String whitespaces = prefixForward(length);
        //        forward(length);
        char ch  = peek();
        if(FULL_LINEBR.indexOf(ch) != -1) {
            final String lineBreak = scanLineBreak();
            this.allowSimpleKey = true;
            if(END_OR_START.matcher(prefix(4)).matches()) {
                return "";
            }
            final StringBuffer breaks = new StringBuffer();
            while(BLANK_OR_LINEBR.indexOf(peek()) != -1) {
                if(' ' == peek()) {
                    forward();
                } else {
                    breaks.append(scanLineBreak());
                    if(END_OR_START.matcher(prefix(4)).matches()) {
                        return "";
                    }
                }
            }            
            if(!lineBreak.equals("\n")) {
                chunks.append(lineBreak);
            } else if(breaks == null || breaks.toString().equals("")) {
                chunks.append(" ");
            }
            chunks.append(breaks);
        } else {
            chunks.append(whitespaces);
        }
        return chunks.toString();
    }

    private String scanTagHandle(final String name) {
        char ch =  peek();
        if(ch != '!') {
            throw new ScannerException("while scanning a " + name,"expected '!', but found " + ch + "(" + ((int)ch) + ")",null);
        }
        int length = 1;
        ch = peek(length);
        if(ch != ' ') {
            while(ALPHA.indexOf(ch) != -1) {
                length++;
                ch = peek(length);
            }
            if('!' != ch) {
                forward(length);
                throw new ScannerException("while scanning a " + name,"expected '!', but found " + ch + "(" + ((int)ch) + ")",null);
            }
            length++;
        }
        final String value = prefixForward(length);
        //        forward(length);
        return value;
    }

    private String scanTagUri(final String name) {
        final StringBuffer chunks = new StringBuffer();
        int length = 0;
        char ch = peek(length);
        while(STRANGE_CHAR.indexOf(ch) != -1) {
            if('%' == ch) {
                chunks.append(prefixForward(length));
                //                forward(length);
                length = 0;
                chunks.append(scanUriEscapes(name));
            } else {
                length++;
            }
            ch = peek(length);
        }
        if(length != 0) {
            chunks.append(prefixForward(length));
            //            forward(length);
        }

        if(chunks.length() == 0) {
            throw new ScannerException("while scanning a " + name,"expected URI, but found " + ch + "(" + ((int)ch) + ")",null);
        }
        return chunks.toString();
    }

    private String scanUriEscapes(final String name) {
        final StringBuffer bytes = new StringBuffer();
        while(peek() == '%') {
            forward();
            try {
                bytes.append(Integer.parseInt(prefix(2),16));
            } catch(final NumberFormatException nfe) {
                throw new ScannerException("while scanning a " + name,"expected URI escape sequence of 2 hexadecimal numbers, but found " + peek(1) + "(" + ((int)peek(1)) + ") and "+ peek(2) + "(" + ((int)peek(2)) + ")",null);
            }
            forward(2);
        }
        return bytes.toString();
    }

    private String scanLineBreak() {
        // Transforms:
        //   '\r\n'      :   '\n'
        //   '\r'        :   '\n'
        //   '\n'        :   '\n'
        //   '\x85'      :   '\n'
        //   default     :   ''
        final char val = peek();
        if(FULL_LINEBR.indexOf(val) != -1) {
            if(RN.equals(prefix(2))) {
                forward(2);
            } else {
                forward();
            }
            return "\n";
        } else {
            return "";
        }
    }

















    /*
    public static void oldmain(final String[] args) {
        final String test1 = "--- \nA: b\nc: 3.14\n";
        final String filename = args[0];
        
        final ScannerImpl sce = new ScannerImpl(test1);
        System.out.println("Reading of string: \"" + test1 + "\"");
        while(!sce.eof) {
            int toShow = 20;
            if(sce.buffer.remaining()<20) {
                toShow = sce.buffer.remaining();
            }
            System.out.println("--prefix" + toShow + ": \"" + sce.prefix(toShow) + "\"");
            sce.forward(toShow);
        }
        
        System.out.println("Reading of file: \"" + filename + "\"");
        final ScannerImpl sce2 = new ScannerImpl(new FileReader(filename));
        while(!sce2.eof) {
            int toShow = 20;
            if(sce2.buffer.remaining()<20) {
                toShow = sce2.buffer.remaining();
            }
            System.out.println("--prefix" + toShow + ": \"" + sce2.prefix(toShow) + "\"");
            sce2.forward(toShow);
        }
    }
    */

    public static void main(final String[] args) throws Exception {
        //        final String test1 = "--- \nA: b\nc: 3.14\n";
        final String filename = args[0];
        /*
        final Scanner sce = new ScannerImpl(test1);
        System.out.println("Reading of string: \"" + test1 + "\"");
        for(final Iterator iter = sce.eachToken();iter.hasNext();) {
            System.out.println(iter.next());
        }
        */
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
            final Scanner sce2 = new ScannerImpl(str);
            for(final Iterator iter = sce2.eachToken();iter.hasNext();) {
                System.out.println(iter.next());
            }
        }
        final long after = System.currentTimeMillis();
        final long time = after-before;
        final double timeS = (after-before)/1000.0;
        System.out.println("Walking through the tokens for the file: " + filename + " took " + time + "ms, or " + timeS + " seconds"); 
    }

}// Scanner
