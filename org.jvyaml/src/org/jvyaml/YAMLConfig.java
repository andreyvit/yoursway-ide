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
 * $Id: YAMLConfig.java,v 1.1 2006/06/19 06:05:12 olabini Exp $
 */
package org.jvyaml;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.1 $
 */
public interface YAMLConfig {
    YAMLConfig indent(final int indent);
    int indent();
    YAMLConfig useHeader(final boolean useHeader);
    boolean useHeader();
    YAMLConfig useVersion(final boolean useVersion);
    boolean useVersion();
    YAMLConfig version(final String version);
    String version();
    YAMLConfig explicitStart(final boolean expStart);
    boolean explicitStart();
    YAMLConfig explicitEnd(final boolean expEnd);
    boolean explicitEnd();
    YAMLConfig anchorFormat(final String format);
    String anchorFormat();
    YAMLConfig explicitTypes(final boolean expTypes);
    boolean explicitTypes();
    YAMLConfig canonical(final boolean canonical);
    boolean canonical();
    YAMLConfig bestWidth(final int bestWidth);
    int bestWidth();
    YAMLConfig useBlock(final boolean useBlock);
    boolean useBlock();
    YAMLConfig useFlow(final boolean useFlow);
    boolean useFlow();
    YAMLConfig usePlain(final boolean usePlain);
    boolean usePlain();
    YAMLConfig useSingle(final boolean useSingle);
    boolean useSingle();
    YAMLConfig useDouble(final boolean useDouble);
    boolean useDouble();
}// YAMLConfig
