package com.bazaarvoice.jackson.rison;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class ORisonTest {

    private static final ObjectMapper JSON = new ObjectMapper(new JsonFactory());
    private static final ObjectMapper O_RISON = new ObjectMapper(new RisonFactory().
            configure(RisonGenerator.Feature.O_RISON, true).
            configure(RisonParser.Feature.O_RISON, true));

    @DataProvider
    public Object[][] objectRisonData() {
        return new Object[][] {
                // rison, json
                {"", "{}"},
                {"a:0", "{\"a\":0}"},
                {"a:0,b:1", "{\"a\":0,\"b\":1}"},
                {"a:0,b:foo,c:'23skidoo'", "{\"a\":0,\"b\":\"foo\",\"c\":\"23skidoo\"}"},
                {"id:!n,type:/common/document", "{\"id\":null,\"type\":\"/common/document\"}"},
        };
    }


    @DataProvider
    public Object[][] arrayRisonData() {
        return new Object[][] {
                // rison, json
                {"", "[]"},
                {"!t,!f,!n,''", "[true,false,null,\"\"]"},
        };
    }

    @Test(dataProvider="objectRisonData")
    public void testObjectRison(String orison, String json) throws IOException {
        Object jsonObject = JSON.readValue(json, Object.class);

        assertEquals(O_RISON.writeValueAsString(jsonObject), orison);
        assertEquals(O_RISON.readValue(orison, Object.class), jsonObject);
    }
}
