package org.xmlcml.www.convention.unit.invalid;

import nu.xom.Document;
import org.apache.log4j.Logger;
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

    private static Logger log = Logger.getLogger(ConventionTest.class);

    private TestUtils testUtils = new TestUtils();
    private ConventionValidator conventionValidator = new ConventionValidator();
    private ValidationReport report = null;
    private String root = "convention/unit/invalid/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }


    @Test
    public void testUnitConventionCanOnlyBeDeclaredOnUnitList() {
        String location = root + "unit-convention-can-only-be-declared-on-unitList.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitListMustContainUnitChildren() {
        String location = root + "unitList-must-contain-unit-children.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitListMustDeclareNamespaceAttribute() {
        String location = root + "unitList-must-declare-namespace-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

}

