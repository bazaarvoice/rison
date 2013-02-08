package com.bazaarvoice.jackson.rison;

import com.fasterxml.jackson.core.JsonGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.StringWriter;

public class RootSeparatorTest {

    @Test
    public void testDefault() throws IOException {
        RisonFactory factory = new RisonFactory();
        String string = writeObjects(factory, "hello", "world");
        Assert.assertEquals(string, "hello world");
    }

    @Test
    public void testNewline() throws IOException {
        RisonFactory factory = new RisonFactory();
        factory.setRootValueSeparator("\n");
        String string = writeObjects(factory, "hello", "world");
        Assert.assertEquals(string, "hello\nworld");
    }

    @Test
    public void testNone() throws IOException {
        RisonFactory factory = new RisonFactory();
        factory.setRootValueSeparator(null);
        String string = writeObjects(factory, "hello", "world");
        Assert.assertEquals(string, "helloworld");
    }

    @Test
    public void testComma() throws IOException {
        RisonFactory factory = new RisonFactory();
        factory.setRootValueSeparator(", ");
        String string = writeObjects(factory, "hello", "world");
        Assert.assertEquals(string, "hello, world");
    }

    private String writeObjects(RisonFactory factory, Object... objects) throws IOException {
        StringWriter out = new StringWriter();
        JsonGenerator gen = factory.createGenerator(out);
        for (Object object : objects) {
            gen.writeObject(object);
        }
        gen.close();
        return out.toString();
    }
}
