package com.bazaarvoice.jackson.rison;

/**
 * Utilities for dealing with Rison unquoted identifiers.
 */
class RisonUtils {

    // char sets match not_idchar, not_idstart from http://mjtemplate.org/dist/mjt-0.9.2/rison.js
    // except that this version considers all whitespace (ascii 0-32) as not part of an ID
    // this does not match the Rison specification precisely.
    private static final int _notIdStart = toCharSet("'!:(),*@$-0123456789");
    private static final int _notIdChar  = toCharSet("'!:(),*@$");

    /**
     * Returns true if the specified character is a legal first character in an unquoted identifier.
     */
    public static boolean isIdStart(int ch) {
        return !inCharSet(ch, _notIdStart);
    }

    /**
     * Returns true if the specified character is a legal following character in an unquoted identifier.
     */
    public static boolean isIdChar(int ch) {
        return !inCharSet(ch, _notIdChar);
    }

    /**
     * Returns true if a string does not need to be quoted when serialized.
     */
    public static boolean isId(String string) {
        int len = string.length();
        if (len == 0) {
            return false;
        }
        if (!isIdStart(string.charAt(0))) {
            return false;
        }
        for (int i = 1; i < len; i++) {
            if (!isIdChar(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if a string does not need to be quoted when serialized.
     */
    public static boolean isId(char[] chars, int offset, int len) {
        if (len == 0) {
            return false;
        }
        int end = offset + len;
        if (!isIdStart(chars[offset++])) {
            return false;
        }
        while (offset < end) {
            if (!isIdChar(chars[offset++])) {
                return false;
            }
        }
        return true;
    }

    private static boolean inCharSet(int ch, int bits) {
        return ch >= 0 && (ch <= ' ' || (ch <= (' ' + 32) && (bits & (1 << (ch - (' ' + 1)))) != 0));
    }

    /**
     * Encodes the characters in the string in a 32-bit integer, where character 33 is at
     * bit 0 of character 64 is at bit 31.  This only works because the characters we're
     * interested in all happen to fall within the ASCII range 33-64.  But it allows fast
     * testing for membership in the set since the membership test is just bit operations.
     */
    private static int toCharSet(String string) {
        int bits = 0;
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (ch <= ' ') {
                // whitespace chars (everything < 32) not supported by this method (they're handled by special case)
                throw new AssertionError();
            }
            int pos = ch - (' ' + 1);
            // assume special characters are all w/in 32 bits of ' '.  if that changes, we'll have
            // to use a different representation for the bitset such as a long or long[].
            if (pos >= 32) {
                throw new AssertionError();
            }
            bits |= 1 << pos;

        }
        return bits;
    }
}
