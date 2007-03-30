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
 * $Id: Node.java,v 1.1 2006/06/06 19:19:17 olabini Exp $
 */
package org.jvyaml.nodes;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.1 $
 */
public abstract class Node {
    private String tag;
    private Object value;
    private int hash = -1;

    public Node(final String tag, final Object value) {
        this.tag = tag;
        this.value = value;
    }

    public String getTag() {
        return this.tag;
    }

    public Object getValue() {
        return this.value;
    }

    public int hashCode() {
        if(hash == -1) {
            hash = 3;
            hash += (null == tag) ? 0 : 3*tag.hashCode();
            hash += (null == value) ? 0 : 3*value.hashCode();
        }
        return hash;
    }

    public boolean equals(final Object oth) {
        if(oth instanceof Node) {
            final Node nod = (Node)oth;
            return ((this.tag != null) ? this.tag.equals(nod.tag) : nod.tag == null) && 
                ((this.value != null) ? this.value.equals(nod.value) : nod.value == null);
        }
        return false;
    }

    public String toString() {
        return "#<" + this.getClass().getName() + " (tag=" + getTag() + ", value=" + getValue()+")>";
    }
}// Node
