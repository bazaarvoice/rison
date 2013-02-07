package com.bazaarvoice.jackson.rison;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class MappingTest {

    private static final ObjectMapper RISON = new ObjectMapper(new RisonFactory());

    @Test
    public void testMapping() throws IOException {
        Data expected = new Data("hello world", null, true, false, (byte) -123, 'z', (short) -123, -123, -123L, -123.45f,
                Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, -123.45e30, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);

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

    public static class Data {
        private final String _string;
        private final Object _null;
        private final boolean _true;
        private final boolean _false;
        private final byte _byte;
        private final char _char;
        private final short _short;
        private final int _int;
        private final long _long;
        private final float _float;
        private final float _floatNaN;
        private final float _floatPosInf;
        private final float _floatNegInf;
        private final double _double;
        private final double _doubleNaN;
        private final double _doublePosInf;
        private final double _doubleNegInf;

        public Data(@JsonProperty("string") String string,
                    @JsonProperty("null") Object aNull,
                    @JsonProperty("true") boolean aTrue,
                    @JsonProperty("false") boolean aFalse,
                    @JsonProperty("byte") byte aByte,
                    @JsonProperty("char") char aChar,
                    @JsonProperty("short") short aShort,
                    @JsonProperty("int") int anInt,
                    @JsonProperty("long") long aLong,
                    @JsonProperty("float") float aFloat,
                    @JsonProperty("floatNaN") float floatNaN,
                    @JsonProperty("floatPosInf") float floatPosInf,
                    @JsonProperty("floatNegInf") float floatNegInf,
                    @JsonProperty("double") double aDouble,
                    @JsonProperty("doubleNaN") double doubleNaN,
                    @JsonProperty("doublePosInf") double doublePosInf,
                    @JsonProperty("doubleNegInf") double doubleNegInf) {
            _string = string;
            _null = aNull;
            _true = aTrue;
            _false = aFalse;
            _byte = aByte;
            _char = aChar;
            _short = aShort;
            _int = anInt;
            _long = aLong;
            _float = aFloat;
            _floatNaN = floatNaN;
            _floatPosInf = floatPosInf;
            _floatNegInf = floatNegInf;
            _double = aDouble;
            _doubleNaN = doubleNaN;
            _doublePosInf = doublePosInf;
            _doubleNegInf = doubleNegInf;
        }

        public String getString() {
            return _string;
        }

        public Object getNull() {
            return _null;
        }

        public boolean isTrue() {
            return _true;
        }

        public boolean isFalse() {
            return _false;
        }

        public byte getByte() {
            return _byte;
        }

        public char getChar() {
            return _char;
        }

        public short getShort() {
            return _short;
        }

        public int getInt() {
            return _int;
        }

        public long getLong() {
            return _long;
        }

        public float getFloat() {
            return _float;
        }

        public float getFloatNaN() {
            return _floatNaN;
        }

        public float getFloatPosInf() {
            return _floatPosInf;
        }

        public float getFloatNegInf() {
            return _floatNegInf;
        }

        public double getDouble() {
            return _double;
        }

        public double getDoubleNaN() {
            return _doubleNaN;
        }

        public double getDoublePosInf() {
            return _doublePosInf;
        }

        public double getDoubleNegInf() {
            return _doubleNegInf;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Data)) {
                return false;
            }
            Data data = (Data) o;
            if (_byte != data._byte) {
                return false;
            }
            if (_char != data._char) {
                return false;
            }
            if (Double.compare(data._double, _double) != 0) {
                return false;
            }
            if (Double.compare(data._doubleNaN, _doubleNaN) != 0) {
                return false;
            }
            if (Double.compare(data._doubleNegInf, _doubleNegInf) != 0) {
                return false;
            }
            if (Double.compare(data._doublePosInf, _doublePosInf) != 0) {
                return false;
            }
            if (_false != data._false) {
                return false;
            }
            if (Float.compare(data._float, _float) != 0) {
                return false;
            }
            if (Float.compare(data._floatNaN, _floatNaN) != 0) {
                return false;
            }
            if (Float.compare(data._floatNegInf, _floatNegInf) != 0) {
                return false;
            }
            if (Float.compare(data._floatPosInf, _floatPosInf) != 0) {
                return false;
            }
            if (_int != data._int) {
                return false;
            }
            if (_long != data._long) {
                return false;
            }
            if (_short != data._short) {
                return false;
            }
            if (_true != data._true) {
                return false;
            }
            if (_null != null ? !_null.equals(data._null) : data._null != null) {
                return false;
            }
            if (_string != null ? !_string.equals(data._string) : data._string != null) {
                return false;
            }
            return true;
        }
    }
}
