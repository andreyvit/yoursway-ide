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
 * $Id: DefaultYAMLFactory.java,v 1.2 2006/09/24 16:32:34 olabini Exp $
 */
package org.jvyaml;

import java.io.Reader;
import java.io.Writer;

/**
 * @author <a href="mailto:ola.bini@ki.se">Ola Bini</a>
 * @version $Revision: 1.2 $
 */
public class DefaultYAMLFactory implements YAMLFactory {
    public Scanner createScanner(final String io) {
        return new ScannerImpl(io);
    }
    public Scanner createScanner(final Reader io) {
        return new ScannerImpl(io);
    }
    public Parser createParser(final Scanner scanner) {
        return new ParserImpl(scanner);
    }
    public Parser createParser(final Scanner scanner, final YAMLConfig cfg) {
        return new ParserImpl(scanner,cfg);
    }
    public Resolver createResolver() {
        return new ResolverImpl();
    }
    public Composer createComposer(final Parser parser, final Resolver resolver) {
        return new ComposerImpl(parser,resolver);
    }
    public Constructor createConstructor(final Composer composer) {
        return new ConstructorImpl(composer);
    }
    public Emitter createEmitter(final Writer output, final YAMLConfig cfg) {
        return new EmitterImpl(output,cfg);
    }
    public Serializer createSerializer(final Emitter emitter, final Resolver resolver, final YAMLConfig cfg) {
        return new SerializerImpl(emitter,resolver,cfg);
    }
    public Representer createRepresenter(final Serializer serializer, final YAMLConfig cfg) {
        return new RepresenterImpl(serializer,cfg);
    }
}// DefaultYAMLFactory
