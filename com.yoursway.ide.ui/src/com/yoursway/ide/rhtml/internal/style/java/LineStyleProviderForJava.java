package com.yoursway.ide.rhtml.internal.style.java;

import java.util.Collection;
import java.util.HashMap;

import org.eclipse.dltk.ruby.internal.ui.RubyUI;
import org.eclipse.dltk.ruby.internal.ui.text.RubyCodeScanner;
import org.eclipse.dltk.ruby.internal.ui.text.RubyColorConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.wst.html.ui.internal.style.IStyleConstantsHTML;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.internal.preferences.ui.ColorHelper;
import org.eclipse.wst.sse.ui.internal.provisional.style.Highlighter;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;
import org.eclipse.wst.sse.ui.internal.util.EditorUtility;
import org.eclipse.wst.xml.ui.internal.style.IStyleConstantsXML;

import com.yoursway.ide.rhtml.internal.style.IStyleConstantsJSP;

@SuppressWarnings("restriction")
public class LineStyleProviderForJava implements LineStyleProvider {
    private class PropertyChangeListener implements IPropertyChangeListener {
        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent event) {
            // have to do it this way so others can override the method
            handlePropertyChange(event);
        }
    }
    
    private IDocument fDocument;
    private Highlighter fHighlighter;
    private boolean fIsInitialized = false;
    private final PropertyChangeListener fPreferenceListener = new PropertyChangeListener();
    /** The scanner it uses */
    private final RubyCodeScanner fScanner;
    /** Contains all text attributes pretaining to this line style provider */
    private HashMap<String, TextAttribute> fTextAttributes = null;
    
    public LineStyleProviderForJava() {
        super();
        fScanner = new RubyCodeScannerForRHTML(RubyUI.getDefault().getTextTools().getColorManager(), RubyUI
                .getDefault().getPreferenceStore());
    }
    
    /**
     * Adds style information to the given text presentation.
     * 
     * @param presentation
     *            the text presentation to be extended
     * @param offset
     *            the offset of the range to be styled
     * @param length
     *            the length of the range to be styled
     * @param attr
     *            the attribute describing the style of the range to be styled
     */
    private void addRange(Collection<StyleRange> presentation, int offset, int length, TextAttribute attr) {
        // support for user defined backgroud for JSP scriptlet regions
        TextAttribute ta = getTextAttributes().get(IStyleConstantsJSP.JSP_CONTENT);
        Color bgColor = ta.getBackground();
        if (bgColor == null)
            bgColor = attr.getBackground();
        StyleRange result = new StyleRange(offset, length, attr.getForeground(), bgColor, attr.getStyle());
        if ((attr.getStyle() & TextAttribute.STRIKETHROUGH) != 0) {
            result.strikeout = true;
        }
        if ((attr.getStyle() & TextAttribute.UNDERLINE) != 0) {
            result.underline = true;
        }
        presentation.add(result);
    }
    
    /**
     * Looks up the colorKey in the preference store and adds the style
     * information to list of TextAttributes
     * 
     * @param colorKey
     */
    private void addTextAttribute(String colorKey) {
        if (getColorPreferences() != null) {
            String prefString = getColorPreferences().getString(colorKey);
            String[] stylePrefs = ColorHelper.unpackStylePreferences(prefString);
            if (stylePrefs != null) {
                RGB foreground = ColorHelper.toRGB(stylePrefs[0]);
                RGB background = ColorHelper.toRGB(stylePrefs[1]);
                boolean bold = Boolean.valueOf(stylePrefs[2]).booleanValue();
                boolean italic = Boolean.valueOf(stylePrefs[3]).booleanValue();
                boolean strikethrough = Boolean.valueOf(stylePrefs[4]).booleanValue();
                boolean underline = Boolean.valueOf(stylePrefs[5]).booleanValue();
                int style = SWT.NORMAL;
                if (bold) {
                    style = style | SWT.BOLD;
                }
                if (italic) {
                    style = style | SWT.ITALIC;
                }
                if (strikethrough) {
                    style = style | TextAttribute.STRIKETHROUGH;
                }
                if (underline) {
                    style = style | TextAttribute.UNDERLINE;
                }
                
                TextAttribute createTextAttribute = createTextAttribute(foreground, background, style);
                getTextAttributes().put(colorKey, createTextAttribute);
            }
        }
    }
    
    /**
     * Looks up the colorKey in the preference store and adds the style
     * information to list of TextAttributes
     * 
     * @param colorKey
     */
    private void addJavaTextAttribute(String colorKey) {
        IPreferenceStore store = getJavaColorPreferences();
        if (store != null && colorKey != null) {
            TextAttribute ta = fScanner.getTextAttribute(colorKey);
            getTextAttributes().put(colorKey, ta);
        }
    }
    
    private TextAttribute createTextAttribute(RGB foreground, RGB background, int style) {
        return new TextAttribute((foreground != null) ? EditorUtility.getColor(foreground) : null,
                (background != null) ? EditorUtility.getColor(background) : null, style);
    }
    
    /**
     * Returns the hashtable containing all the text attributes for this line
     * style provider. Lazily creates a hashtable if one has not already been
     * created.
     * 
     * @return
     */
    private HashMap<String, TextAttribute> getTextAttributes() {
        if (fTextAttributes == null) {
            fTextAttributes = new HashMap<String, TextAttribute>();
            loadColors();
        }
        return fTextAttributes;
    }
    
    /**
     * Returns a text attribute encoded in the given token. If the token's data
     * is not <code>null</code> and a text attribute it is assumed that it is
     * the encoded text attribute. It returns the default text attribute if
     * there is no encoded text attribute found.
     * 
     * @param token
     *            the token whose text attribute is to be determined
     * @return the token's text attribute
     */
    private TextAttribute getTokenTextAttribute(IToken token) {
        TextAttribute ta = null;
        
        Object data = token.getData();
        if (data instanceof TextAttribute)
            ta = (TextAttribute) data;
        else {
            ta = getTextAttributes().get(RubyColorConstants.RUBY_DEFAULT);
        }
        return ta;
    }
    
    public void init(IStructuredDocument document, Highlighter highlighter) {
        fDocument = document;
        fHighlighter = highlighter;
        
        if (fIsInitialized)
            return;
        
        registerPreferenceListener();
        
        fIsInitialized = true;
    }
    
    private void loadColors() {
        addTextAttribute(IStyleConstantsHTML.SCRIPT_AREA_BORDER);
        addTextAttribute(IStyleConstantsXML.TAG_ATTRIBUTE_NAME);
        addTextAttribute(IStyleConstantsXML.TAG_ATTRIBUTE_VALUE);
        addTextAttribute(IStyleConstantsJSP.JSP_CONTENT);
        
        for (String key : RubyColorConstants.ALL)
            addJavaTextAttribute(key);
        
        fScanner.initialize();
    }
    
    protected void handlePropertyChange(PropertyChangeEvent event) {
        String styleKey = null;
        String javaStyleKey = null;
        
        if (event != null) {
            String prefKey = event.getProperty();
            // check if preference changed is a style preference
            if (IStyleConstantsHTML.SCRIPT_AREA_BORDER.equals(prefKey))
                styleKey = IStyleConstantsHTML.SCRIPT_AREA_BORDER;
            else if (IStyleConstantsXML.TAG_ATTRIBUTE_NAME.equals(prefKey))
                styleKey = IStyleConstantsXML.TAG_ATTRIBUTE_NAME;
            else if (IStyleConstantsXML.TAG_ATTRIBUTE_VALUE.equals(prefKey))
                styleKey = IStyleConstantsXML.TAG_ATTRIBUTE_VALUE;
            else if (IStyleConstantsJSP.JSP_CONTENT.equals(prefKey))
                styleKey = IStyleConstantsJSP.JSP_CONTENT;
        }
        
        if (styleKey != null) {
            // overwrite style preference with new value
            addTextAttribute(styleKey);
        }
        if (javaStyleKey != null) {
            // overwrite style preference with new value
            addJavaTextAttribute(javaStyleKey);
            fScanner.initialize();
        }
        if (styleKey != null || javaStyleKey != null) {
            // force a full update of the text viewer
            fHighlighter.refreshDisplay();
        }
    }
    
    @SuppressWarnings("unchecked")
    public boolean prepareRegions(ITypedRegion typedRegion, int ssssrequestedStart, int ssssrequestedLength,
            Collection rawHoldResults) {
        boolean result = true;
        Collection<StyleRange> holdResults = rawHoldResults;
        try {
            // ideally, eventually, we'll have a "virtualDocument" we can 
            // refer to, but for now ... we'll simple rescan the one region.
            // use simple adjustment (since "sub-content" starts at 0
            int offsetAdjustment = typedRegion.getOffset();
            String content = fDocument.get(typedRegion.getOffset(), typedRegion.getLength());
            IDocument document = new Document(content);
            
            int lastStart = 0;
            int length = 0;
            IToken lastToken = Token.UNDEFINED;
            
            int remainingLength = typedRegion.getLength();
            fScanner.setRange(document, lastStart, remainingLength);
            
            while (true) {
                IToken token = fScanner.nextToken();
                
                if (token.isEOF()) {
                    if (!lastToken.isUndefined() && length != 0)
                        addRange(holdResults, lastStart + offsetAdjustment, length,
                                getTokenTextAttribute(lastToken));
                    break;
                }
                
                if (token.isWhitespace()) {
                    length += fScanner.getTokenLength();
                    continue;
                }
                
                if (lastToken.isUndefined()) {
                    lastToken = token;
                    length += fScanner.getTokenLength();
                    continue;
                }
                
                if (token != lastToken) {
                    addRange(holdResults, lastStart + offsetAdjustment, length,
                            getTokenTextAttribute(lastToken));
                    lastToken = token;
                    lastStart = fScanner.getTokenOffset();
                    length = fScanner.getTokenLength();
                    continue;
                }
                
                length += fScanner.getTokenLength();
            }
        } catch (BadLocationException e) {
            // shouldn't happen, but we don't want it to stop other highlighting, if it does.
            result = false;
        }
        return result;
    }
    
    private void registerPreferenceListener() {
        getColorPreferences().addPropertyChangeListener(fPreferenceListener);
        getJavaColorPreferences().addPropertyChangeListener(fPreferenceListener);
    }
    
    public void release() {
        unregisterPreferenceManager();
        if (fTextAttributes != null) {
            fTextAttributes.clear();
            fTextAttributes = null;
        }
        fIsInitialized = false;
    }
    
    private void unregisterPreferenceManager() {
        getColorPreferences().removePropertyChangeListener(fPreferenceListener);
        getJavaColorPreferences().removePropertyChangeListener(fPreferenceListener);
    }
    
    private IPreferenceStore getColorPreferences() {
        return RubyUI.getDefault().getPreferenceStore();
    }
    
    private IPreferenceStore getJavaColorPreferences() {
        return RubyUI.getDefault().getPreferenceStore();
    }
}
