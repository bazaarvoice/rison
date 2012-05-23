package com.bazaarvoice.jackson.rison;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class IdentifierTest {

    @Test
    public void testIdStart() {
        String notIdStart = "'!:(),*@$-0123456789";  // plus whitespace <= ' '
        for (char ch = Character.MAX_VALUE; ch < Character.MAX_VALUE; ch++) {
            boolean expected = (ch < 0 || ch > ' ') && notIdStart.indexOf(ch) == -1;
            doTest(Character.toString(ch), expected);
        }
    }

    @Test
    public void testIdChar() {
        String notIdChar = "'!:(),*@$";  // plus whitespace <= ' '
        for (char ch = Character.MAX_VALUE; ch < Character.MAX_VALUE; ch++) {
            boolean expected = (ch < 0 || ch > ' ') && notIdChar.indexOf(ch) == -1;
            doTest(new String(new char[]{'a', ch}), expected);
        }
    }

    @Test
    public void testId() {
        doTest("", false);
        doTest("id", true);
        doTest("i0123456789-", true);
        doTest("0iii", false);
        doTest(" ", false);
        doTest(" id", false);
        doTest("id ", false);
        doTest("\n", false);
        doTest("\u1234", true);
    }

    private void doTest(String string, boolean expected) {
        // test basic string function
        assertEquals(RisonUtils.isId(string), expected);

        // test char[] function
        char[] chars = string.toCharArray();
        assertEquals(RisonUtils.isId(chars, 0, chars.length), expected);

        // test for off-by-one errors in the char[] logic
        char[] chars2 = new char[chars.length + 2];
        System.arraycopy(chars, 0, chars2, 1, chars.length);
        chars2[0] = chars2[chars2.length - 1] = ' ';
        assertEquals(RisonUtils.isId(chars2, 1, chars2.length - 2), expected);
        chars2[0] = chars2[chars2.length - 1] = 'a';
        assertEquals(RisonUtils.isId(chars2, 1, chars2.length - 2), expected);
    }
}
