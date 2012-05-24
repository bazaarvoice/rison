package com.bazaarvoice.jackson.rison;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import static org.testng.Assert.assertEquals;

public class BinaryMappingTest {

    private static final Random RANDOM = new Random();
    private static final ObjectMapper RISON = new ObjectMapper(new RisonFactory());

    @Test
    public void testBinaryMapping() throws IOException {
        Data expected = new Data(randomBytes(16), randomBytes(9000));

        // test char-based roundtrip
        {
            String rison = RISON.writeValueAsString(expected);
            Data actual = RISON.readValue(rison, Data.class);
            assertEquals(actual, expected);
        }

        // test byte-based roundtrip
        {
            byte[] rison = RISON.writeValueAsBytes(expected);
            Data actual = RISON.readValue(rison, Data.class);
            assertEquals(actual, expected);
        }
    }

    @Test
    public void testBinaryWhitespace() throws IOException {
        String rison = "(buffer1:'DdNS\tqxIF\nBlOv hF4mABm9ZA==')";
        Data expected = new Data(new byte[] {13,-45,82,-85,18,5,6,83,-81,-124,94,38,0,25,-67,100}, null);
        Object actual = RISON.readValue(rison, Data.class);
        assertEquals(actual, expected);
    }

    private byte[] randomBytes(int len) {
        byte[] bytes = new byte[len];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

    public static class Data {

        private final byte[] _buffer1;
        private final byte[] _buffer2;

        public Data(@JsonProperty("buffer1") byte[] buffer1, @JsonProperty("buffer2") byte[] buffer2) {
            _buffer1 = buffer1;
            _buffer2 = buffer2;
        }

        public byte[] getBuffer1() {
            return _buffer1;
        }

        public byte[] getBuffer2() {
            return _buffer2;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Data)) {
                return false;
            }
            Data data = (Data) obj;
            return Arrays.equals(_buffer1, data.getBuffer1()) && Arrays.equals(_buffer2, data.getBuffer2());
        }
    }
}
