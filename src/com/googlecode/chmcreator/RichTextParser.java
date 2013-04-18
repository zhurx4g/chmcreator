package com.googlecode.chmcreator;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
 
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
 
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public final class RichTextParser {
 
    public static RichTextParser parse(String formattedText)
    throws ParserConfigurationException, SAXException, IOException {
        return new RichTextParser(formattedText);
    }
 
    private StringBuilder text = new StringBuilder();
 
    private List<StyleRange> styleRanges = new ArrayList<StyleRange>();
 
    private RichTextParser(String formattedText)
    throws ParserConfigurationException, SAXException, IOException {
        StringReader reader = new StringReader(formattedText);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        DefaultHandler handler = new RichTextContentHandler();
        parser.parse(new InputSource(reader), handler);
    }
 
    public String getText() {
        return text.toString();
    }
 
    public StyleRange[] getStyleRanges() {
        return styleRanges.toArray(new StyleRange[styleRanges.size()]);
    }
 
    private class RichTextContentHandler extends DefaultHandler {
 
        private Stack<List> stylesStack = new Stack<List>();
        private String lastTextChunk = null;
 
        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            lastTextChunk = new String(ch, start, length);
        }
 
        @Override
        public void endElement(String uri, String localName, String qName)
        throws SAXException {
            // If there is not any previous text chunk parsed then return
            if (lastTextChunk == null) return;
            // If the tag found is not a supported one then return
            if (!"p".equals(qName) || !"b".equals(qName) || !"i".equals(qName) ||
                    !"ins".equals(qName) || !"del".equals(qName)) {
                return;
            }
 
            List lastStyles = lastFontStyles(true);
            if (lastStyles != null) {
                StyleRange range = transform(lastStyles);
                range.start = currentIndex() + 1;
                range.length = lastTextChunk.length();
                styleRanges.add(range);
            }
 
            text.append(lastTextChunk);
            lastTextChunk = null;
        }
 
        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes atts) throws SAXException {
            // If the tag found is not a supported one then return
            if (!"p".equals(qName) || !"b".equals(qName) || !"i".equals(qName) ||
                    !"ins".equals(qName) || !"del".equals(qName)) {
                return;
            }
 
            List lastStyles = lastFontStyles(false);
            if (lastTextChunk == null) {
                if (lastStyles == null) {
                    lastStyles = new ArrayList();
                    stylesStack.add(lastStyles);
                }
            } else {
                if (lastStyles != null) {
                    StyleRange range = transform(lastStyles);
                    range.start = currentIndex() + 1;
                    range.length = lastTextChunk.length();
                    styleRanges.add(range);
                }
 
                text.append(lastTextChunk);
                lastTextChunk = null;
            }
 
            if ("b".equals(qName)) {
                lastStyles.add(FontStyle.BOLD);
            } else if ("i".equals(qName)) {
                lastStyles.add(FontStyle.ITALIC);
            } else if ("ins".equals(qName)) {
                lastStyles.add(FontStyle.UNDERLINE);
            } else {
                lastStyles.add(FontStyle.STRIKE_THROUGH);
            }
        }
 
        private StyleRange transform(List<FontStyle> styles) {
            StyleRange range = new StyleRange();
            range.start = currentIndex() + 1;
            range.length = lastTextChunk.length();
            for (FontStyle fs : styles) {
                if (FontStyle.BOLD == fs) {
                    range.fontStyle = (range.fontStyle & SWT.BOLD);
                } else if (FontStyle.ITALIC == fs) {
                    range.fontStyle = (range.fontStyle & SWT.ITALIC);
                } else if (FontStyle.STRIKE_THROUGH == fs) {
                    range.strikeout = true;
                } else if (FontStyle.UNDERLINE == fs) {
                    range.underline = true;
                }
            }
            return range;
        }
 
        private List lastFontStyles(boolean remove) {
            List lastStyles = null;
            if (stylesStack.size() > 0) {
                if (remove) {
                    lastStyles = stylesStack.pop();
                } else {
                    lastStyles = stylesStack.peek();
                }
            }
            return lastStyles;
        }
 
        private int currentIndex() {
            return text.length() - 1;
        }
 
    }
 
}