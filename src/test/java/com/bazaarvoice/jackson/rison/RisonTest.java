package com.bazaarvoice.jackson.rison;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class RisonTest {

    private static final ObjectMapper JSON = new ObjectMapper(new JsonFactory());
    private static final ObjectMapper RISON = new ObjectMapper(new RisonFactory());

    @DataProvider
    public Object[][] risonData() {
        return new Object[][] {
                // rison, json
                {"(a:0,b:1)", "{\"a\":0,\"b\":1}"},
                {"(a:0,b:foo,c:'23skidoo')", "{\"a\":0,\"b\":\"foo\",\"c\":\"23skidoo\"}"},
                {"!t", "true"},
                {"!f", "false"},
                {"!n", "null"},
                {"''", "\"\""},
                {"0", "0"},
                {"1.5", "1.5"},
                {"-3", "-3"},
                {"1.0e30", "1.0e+30"},
                {"1.0e-30", "1.0e-30"},
                {"a", "\"a\""},
                {"'0a'", "\"0a\""},
                {"'abc def'", "\"abc def\""},
                {"()", "{}"},
                {"(a:0)", "{\"a\":0}"},
                {"(id:!n,type:/common/document)", "{\"id\":null,\"type\":\"/common/document\"}"},
                {"!()", "[]"},
                {"!(!t,!f,!n,'')", "[true,false,null,\"\"]"},
                {"'-h'", "\"-h\""},
                {"a-z", "\"a-z\""},
                {"'wow!!'", "\"wow!\""},
                {"domain.com", "\"domain.com\""},
                {"'user@domain.com'", "\"user@domain.com\""},
                {"'US $10'", "\"US $10\""},
                {"'can!'t'", "\"can't\""},
                {"'Control-F: \u0006'", "\"Control-F: \\u0006\""},
                {"'Unicode: \u0BEB'", "\"Unicode: \\u0BEB\""},
                {"(a:!((b:c,'3':!())))", "{\"a\":[{\"b\":\"c\",\"3\":[]}]}"},
                {"(a:'this\thas\nmultiple\nlines\n')", "{\"a\":\"this\\thas\\nmultiple\\nlines\\n\"}"},
        };
    }

    @Test(dataProvider= "risonData")
    public void testRison(String rison, String json) throws IOException {
        Object jsonObject = JSON.readValue(json, Object.class);

        assertEquals(RISON.writeValueAsString(jsonObject), rison);
        assertEquals(RISON.readValue(rison, Object.class), jsonObject);
    }
}
