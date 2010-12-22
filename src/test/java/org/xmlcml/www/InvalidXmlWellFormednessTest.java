package org.xmlcml.www;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: jat45
 * Date: 22/12/10
 * Time: 10:12
 */
public class InvalidXmlWellFormednessTest {

    TestUtils testUtils = new TestUtils();
    XmlWellFormednessValidator xmlWellFormednessValidator = new XmlWellFormednessValidator();
    ValidationReport report = null;
     @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
    public void testValidXml4() {
        String location = "xml/valid/wellformed-xml-4.cml";
        String input = testUtils.getFileAsString(location);
        report = xmlWellFormednessValidator.validate(input);
        assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
    }

    @Test
    public void testInvalidXml1() {
        String location = "xml/invalid/non-xml-1.cml";
        String input = testUtils.getFileAsString(location);
        report = xmlWellFormednessValidator.validate(input);
        assertEquals(location+" should be invalid", ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testInvalidXml2() {
        String location = "xml/invalid/non-xml-2.cml";
        String input = testUtils.getFileAsString(location);
        report = xmlWellFormednessValidator.validate(input);
        assertEquals(location+" should be invalid", ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testInvalidXml3() {
        String location = "xml/invalid/non-xml-3.cml";
        String input = testUtils.getFileAsString(location);
        report = xmlWellFormednessValidator.validate(input);
        assertEquals(location+" should be invalid", ValidationResult.INVALID, report.getValidationResult());
    }

}
