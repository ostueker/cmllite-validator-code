package org.xmlcml.www.convention.simpleUnit.invalid;

import nu.xom.Document;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.www.ConventionValidator;
import org.xmlcml.www.TestUtils;
import org.xmlcml.www.ValidationReport;
import org.xmlcml.www.ValidationResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
    private String root = "convention/simpleUnit/invalid/";


    private static Logger log = Logger.getLogger(ConventionTest.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
    public void testSimpleUnitMustBeSpecifiedOnUnitList1() {
        String location = root + "simpleUnit-must-be-specified-on-unitList-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid\n" + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        assertFalse(location + " should not contain info\n" + report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testSimpleUnitMustBeSpecifiedOnUnitList2() {
        String location = root + "simpleUnit-must-be-specified-on-unitList-2.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid\n" + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        assertFalse(location + " should not contain info\n" + report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testUnitListMustHaveUnitChild1() {
        String location = root + "unitList-must-have-child-unit-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid\n" + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        assertFalse(location + " should not contain info\n" + report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testUnitListMustHaveUnitChild2() {
        String location = root + "unitList-must-have-child-unit-2.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid\n" + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        assertFalse(location + " should not contain info\n" + report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testUnitListMustHaveUnitChild3() {
        String location = root + "unitList-must-have-child-unit-3.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid\n" + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        assertFalse(location + " should not contain info\n" + report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testUnitListMustHaveUnitChild4() {
        String location = root + "unitList-must-have-child-unit-4.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid\n" + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        assertFalse(location + " should not contain info\n" + report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testUnitListMustHaveUnitChild5() {
        String location = root + "unitList-must-have-child-unit-5.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid\n" + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        assertFalse(location + " should not contain info\n" + report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testUnitListMustHaveUnitChild6() {
        String location = root + "unitList-must-have-child-unit-6.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid\n" + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        assertFalse(location + " should not contain info\n" + report.getReport().toXML(), report.containsInfo());
    }

}

