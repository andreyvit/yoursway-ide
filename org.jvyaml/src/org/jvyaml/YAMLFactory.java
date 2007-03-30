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
 * $Id: YAMLFactory.java,v 1.2 2006/09/24 16:32:35 olabini Exp $
 */
package org.jvyaml;

import java.io.Reader;
import java.io.Writer;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.2 $
 */
public interface YAMLFactory {
    Scanner createScanner(final String io);
    Scanner createScanner(final Reader io);
    Parser createParser(final Scanner scanner);
    Parser createParser(final Scanner scanner, final YAMLConfig cfg);
    Resolver createResolver();
    Composer createComposer(final Parser parser, final Resolver resolver);
    Constructor createConstructor(final Composer composer);
    Emitter createEmitter(final Writer output, final YAMLConfig cfg);
    Serializer createSerializer(final Emitter emitter, final Resolver resolver, final YAMLConfig cfg);
    Representer createRepresenter(final Serializer serializer, final YAMLConfig cfg);
}// YAMLFactory
