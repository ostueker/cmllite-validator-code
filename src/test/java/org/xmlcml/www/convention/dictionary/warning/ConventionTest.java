package org.xmlcml.www.convention.dictionary.warning;

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
 */
public class ConventionTest {

    private TestUtils testUtils = new TestUtils();
    private ConventionValidator conventionValidator = new ConventionValidator();
    private ValidationReport report = null;
    private String root = "convention/dictionary/warning/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
    public void testNoDictionaryConventionPresent() {
        String location = root+"no-dictionary-convention-present.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        System.out.println("report: "+report.getReport().toXML());
        assertEquals(location+" should be warning", ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }
}
