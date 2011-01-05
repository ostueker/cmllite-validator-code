package org.xmlcml.www.convention.dictionary.invalid;

import nu.xom.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.www.ConventionValidator;
import org.xmlcml.www.TestUtils;
import org.xmlcml.www.ValidationReport;
import org.xmlcml.www.ValidationResult;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: jat45
 * Date: 23/12/10
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class ConventionTest {

    private TestUtils testUtils = new TestUtils();
    private ConventionValidator conventionValidator = new ConventionValidator();
    private ValidationReport report = null;
    private String root = "convention/dictionary/invalid/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
    public void testNoDictionaryElementPresent1() {
        String location = root + "no-dictionary-element-present-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid", ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testNoDictionaryElementPresent2() {
        String location = root + "no-dictionary-element-present-2.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testNoDictionaryElementPresent3() {
        String location = root + "no-dictionary-element-present-3.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testNoDictionaryElementPresent4() {
        String location = root + "no-dictionary-element-present-4.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testNoEntryInDictionary() {
        String location = root + "no-entry-in-dictionary.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid", ValidationResult.INVALID, report.getValidationResult());
    }
}
