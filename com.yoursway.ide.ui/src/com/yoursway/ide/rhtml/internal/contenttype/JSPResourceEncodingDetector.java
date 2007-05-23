package com.yoursway.ide.rhtml.internal.contenttype;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.sse.core.internal.encoding.CodedIO;
import org.eclipse.wst.sse.core.internal.encoding.EncodingMemento;
import org.eclipse.wst.sse.core.internal.encoding.IResourceCharsetDetector;
import org.eclipse.wst.sse.core.internal.encoding.NonContentBasedEncodingRules;
import org.eclipse.wst.sse.core.internal.encoding.util.ByteReader;
import org.eclipse.wst.xml.core.internal.contenttype.EncodingParserConstants;
import org.eclipse.wst.xml.core.internal.contenttype.XMLHeadTokenizerConstants;

public class JSPResourceEncodingDetector implements IResourceCharsetDetector {
    
    private String fCharset;
    
    private String fContentType;
    
    private String fContentTypeValue;
    
    private String fLanguage;
    
    private String fPageEncodingValue;
    
    private JSPHeadTokenizer fTokenizer;
    
    private String fXMLDecEncodingName;
    
    private boolean unicodeCase;
    
    private EncodingMemento fEncodingMemento;
    
    private boolean fHeaderParsed;
    
    private Reader fReader;
    
    private boolean fXHTML;
    
    private boolean fWML;
    
    /**
     * No Arg constructor.
     */
    public JSPResourceEncodingDetector() {
        super();
    }
    
    class NullMemento extends EncodingMemento {
        /**
         * 
         */
        public NullMemento() {
            super();
            String defaultCharset = NonContentBasedEncodingRules.useDefaultNameRules(null);
            setJavaCharsetName(defaultCharset);
            setAppropriateDefault(defaultCharset);
            setDetectedCharsetName(null);
        }
        
    }
    
    /**
     * @return Returns the contentType.
     */
    public String getContentType() throws IOException {
        ensureInputSet();
        if (!fHeaderParsed) {
            parseInput();
            // we keep track of if header's already been parse, so can make
            // multiple 'get' calls, without causing reparsing.
            fHeaderParsed = true;
            // Note: there is a "hidden assumption" here that an empty
            // string in content should be treated same as not present.
        }
        return fContentType;
    }
    
    public String getEncoding() throws IOException {
        return getEncodingMemento().getDetectedCharsetName();
    }
    
    // to ensure consist overall rules used, we'll mark as
    // final,
    // and require subclasses to provide certain pieces of
    // the
    // implementation
    public EncodingMemento getEncodingMemento() throws IOException {
        ensureInputSet();
        if (!fHeaderParsed) {
            parseInput();
            // we keep track of if header's already been
            // parse, so can make
            // multiple 'get' calls, without causing
            // reparsing.
            fHeaderParsed = true;
            // Note: there is a "hidden assumption" here
            // that an empty
            // string in content should be treated same as
            // not present.
        }
        if (fEncodingMemento == null) {
            handleSpecDefault();
        }
        if (fEncodingMemento == null) {
            // safty net
            fEncodingMemento = new NullMemento();
        }
        return fEncodingMemento;
    }
    
    public String getLanguage() throws IOException {
        ensureInputSet();
        if (!fHeaderParsed) {
            parseInput();
            fHeaderParsed = true;
        }
        return fLanguage;
    }
    
    public String getSpecDefaultEncoding() {
        // by JSP Spec
        final String enc = "ISO-8859-1"; //$NON-NLS-1$
        return enc;
    }
    
    public EncodingMemento getSpecDefaultEncodingMemento() {
        resetAll();
        EncodingMemento result = null;
        String enc = getSpecDefaultEncoding();
        if (enc != null) {
            createEncodingMemento(enc, EncodingMemento.DEFAULTS_ASSUMED_FOR_EMPTY_INPUT);
            fEncodingMemento.setAppropriateDefault(enc);
            result = fEncodingMemento;
        }
        return result;
    }
    
    /**
     * 
     */
    public void set(InputStream inputStream) {
        resetAll();
        fReader = new ByteReader(inputStream);
        try {
            fReader.mark(CodedIO.MAX_MARK_SIZE);
        } catch (IOException e) {
            // impossible, since we know ByteReader
            // supports marking
            throw new Error(e);
        }
    }
    
    /**
     * 
     */
    public void set(IStorage iStorage) throws CoreException {
        resetAll();
        InputStream inputStream = iStorage.getContents();
        InputStream resettableStream = new BufferedInputStream(inputStream, CodedIO.MAX_BUF_SIZE);
        resettableStream.mark(CodedIO.MAX_MARK_SIZE);
        set(resettableStream);
        // TODO we'll need to "remember" IFile, or
        // get its (or its project's) settings, in case
        // those are needed to handle cases when the
        // encoding is not in the file stream.
    }
    
    /**
     * Note: this is not part of interface to help avoid confusion ... it
     * expected this Reader is a well formed character reader ... that is, its
     * all ready been determined to not be a unicode marked input stream. And,
     * its assumed to be in the correct position, at position zero, ready to
     * read first character.
     */
    public void set(Reader reader) {
        resetAll();
        fReader = reader;
        if (!fReader.markSupported()) {
            fReader = new BufferedReader(fReader);
        }
        try {
            fReader.mark(CodedIO.MAX_MARK_SIZE);
        } catch (IOException e) {
            // impossble, since we just checked if markable
            throw new Error(e);
        }
    }
    
    private boolean canHandleAsUnicodeStream(String tokenType) {
        boolean canHandleAsUnicode = false;
        if (tokenType == EncodingParserConstants.UTF83ByteBOM) {
            canHandleAsUnicode = true;
            String enc = "UTF-8"; //$NON-NLS-1$
            createEncodingMemento(enc, EncodingMemento.DETECTED_STANDARD_UNICODE_BYTES);
            fEncodingMemento.setUTF83ByteBOMUsed(true);
        } else if (tokenType == EncodingParserConstants.UTF16BE) {
            canHandleAsUnicode = true;
            String enc = "UTF-16BE"; //$NON-NLS-1$
            createEncodingMemento(enc, EncodingMemento.DETECTED_STANDARD_UNICODE_BYTES);
        } else if (tokenType == EncodingParserConstants.UTF16LE) {
            canHandleAsUnicode = true;
            String enc = "UTF-16"; //$NON-NLS-1$
            createEncodingMemento(enc, EncodingMemento.DETECTED_STANDARD_UNICODE_BYTES);
        }
        return canHandleAsUnicode;
    }
    
    /**
     * Note: once this instance is created, trace info still needs to be
     * appended by caller, depending on the context its created.
     */
    private void createEncodingMemento(String detectedCharsetName) {
        fEncodingMemento = new EncodingMemento();
        fEncodingMemento.setJavaCharsetName(getAppropriateJavaCharset(detectedCharsetName));
        fEncodingMemento.setDetectedCharsetName(detectedCharsetName);
        // TODO: if detectedCharset and spec default is
        // null, need to use "work
        // bench based" defaults.
        fEncodingMemento.setAppropriateDefault(getSpecDefaultEncoding());
    }
    
    /**
     * There can sometimes be mulitple 'encodings' specified in a file. This is
     * an attempt to centralize the rules for deciding between them. Returns
     * encoding according to priority: 1. XML Declaration 2. page directive
     * pageEncoding name 3. page directive contentType charset name
     */
    private String getAppropriateEncoding() {
        String result = null;
        if (fXMLDecEncodingName != null)
            result = fXMLDecEncodingName;
        else if (fPageEncodingValue != null)
            result = fPageEncodingValue;
        else if (fCharset != null)
            result = fCharset;
        return result;
    }
    
    /**
     * This method can return null, if invalid charset name (in which case
     * "appropriateDefault" should be used, if a name is really need for some
     * "save anyway" cases).
     * 
     * @param detectedCharsetName
     * @return
     */
    private String getAppropriateJavaCharset(String detectedCharsetName) {
        String result = null;
        // 1. Check explicit mapping overrides from
        // property file -- its here we pick up "rules" for cases
        // that are not even in Java
        result = CodedIO.checkMappingOverrides(detectedCharsetName);
        // 2. Use the "canonical" name from JRE mappings
        // Note: see Charset JavaDoc, the name you get one
        // with can be alias,
        // the name you get back is "standard" name.
        Charset javaCharset = null;
        try {
            javaCharset = Charset.forName(detectedCharsetName);
        } catch (UnsupportedCharsetException e) {
            // only set invalid, if result is same as detected -- they won't
            // be equal if
            // overridden
            if (result != null && result.equals(detectedCharsetName)) {
                fEncodingMemento.setInvalidEncoding(detectedCharsetName);
            }
        } catch (IllegalCharsetNameException e) {
            // only set invalid, if result is same as detected -- they won't
            // be equal if
            // overridden
            if (result != null && result.equals(detectedCharsetName)) {
                fEncodingMemento.setInvalidEncoding(detectedCharsetName);
            }
        }
        // give priority to java cononical name, if present
        if (javaCharset != null) {
            result = javaCharset.name();
            // but still allow overrides
            result = CodedIO.checkMappingOverrides(result);
        }
        return result;
    }
    
    private JSPHeadTokenizer getTokinizer() {
        if (fTokenizer == null) {
            fTokenizer = new JSPHeadTokenizer();
        }
        return fTokenizer;
    }
    
    private void handleSpecDefault() {
        String encodingName;
        encodingName = getSpecDefaultEncoding();
        if (encodingName != null) {
            // createEncodingMemento(encodingName,
            // EncodingMemento.USED_CONTENT_TYPE_DEFAULT);
            fEncodingMemento = new EncodingMemento();
            fEncodingMemento.setJavaCharsetName(encodingName);
            fEncodingMemento.setAppropriateDefault(encodingName);
        }
    }
    
    private boolean isLegalString(String valueTokenType) {
        boolean result = false;
        if (valueTokenType != null) {
            result = valueTokenType.equals(EncodingParserConstants.StringValue)
                    || valueTokenType.equals(EncodingParserConstants.UnDelimitedStringValue)
                    || valueTokenType.equals(EncodingParserConstants.InvalidTerminatedStringValue)
                    || valueTokenType
                            .equals(EncodingParserConstants.InvalidTermintatedUnDelimitedStringValue);
        }
        return result;
    }
    
    /**
     * This method should be exactly the same as what is in
     * JSPHeadTokenizerTester
     * 
     * @param contentType
     */
    private void parseContentTypeValue(String contentType) {
        Pattern pattern = Pattern.compile(";\\s*charset\\s*=\\s*"); //$NON-NLS-1$
        String[] parts = pattern.split(contentType);
        if (parts.length > 0) {
            // if only one item, it can still be charset instead of
            // contentType
            if (parts.length == 1) {
                if (parts[0].length() > 6) {
                    String checkForCharset = parts[0].substring(0, 7);
                    if (checkForCharset.equalsIgnoreCase("charset")) { //$NON-NLS-1$
                        int eqpos = parts[0].indexOf('=');
                        eqpos = eqpos + 1;
                        if (eqpos < parts[0].length()) {
                            fCharset = parts[0].substring(eqpos);
                            fCharset = fCharset.trim();
                        }
                    } else {
                        fContentType = parts[0];
                    }
                }
            } else {
                fContentType = parts[0];
            }
        }
        if (parts.length > 1) {
            fCharset = parts[1];
        }
    }
    
    /**
     * Looks for what ever encoding properties the tokenizer returns. Its the
     * responsibility of the tokenizer to stop when appropriate and not go too
     * far.
     */
    private void parseHeader(JSPHeadTokenizer tokenizer) throws IOException {
        fPageEncodingValue = null;
        fCharset = null;
        
        HeadParserToken token = null;
        do {
            // don't use 'get' here (at least until reset issue fixed)
            token = tokenizer.getNextToken();
            String tokenType = token.getType();
            if (canHandleAsUnicodeStream(tokenType))
                unicodeCase = true;
            else {
                
                if (tokenType == XMLHeadTokenizerConstants.XMLDelEncoding) {
                    if (tokenizer.hasMoreTokens()) {
                        HeadParserToken valueToken = tokenizer.getNextToken();
                        String valueTokenType = valueToken.getType();
                        if (isLegalString(valueTokenType)) {
                            fXMLDecEncodingName = valueToken.getText();
                        }
                    }
                } else if (tokenType == JSPHeadTokenizerConstants.PageEncoding) {
                    if (tokenizer.hasMoreTokens()) {
                        HeadParserToken valueToken = tokenizer.getNextToken();
                        String valueTokenType = valueToken.getType();
                        if (isLegalString(valueTokenType)) {
                            fPageEncodingValue = valueToken.getText();
                        }
                    }
                } else if (tokenType == JSPHeadTokenizerConstants.PageContentType) {
                    if (tokenizer.hasMoreTokens()) {
                        HeadParserToken valueToken = tokenizer.getNextToken();
                        String valueTokenType = valueToken.getType();
                        if (isLegalString(valueTokenType)) {
                            fContentTypeValue = valueToken.getText();
                        }
                    }
                } else if (tokenType == JSPHeadTokenizerConstants.PageLanguage) {
                    if (tokenizer.hasMoreTokens()) {
                        HeadParserToken valueToken = tokenizer.getNextToken();
                        String valueTokenType = valueToken.getType();
                        if (isLegalString(valueTokenType)) {
                            fLanguage = valueToken.getText();
                        }
                    }
                }
            }
        } while (tokenizer.hasMoreTokens());
        if (fContentTypeValue != null) {
            parseContentTypeValue(fContentTypeValue);
        }
        if (tokenizer.isXHTML()) {
            fXHTML = true;
        }
        if (tokenizer.isWML()) {
            fWML = true;
        }
        
    }
    
    private void parseInput() throws IOException {
        JSPHeadTokenizer tokenizer = getTokinizer();
        fReader.reset();
        tokenizer.reset(fReader);
        parseHeader(tokenizer);
        // unicode stream cases are created directly in parseHeader
        if (!unicodeCase) {
            String enc = getAppropriateEncoding();
            if (enc != null && enc.length() > 0) {
                createEncodingMemento(enc, EncodingMemento.FOUND_ENCODING_IN_CONTENT);
            }
        }
    }
    
    /**
     * 
     */
    private void resetAll() {
        fReader = null;
        fHeaderParsed = false;
        fEncodingMemento = null;
        fCharset = null;
        fContentTypeValue = null;
        fPageEncodingValue = null;
        fXMLDecEncodingName = null;
        unicodeCase = false;
        fXHTML = false;
        fWML = false;
    }
    
    /**
     * convience method all subclasses can use (but not override)
     * 
     * @param detectedCharsetName
     * @param reason
     */
    private void createEncodingMemento(String detectedCharsetName, String reason) {
        createEncodingMemento(detectedCharsetName);
    }
    
    /**
     * convience method all subclasses can use (but not override)
     */
    private void ensureInputSet() {
        if (fReader == null) {
            throw new IllegalStateException("input must be set before use"); //$NON-NLS-1$
        }
    }
    
    public boolean isWML() throws IOException {
        ensureInputSet();
        if (!fHeaderParsed) {
            parseInput();
            // we keep track of if header's already been parse, so can make
            // multiple 'get' calls, without causing reparsing.
            fHeaderParsed = true;
            // Note: there is a "hidden assumption" here that an empty
            // string in content should be treated same as not present.
        }
        return fWML;
    }
    
    public boolean isXHTML() throws IOException {
        ensureInputSet();
        if (!fHeaderParsed) {
            parseInput();
            // we keep track of if header's already been parse, so can make
            // multiple 'get' calls, without causing reparsing.
            fHeaderParsed = true;
            // Note: there is a "hidden assumption" here that an empty
            // string in content should be treated same as not present.
        }
        return fXHTML;
    }
}
