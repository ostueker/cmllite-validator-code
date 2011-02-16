package org.xmlcml.www.xml.valid;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.www.TestUtils;
import org.xmlcml.www.ValidationReport;
import org.xmlcml.www.ValidationResult;
import org.xmlcml.www.XmlWellFormednessValidator;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: jat45
 * Date: 22/12/10
 * Time: 10:12
 */
public class XmlWellFormednessTest {

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
    public void testValidXml1() {
        String location = "xml/valid/wellformed-xml-1.cml";
        String input = testUtils.getFileAsString(location);
        report = xmlWellFormednessValidator.validate(input);
        Assert.assertEquals(location + " should be valid "+report.getReport().toXML(), ValidationResult.VALID, report.getValidationResult());
    }
    @Test
    public void testValidXml2() {
        String location = "xml/valid/wellformed-xml-2.cml";
        String input = testUtils.getFileAsString(location);
        report = xmlWellFormednessValidator.validate(input);
        assertEquals(location+" should be valid "+report.getReport().toXML(), ValidationResult.VALID, report.getValidationResult());
    }
    @Test
    public void testValidXml3() {
        String location = "xml/valid/wellformed-xml-3.cml";
        String input = testUtils.getFileAsString(location);
        report = xmlWellFormednessValidator.validate(input);
        assertEquals(location+" should be valid "+report.getReport().toXML(), ValidationResult.VALID, report.getValidationResult());
    }
    @Test
    public void testValidXml4() {
        String location = "xml/valid/wellformed-xml-4.cml";
        String input = testUtils.getFileAsString(location);
        report = xmlWellFormednessValidator.validate(input);
        assertEquals(location+" should be valid "+report.getReport().toXML(), ValidationResult.VALID, report.getValidationResult());
    }

    //
    // Test a document which has greek utf8 characters as part of hte element names - taken out as failing to run
    // under maven though ok through intelliJ
//    @Test
//    public void testValidXml5() {
//        String location = "xml/valid/wellformed-xml-5.cml";
//        String input = testUtils.getFileAsString(location);
//        report = xmlWellFormednessValidator.validate(input);
//        assertEquals(location+" should be valid "+report.getReport().toXML(), ValidationResult.VALID, report.getValidationResult());
//    }
}
