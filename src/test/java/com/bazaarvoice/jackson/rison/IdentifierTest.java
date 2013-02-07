package com.bazaarvoice.jackson.rison;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class IdentifierTest {

    private static final ObjectMapper RISON = new ObjectMapper(new RisonFactory());

    // The sets of "lenient" characters from 0 to 260 were obtained experimentally against rson.js
    private static final String LENIENT_NOT_IDSTART = " !$'()*,:@-0123456789";
    private static final String LENIENT_IDSTART = "" +
            "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f" +
            "\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f" +
            "\"#%&+./;<=>?ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007f" +
            "\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008a\u008b\u008c\u008d\u008e\u008f" +
            "\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009a\u009b\u009c\u009d\u009e\u009f" +
            "\u00a0\u00a1\u00a2\u00a3\u00a4\u00a5\u00a6\u00a7\u00a8\u00a9\u00aa\u00ab\u00ac\u00ad\u00ae\u00af" +
            "\u00b0\u00b1\u00b2\u00b3\u00b4\u00b5\u00b6\u00b7\u00b8\u00b9\u00ba\u00bb\u00bc\u00bd\u00be\u00bf" +
            "\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00c6\u00c7\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf" +
            "\u00d0\u00d1\u00d2\u00d3\u00d4\u00d5\u00d6\u00d7\u00d8\u00d9\u00da\u00db\u00dc\u00dd\u00de\u00df" +
            "\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u00ed\u00ee\u00ef" +
            "\u00f0\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6\u00f7\u00f8\u00f9\u00fa\u00fb\u00fc\u00fd\u00fe\u00ff" +
            "\u0100\u0101\u0102\u0103\u0104";
    private static final String LENIENT_IDCHAR = "" +
            "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f" +
            "\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f" +
            "\"#%&+-./0123456789;<=>?ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007f" +
            "\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008a\u008b\u008c\u008d\u008e\u008f" +
            "\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009a\u009b\u009c\u009d\u009e\u009f" +
            "\u00a0\u00a1\u00a2\u00a3\u00a4\u00a5\u00a6\u00a7\u00a8\u00a9\u00aa\u00ab\u00ac\u00ad\u00ae\u00af" +
            "\u00b0\u00b1\u00b2\u00b3\u00b4\u00b5\u00b6\u00b7\u00b8\u00b9\u00ba\u00bb\u00bc\u00bd\u00be\u00bf" +
            "\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00c6\u00c7\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf" +
            "\u00d0\u00d1\u00d2\u00d3\u00d4\u00d5\u00d6\u00d7\u00d8\u00d9\u00da\u00db\u00dc\u00dd\u00de\u00df" +
            "\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u00ed\u00ee\u00ef" +
            "\u00f0\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6\u00f7\u00f8\u00f9\u00fa\u00fb\u00fc\u00fd\u00fe\u00ff" +
            "\u0100\u0101\u0102\u0103\u0104";

    @Test
    public void testIdChars() {
        for (char ch = Character.MIN_VALUE; ch < Character.MAX_VALUE; ch++) {
            assertEquals(IdentifierUtils.isIdStartLenient(ch), LENIENT_IDSTART.indexOf(ch) != -1 || ch > 255);
            assertEquals(IdentifierUtils.isIdCharLenient(ch), LENIENT_IDCHAR.indexOf(ch) != -1 || ch > 255);
        }
    }

    @Test
    public void testIdParsing() throws IOException {
        // the idstart and idchar strings should be valid strings as-is, without any encoding or escaping or quoting
        assertEquals(RISON.readValue(LENIENT_IDSTART, Object.class), LENIENT_IDSTART);
        assertEquals(RISON.readValue("a" + LENIENT_IDCHAR, Object.class), "a" + LENIENT_IDCHAR);

        // verify that each non-id doesn't parse as a string all by itself.
        for (int i = 0; i < LENIENT_NOT_IDSTART.length(); i++) {
            try {
                Object value = RISON.readValue(LENIENT_NOT_IDSTART.substring(i, i + 1), Object.class);
                assertTrue(value instanceof Integer);  // digits '0'-'9' parse successfully
            } catch (JsonParseException e) {
                // expected
            }
        }
    }

    @Test
    public void testIdEncoding() {
        doTestStrict("", false);
        doTestStrict("id", true);
        doTestStrict("i0123456789-", true);
        doTestStrict("0iii", false);
        doTestStrict(" ", false);
        doTestStrict(" id", false);
        doTestStrict("id ", false);
        doTestStrict("\n", false);
        doTestStrict("\u1234", true);
    }

    private void doTestStrict(String string, boolean expected) {
        // test basic string function
        assertEquals(IdentifierUtils.isIdStrict(string), expected);

        // test char[] function
        char[] chars = string.toCharArray();
        assertEquals(IdentifierUtils.isIdStrict(chars, 0, chars.length), expected);

        // test for off-by-one errors in the char[] logic
        char[] chars2 = new char[chars.length + 2];
        System.arraycopy(chars, 0, chars2, 1, chars.length);
        chars2[0] = chars2[chars2.length - 1] = ' ';
        assertEquals(IdentifierUtils.isIdStrict(chars2, 1, chars2.length - 2), expected);
        chars2[0] = chars2[chars2.length - 1] = 'a';
        assertEquals(IdentifierUtils.isIdStrict(chars2, 1, chars2.length - 2), expected);
    }
}
