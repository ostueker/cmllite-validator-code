package org.xmlcml.www.convention.unit.warning;

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
        private String root = "convention/unit/warning/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
    public void testNamespaceOfUnitListShouldEndWithSlashOrHash() {
        String location = root+"namespace-of-unitList-should-end-with-slash-or-hash.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be warning\n" + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testUnitSymbolShouldOnlyContainAscii() {
        String location = root + "unit-symbol-should-only-contain-ascii.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testUnitTitleShouldOnlyContainAscii() {
        String location = root + "unit-title-should-only-contain-ascii.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testUnitListShouldHaveTitleAttributeAttribute() {
        String location = root + "unitList-should-have-title-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }


    @Test
    public void testUnitListShouldHaveDescription() {
        String location = root + "unitList-should-have-description.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testUnitListTitleShouldOnlyContainAscii() {
        String location = root + "unitList-title-should-only-contain-ascii.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

}

