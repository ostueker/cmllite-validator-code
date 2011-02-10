package org.xmlcml.www.convention.dictionary.valid;

import nu.xom.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.www.*;

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
        private String root = "convention/dictionary/valid/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
    public void testExampleDictionary() {
        String location = root+"example-dictionary.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        CmlLiteValidator.print(report.getReport(), System.out);
        assertEquals(location + " should be valid", ValidationResult.VALID, report.getValidationResult());
    }

    @Test
    public void testDictionaryPresent1() {
        String location = root+"dictionary-present-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
    }

}
