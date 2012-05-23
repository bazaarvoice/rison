package com.bazaarvoice.jackson.rison;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class ARisonTest {

    private static final ObjectMapper JSON = new ObjectMapper(new JsonFactory());
    private static final ObjectMapper A_RISON = new ObjectMapper(new RisonFactory().
            configure(RisonGenerator.Feature.A_RISON, true).
            configure(RisonParser.Feature.A_RISON, true));

    @DataProvider
    public Object[][] arrayRisonData() {
        return new Object[][] {
                // rison, json
                {"", "[]"},
                {"!t,!f,!n,''", "[true,false,null,\"\"]"},
        };
    }

    @Test(dataProvider="arrayRisonData")
    public void testArrayRison(String arison, String json) throws IOException {
        Object jsonObject = JSON.readValue(json, Object.class);

        assertEquals(A_RISON.writeValueAsString(jsonObject), arison);
        assertEquals(A_RISON.readValue(arison, Object.class), jsonObject);
    }
}
