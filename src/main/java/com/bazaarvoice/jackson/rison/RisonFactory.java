/*
Portions of this file are copyright by the authors of Jackson under the Apache 2.0 or LGPL license.

The implementation was derived from the Jackson class 'org.codehaus.jackson.impl.WriterBasedGenerator'
and modified for Rison.
*/

package com.bazaarvoice.jackson.rison;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.format.InputAccessor;
import org.codehaus.jackson.format.MatchStrength;
import org.codehaus.jackson.io.IOContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * See the <a href="http://mjtemplate.org/examples/rison.html">Rison spec</a>.
 */
public class RisonFactory extends JsonFactory {

    public final static String FORMAT_NAME_RISON = "Rison";

    final static int DEFAULT_RISON_PARSER_FEATURE_FLAGS = RisonParser.Feature.collectDefaults();

    final static int DEFAULT_RISON_GENERATOR_FEATURE_FLAGS = RisonGenerator.Feature.collectDefaults();

    protected int _risonParserFeatures = DEFAULT_RISON_PARSER_FEATURE_FLAGS;

    protected int _risonGeneratorFeatures = DEFAULT_RISON_GENERATOR_FEATURE_FLAGS;

    public RisonFactory() {
        this(null);
    }

    public RisonFactory(ObjectCodec codec) {
        super(codec);
    }

    @Override
    public String getFormatName() {
        return FORMAT_NAME_RISON;
    }

    @Override
    public MatchStrength hasFormat(InputAccessor acc) throws IOException {
        return MatchStrength.SOLID_MATCH;  // format detection isn't supported
    }

    //
    // Parser configuration
    //

    public final RisonFactory configure(RisonParser.Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public RisonFactory enable(RisonParser.Feature f) {
        _risonParserFeatures |= f.getMask();
        return this;
    }

    public RisonFactory disable(RisonParser.Feature f) {
        _risonParserFeatures &= ~f.getMask();
        return this;
    }

    public boolean isEnabled(RisonParser.Feature f) {
        return (_risonParserFeatures & f.getMask()) != 0;
    }

    //
    // Generator configuration
    //

    public RisonFactory configure(RisonGenerator.Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }

    public RisonFactory enable(RisonGenerator.Feature f) {
        _risonGeneratorFeatures |= f.getMask();
        return this;
    }

    public RisonFactory disable(RisonGenerator.Feature f) {
        _risonGeneratorFeatures &= ~f.getMask();
        return this;
    }

    public final boolean isEnabled(RisonGenerator.Feature f) {
        return (_risonGeneratorFeatures & f.getMask()) != 0;
    }

    //
    // Internal factory methods
    //

    @Override
    protected RisonParser _createJsonParser(Reader r, IOContext ctxt) throws IOException, JsonParseException {
        return new RisonParser(ctxt, _parserFeatures, _risonParserFeatures, r, _objectCodec,
                _rootCharSymbols.makeChild(isEnabled(JsonParser.Feature.CANONICALIZE_FIELD_NAMES),
                        isEnabled(JsonParser.Feature.INTERN_FIELD_NAMES)));
    }

    @Override
    protected RisonParser _createJsonParser(InputStream in, IOContext ctxt) throws IOException, JsonParseException {
        return _createJsonParser(new InputStreamReader(in, "UTF-8"), ctxt);
    }

    @Override
    protected RisonParser _createJsonParser(byte[] data, int offset, int len, IOContext ctxt) throws IOException, JsonParseException {
        return _createJsonParser(new ByteArrayInputStream(data, offset, len), ctxt);
    }

    @Override
    protected RisonGenerator _createJsonGenerator(Writer out, IOContext ctxt) throws IOException {
        return new RisonGenerator(ctxt, _generatorFeatures, _risonGeneratorFeatures, _objectCodec, out);
    }

    @Override
    protected RisonGenerator _createUTF8JsonGenerator(OutputStream out, IOContext ctxt) throws IOException {
        return _createJsonGenerator(_createWriter(out, JsonEncoding.UTF8, ctxt), ctxt);
    }

    @Override
    protected Writer _createWriter(OutputStream out, JsonEncoding enc, IOContext ctxt) throws IOException {
        return new OutputStreamWriter(out, enc.getJavaName());
    }
}
