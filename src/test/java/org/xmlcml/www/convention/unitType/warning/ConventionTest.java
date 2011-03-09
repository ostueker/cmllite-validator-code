package org.xmlcml.www.convention.unitType.warning;

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
        private String root = "convention/unitType/warning/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }
    @Test
      public void testDimensionNameShouldContainOnlyAscii() {
          String location = root + "dimension-name-should-contain-only-ascii.cml";
          Document input = testUtils.getFileAsDocument(location);
          report = conventionValidator.validate(input);
          log.info(report.getReport().toXML());
          assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
      }

    @Test
    public void testNamespaceOfUnitTypeListShouldEndWithSlashOrHash() {
        String location = root+"namespace-of-unitTypeList-should-end-with-slash-or-hash.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be warning\n" + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testUnitTypeNameShouldContainOnlyAscii() {
        String location = root + "unitType-name-should-contain-only-ascii.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testUnitTypeTitleShouldContainOnlyAscii() {
        String location = root + "unitType-title-should-contain-only-ascii.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testUnitTypeListShouldHaveDescription() {
        String location = root + "unitTypeList-should-have-description.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testUnitTypeListShouldHaveTitleAttribute() {
        String location = root + "unitTypeList-should-have-title-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testUnitTypeListTitleShouldContainOnlyAscii() {
        String location = root + "unitTypeList-title-should-contain-only-ascii.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testValueOfPreserveShouldBeTrueOrFalse1() {
        String location = root + "value-of-preserve-should-be-true-or-false-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testValueOfPreserveShouldBeTrueOrFalse2() {
        String location = root + "value-of-preserve-should-be-true-or-false-2.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testValueOfPreserveShouldBeTrueOrFalse3() {
        String location = root + "value-of-preserve-should-be-true-or-false-3.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

}

