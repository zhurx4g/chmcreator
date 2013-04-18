package com.googlecode.chmcreator;

import java.util.Stack;

public final class RichStringBuilder {
 
    public static final String LINE_DELIMITER = "<br/>";
 
    private StringBuilder builder;
    private Stack<FontStyle> fontStyleStack;
 
    public RichStringBuilder() {
        builder = new StringBuilder();
        fontStyleStack = new Stack<FontStyle>();
    }
 
    public RichStringBuilder append(String text) {
        builder.append(text);
        return this;
    }
 
    public RichStringBuilder appendLineBreak() {
        builder.append(LINE_DELIMITER);
        return this;
    }
 
    public RichStringBuilder startParagraph() {
        builder.append("<p>");
        return this;
    }
 
    public RichStringBuilder startFontStyle(FontStyle fontStyle) {
        fontStyleStack.push(fontStyle);
        internalStartFontStyle(fontStyle);
        return this;
    }
 
    public RichStringBuilder startFontStyles(FontStyle... fontStyles) {
        for (FontStyle fs : fontStyles) {
            startFontStyle(fs);
        }
        return this;
    }
 
    public RichStringBuilder endFontStyles(int count) {
        for (int i = 0;i < count;i++) {
            endStyle();
        }
        return this;
    }
 
    public RichStringBuilder endStyle() {
        if (fontStyleStack.size() > 0) {
            FontStyle style = fontStyleStack.pop();
            internalEndFontStyle(style);
        }
        return this;
    }
 
    public RichStringBuilder endParagraph() {
        flushStyles();
        builder.append("</p>");
        return this;
    }
 
    public void flushStyles() {
        while (fontStyleStack.size() > 0) {
            endStyle();
        }
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o) return false;
        if (!(o instanceof RichStringBuilder)) return false;
 
        return ((RichStringBuilder) o).builder.equals(builder);
    }
 
    @Override
    public int hashCode() {
        return builder.hashCode();
    }
 
    @Override
    public String toString() {
        return builder.toString();
    }
 
    private void internalStartFontStyle(FontStyle fontStyle) {
        switch (fontStyle) {
        case BOLD:
            builder.append("<b>");
                        break;
                case ITALIC:
                        builder.append("<i>");
                        break;
                case STRIKE_THROUGH:
                        builder.append("<del>");
                        break;
                case UNDERLINE:
                        builder.append("<ins>");
                        break;
                }
        }
 
        private void internalEndFontStyle(FontStyle fontStyle) {
                switch (fontStyle) {
                case BOLD:
                        builder.append("</b>");
            break;
        case ITALIC:
            builder.append("</i>");
            break;
        case STRIKE_THROUGH:
            builder.append("</del>");
            break;
        case UNDERLINE:
            builder.append("</ins>");
            break;
        }
    }
 
}