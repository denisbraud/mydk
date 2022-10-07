package io.mydk.util;

import java.util.Map;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Operations on {@link java.lang.String}.<br>
 * @see org.apache.commons.lang3.StringUtils
 */
public final class StringUtil {
    /**
     * Suppresses default constructor, ensuring non-instantiability.
     */
    private StringUtil() {
        super();
    }
    /**
     * Returns the code of string.<br>
     * The return string can be used :<br>
     * <ul>
     * <li>as a code (eg. to generate file name)</li>
     * <li>to compare 2 strings, case and accents insensitive</li>
     * </ul>
     * Non ascii-128 chars are translated to ascii-128 equivalent.<br>
     * Non alphanumeric chars are removed.
     * @param s string
     * @return code
     */
    public static String toCode(String s) {
        return toCode(s, null);
    }
    /**
     * Returns the code of string.<br>
     * The return string can be used :<br>
     * <ul>
     * <li>as a code (eg. to generate file name)</li>
     * <li>to compare 2 strings, case and accents insensitive</li>
     * </ul>
     * Non ascii-128 chars are translated to ascii-128 equivalent.<br>
     * Non alphanumeric chars are removed (except mentionned by keepChars).<br>
     * <br>
     * see :<br>
     * http://www.unicode.org/Public/MAPPINGS/VENDORS/MICSFT/WINDOWS/CP1252.TXT
     * <br>
     * http://www.unicode.org/Public/MAPPINGS/ISO8859/8859-1.TXT<br>
     * @param s string
     * @param keepChars chars to keep
     * @return code
     */
    public static String toCode(String s, String keepChars) {
        if (s == null) {
            return null;
        }
        String code = StringUtils.stripAccents(s).toLowerCase();
        int len = code.length();
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = code.charAt(i);
            if ((keepChars != null && keepChars.indexOf(c) != -1)
                || CharUtils.isAsciiAlphanumeric(c)) {
                buf.append(c);
            }
        }
        return buf.toString();
    }
    /**
     * Returns null if a String is whitespace, empty (""), null ("null") or
     * null.
     * @param str the String to check, may be null
     * @return return string
     * @see StringUtils#stripToNull(java.lang.String)
     */
    public static String stripAndNullToNull(String str) {
        String s = StringUtils.stripToNull(str);
        if (s == null || "null".equals(s)) {
            return null;
        }
        return s;
    }

    /**
     * Replaces all occurrences of a tokens.key with tokens.value.
     * @param str string
     * @param tokens Map
     * @return the text with any replacements processed, <code>null</code> if
     *         null String input
     */
    public static String replace(String str, Map<String, String> tokens) {
        if (str == null || tokens == null) {
            return str;
        }
        String strNew = str;
        for (Map.Entry<String, String> entry : tokens.entrySet()) {
            strNew =
                StringUtils.replace(strNew, entry.getKey(), entry.getValue());
        }
        return strNew;
    }

    /**
     * <p>
     * Gets the <b>shortest</b> String that is nested in between two Strings.
     * Only the first match is returned.
     * </p>
     * <p>
     * A <code>null</code> input String returns <code>null</code>. A
     * <code>null</code> open/close returns <code>null</code> (no match). An
     * empty ("") open and close returns an empty string.
     * </p>
     * 
     * <pre>
     * StringUtils.substringBetween("wx[b]yz", "[", "]") = "b"
     * StringUtils.substringBetween(null, *, *)          = null
     * StringUtils.substringBetween(*, null, *)          = null
     * StringUtils.substringBetween(*, *, null)          = null
     * StringUtils.substringBetween("", "", "")          = ""
     * StringUtils.substringBetween("", "", "]")         = null
     * StringUtils.substringBetween("", "[", "]")        = null
     * StringUtils.substringBetween("yabcz", "", "")     = ""
     * StringUtils.substringBetween("yabcz", "y", "z")   = "abc"
     * StringUtils.substringBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     * 
     * @param str the String containing the substring, may be null
     * @param open the String before the substring, may be null
     * @param close the String after the substring, may be null
     * @return the substring, <code>null</code> if no match
     * @see StringUtils#substringBetween(String, String, String)
     */
    public static String
        substringIgnoreCaseBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        String strNew = str;
        int posStart;
        int openLen = open.length();
        String openLC = open;
        if (openLen > 0) {
            openLC = openLC.toLowerCase();
            posStart = strNew.toLowerCase().indexOf(openLC);
            if (posStart == -1) {
                return null;
            }
            strNew = strNew.substring(posStart + openLen);
        }

        int posEnd = strNew.length();
        int closeLen = close.length();
        String closeLC = close;
        if (closeLen > 0) {
            closeLC = closeLC.toLowerCase();
            posEnd = strNew.toLowerCase().indexOf(closeLC);
            if (posEnd == -1) {
                return null;
            }
        }
        if (openLen > 0 && !openLC.equals(closeLC)) {
            posStart = strNew.toLowerCase().indexOf(openLC);
            while (posStart != -1 && posStart < posEnd) {
                int offset = posStart + openLen;
                int newPosEnd = posEnd - offset;
                if (newPosEnd <= 0) {
                    if (closeLen == 0) {
                        return StringUtils.substring(strNew, 0, posEnd);
                    }
                    newPosEnd =
                        strNew.toLowerCase().indexOf(closeLC, offset) - offset;
                    if (newPosEnd == -1) {
                        return StringUtils.substring(strNew, 0, posEnd);
                    }
                }
                strNew = strNew.substring(offset);
                posEnd = newPosEnd;
                posStart = strNew.toLowerCase().indexOf(openLC);
            }
        }
        return StringUtils.substring(strNew, 0, posEnd);
    }
}
