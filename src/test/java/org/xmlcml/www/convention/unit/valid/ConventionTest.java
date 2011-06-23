package org.xmlcml.www.convention.unit.valid;

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
        private String root = "convention/unit/valid/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
    public void testExampleUnitList() {
        String location = root+"example-unit-list.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
    }

    @Test
    public void testValidUnitListWithInfo() {
        String location = root+"valid-unit-list-with-info.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        CmlLiteValidator.print(report.getReport(), System.out);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
    }


}
