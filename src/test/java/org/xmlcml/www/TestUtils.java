package org.xmlcml.www;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: jat45
 * Date: 22/12/10
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
public class TestUtils {

    public Builder builder = new Builder();

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void dummyTest() {
        assertTrue("dummy test so class builds", true);
    }

     public String getFileAsString(String location) {
        InputStream stream = getClass().getResourceAsStream(location);
        String input = "";
        if (stream != null) {
            try {
                input = IOUtils.toString(stream);
            } catch (IOException e) {
                throw new RuntimeException("should be able to convert from stream to string: "+location);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        } else {
            throw new RuntimeException("couldn't read from: "+location);
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
                throw new RuntimeException("should be able to construct document from: "+location,e);
            } catch (IOException e) {
                throw new RuntimeException("should be able to construct document from: "+location,e);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        } else {
            throw new RuntimeException("couldn't read from: "+location);
        }
        return document;
    }

    public Document getFileAsDocument(File file) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
                throw new RuntimeException("should be able to construct document from: "+file.getAbsolutePath(),e);
        }
        Document document = null;
        if (stream != null) {
            try {
                document = builder.build(stream);
            } catch (ParsingException e) {
                throw new RuntimeException("should be able to construct document from: "+file.getAbsolutePath(), e);
            } catch (IOException e) {
                throw new RuntimeException("should be able to construct document from: "+file.getAbsolutePath(),e);
            } finally {
            IOUtils.closeQuietly(stream);
        }

        } else {
            throw new RuntimeException("couldn't read from: "+file.getAbsolutePath());
        }
        return document;
    }
}
