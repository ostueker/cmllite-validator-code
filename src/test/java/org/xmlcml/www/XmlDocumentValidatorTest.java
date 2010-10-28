package org.xmlcml.www;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XmlDocumentValidatorTest {

    private XmlDocumentValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new XmlDocumentValidator();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testValidateWithNonXml() {
        boolean isValid = validator.validate(IOUtils.toInputStream("a non xml string"));
        assertFalse("should be invalid", isValid);
    }

    @Test
    public void testValidateWithEmptyStream() {
        boolean isValid = validator.validate(IOUtils.toInputStream(""));
        assertFalse("should be invalid", isValid);
    }

    @Test
    public void testValidateWithNull() {
        boolean isValid = validator.validate(null);
        assertFalse("should be invalid", isValid);
    }

    @Test
    public void testValidateWithXmlDeclarationOnly() {
        boolean isValid = validator.validate(IOUtils.toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertFalse("should be invalid", isValid);
    }

    @Test
    public void testValidateWithBasicXml() {
        boolean isValid = validator.validate(IOUtils.toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xml />"));
        assertTrue("should be valid", isValid);
    }

    @Test
    public void testValidateWithBadlyFormedXml1() {
        boolean isValid = validator.validate(IOUtils.toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xml>"));
        assertFalse("should be invalid", isValid);
    }

    @Test
    public void testValidateWithBadlyFormedXml2() {
        boolean isValid = validator.validate(IOUtils.toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xml>" +
                "<a>" +
                "</xml>"));
        assertFalse("should be invalid", isValid);
    }

    @Test
    public void testValidateWithNamespacedXml() {
        boolean isValid = validator.validate(IOUtils.toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xml xmlns=\"http://www.example.com\">" +
                "</xml>"));
        assertTrue("should be valid", isValid);
    }

    @Test
    public void testValidateWithPrefixedNamespacedXml() {
        boolean isValid = validator.validate(IOUtils.toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<x:xml xmlns:x=\"http://www.example.com\">" +
                "</x:xml>"));
        assertTrue("should be valid", isValid);
    }

    @Test
    public void testValidateWithMixedNamespacedXml() {
        boolean isValid = validator.validate(IOUtils.toInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<x:xml xmlns:x=\"http://www.example.com\" xmlns:o=\"http://www.other.com\">" +
                "<o:a />" +
                "</x:xml>"));
        assertTrue("should be valid", isValid);
    }
}
