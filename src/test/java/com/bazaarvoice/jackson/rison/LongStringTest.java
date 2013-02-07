package com.bazaarvoice.jackson.rison;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import static org.testng.Assert.*;

public class LongStringTest {

    private static final Random RANDOM = new Random();
    private static final ObjectMapper RISON = new ObjectMapper(new RisonFactory());

    @Test
    public void testLongEscapedString() throws IOException {
        // The generator internally uses different code paths for strings <= 2000 bytes.  Test near the boundaries.
        for (int len : new int[]{1999, 2000, 2001, 2002, 123456}) {
            String string = allEscaped(len);
            String actual = RISON.writeValueAsString(string);
            String expected = quote(string);
            assertEquals(actual.length(), expected.length());
            assertEquals(actual, expected);
        }
    }

    @Test
    public void testLongStrings() throws IOException {
        Map<String, Object> expected = new LinkedHashMap<String, Object>();
        expected.put(randomString(12345), randomString(2654321));
        expected.put(randomIdentifier(12345), randomIdentifier(2654321));
        expected.put(allEscaped(12345), allEscaped(2654321));
        expected.put(randomString(2001), new BigInteger(randomBytes(200)));

        String rison = RISON.writeValueAsString(expected);
        Map actual = RISON.readValue(rison, Map.class);

        for (Map.Entry<String, Object> entry : expected.entrySet()) {
            String expectedKey = entry.getKey();
            Object expectedValue = entry.getValue();
            Object actualValue = actual.get(expectedKey);
            assertNotNull(actualValue);
            assertEquals(actualValue.toString().length(), expectedValue.toString().length());
            assertEquals(actualValue, expectedValue);
        }
        assertEquals(actual, expected);
    }

    private String randomString(int len) {
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) RANDOM.nextInt();
        }
        return new String(chars);
    }

    private String randomIdentifier(int len) {
        // most characters are ID start or chars, so chances are these loops don't discard many characters and are fast...
        char[] chars = new char[len];
        int pos = 0;
        while (pos < 1) {
            char ch = (char) RANDOM.nextInt();
            if (IdentifierUtils.isIdStartLenient(ch)) {
                chars[pos++] = ch;
            }
        }
        while (pos < chars.length) {
            char ch = (char) RANDOM.nextInt();
            if (IdentifierUtils.isIdCharLenient(ch)) {
                chars[pos++] = ch;
            }
        }
        return new String(chars);
    }

    private String allEscaped(int len) {
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = RANDOM.nextBoolean() ? '!' : '\'';
        }
        return new String(chars);
    }

    private String quote(String string) {
        StringBuilder buf = new StringBuilder();
        buf.append('\'');
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (ch == '!' || ch == '\'') {
                buf.append('!');
            }
            buf.append(ch);
        }
        buf.append('\'');
        return buf.toString();
    }

    private byte[] randomBytes(int len) {
        byte[] bytes = new byte[len];
        RANDOM.nextBytes(bytes);
        return bytes;
    }
}
