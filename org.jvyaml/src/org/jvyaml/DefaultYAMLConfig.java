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
 * $Id: DefaultYAMLConfig.java,v 1.2 2006/09/23 17:50:39 olabini Exp $
 */
package org.jvyaml;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.2 $
 */
public class DefaultYAMLConfig implements YAMLConfig {
    private int indent = 2;
    private boolean useHeader = false;
    private boolean useVersion = false;
    private String version = "1.1";
    private boolean expStart = true;
    private boolean expEnd = false;
    private String format = "id{0,number,####}";
    private boolean expTypes = false;
    private boolean canonical = false;
    private int bestWidth = 80;
    private boolean useBlock = false;
    private boolean useFlow = false;
    private boolean usePlain = false;
    private boolean useSingle = false;
    private boolean useDouble = false;
    
    public YAMLConfig indent(final int indent) { this.indent = indent; return this; }
    public int indent() { return this.indent; }
    public YAMLConfig useHeader(final boolean useHeader) { this.useHeader = useHeader; return this; }
    public boolean useHeader() { return this.useHeader; }
    public YAMLConfig useVersion(final boolean useVersion) { this.useVersion = useVersion; return this; }
    public boolean useVersion() { return this.useVersion; }
    public YAMLConfig version(final String version) { this.version = version; return this; }
    public String version() { return this.version; }
    public YAMLConfig explicitStart(final boolean expStart) { this.expStart = expStart; return this; }
    public boolean explicitStart() { return this.expStart; }
    public YAMLConfig explicitEnd(final boolean expEnd) { this.expEnd = expEnd; return this; }
    public boolean explicitEnd() { return this.expEnd; }
    public YAMLConfig anchorFormat(final String format) { this.format = format; return this; }
    public String anchorFormat() { return this.format; }
    public YAMLConfig explicitTypes(final boolean expTypes) { this.expTypes = expTypes; return this; }
    public boolean explicitTypes() { return this.expTypes; }
    public YAMLConfig canonical(final boolean canonical) { this.canonical = canonical; return this; }
    public boolean canonical() { return this.canonical; }
    public YAMLConfig bestWidth(final int bestWidth) { this.bestWidth = bestWidth; return this; }
    public int bestWidth() { return this.bestWidth; }
    public YAMLConfig useBlock(final boolean useBlock) { this.useBlock = useBlock; return this; }
    public boolean useBlock() { return this.useBlock; }
    public YAMLConfig useFlow(final boolean useFlow) { this.useFlow = useFlow; return this; }
    public boolean useFlow() { return this.useFlow; }
    public YAMLConfig usePlain(final boolean usePlain) { this.usePlain = usePlain; return this; }
    public boolean usePlain() { return this.usePlain; }
    public YAMLConfig useSingle(final boolean useSingle) { this.useSingle = useSingle; return this; }
    public boolean useSingle() { return this.useSingle; }
    public YAMLConfig useDouble(final boolean useDouble) { this.useDouble = useDouble; return this; }
    public boolean useDouble() { return this.useDouble; }
}// DefaultYAMLConfig
