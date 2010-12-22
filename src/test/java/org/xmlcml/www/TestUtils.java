package org.xmlcml.www;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: jat45
 * Date: 22/12/10
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
public class TestUtils {

    public Builder builder = new Builder();
     public String getFileAsString(String location) {
        InputStream stream = getClass().getResourceAsStream(location);
        String input = "";
        if (stream != null) {
            try {
                input = IOUtils.toString(stream);
            } catch (IOException e) {
                fail("should be able to convert from stream to string: "+location);
            }
        } else {
            fail("couldn't read from: "+location);
        }
        return input;
    }

    public Document getFileAsDocument(String location) {
        InputStream stream = getClass().getResourceAsStream(location);
        Document document = null;
        if (stream != null) {
            try {
                document = builder.build(stream);
            } catch (ParsingException e) {
                fail("should be able to construct document from: "+location);
            } catch (IOException e) {
                fail("should be able to construct document from: "+location);
            }

        } else {
            fail("couldn't read from: "+location);
        }
        return document;
    }

}
