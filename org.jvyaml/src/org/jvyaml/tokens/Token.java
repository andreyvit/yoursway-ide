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
 * $Id: Token.java,v 1.1 2006/06/06 19:19:21 olabini Exp $
 */
package org.jvyaml.tokens;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.1 $
 */
public abstract class Token {
    public final static Token DOCUMENT_START = new DocumentStartToken();
    public final static Token DOCUMENT_END = new DocumentEndToken();
    public final static Token BLOCK_MAPPING_START = new BlockMappingStartToken();
    public final static Token BLOCK_SEQUENCE_START = new BlockSequenceStartToken();
    public final static Token BLOCK_ENTRY = new BlockEntryToken();
    public final static Token BLOCK_END = new BlockEndToken();
    public final static Token FLOW_ENTRY = new FlowEntryToken();
    public final static Token FLOW_MAPPING_END = new FlowMappingEndToken();
    public final static Token FLOW_MAPPING_START = new FlowMappingStartToken();
    public final static Token FLOW_SEQUENCE_END = new FlowSequenceEndToken();
    public final static Token FLOW_SEQUENCE_START = new FlowSequenceStartToken();
    public final static Token KEY = new KeyToken();
    public final static Token VALUE = new ValueToken();
    public final static Token STREAM_END = new StreamEndToken();
    public final static Token STREAM_START = new StreamStartToken();

    public Token() {
    }

    public void setValue(final Object value) {
    }

    public String toString() {
        return "#<" + this.getClass().getName() + ">";
    }
}// Token
