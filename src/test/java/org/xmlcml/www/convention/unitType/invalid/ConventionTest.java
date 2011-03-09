package org.xmlcml.www.convention.unitType.invalid;

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
    private String root = "convention/unitType/invalid/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
    public void testDefinitionMustBeChildOfUnitType() {
        String location = root + "definition-must-be-child-of-unitType.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDescriptionMustBeChildOfUnitTypeListOrUnitType1() {
        String location = root + "description-must-be-child-of-unitTypeList-or-unitType-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDescriptionMustBeChildOfUnitTypeListOrUnitType2() {
        String location = root + "description-must-be-child-of-unitTypeList-or-unitType-2.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDimensionMustBeChildOfUnitType() {
        String location = root + "dimension-must-be-child-of-unitType.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDimensionMustHaveNameAttribute() {
        String location = root + "dimension-must-have-name-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDimensionMustHavePowerAttribute() {
        String location = root + "dimension-must-have-power-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDimensionMustHaveUnitTypeAttribute() {
        String location = root + "dimension-must-have-unitType-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDimensionNameMustNotBeEmpty() {
        String location = root + "dimension-name-must-not-be-empty.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDimensionNameMustNotContainOnlyWhitespace() {
        String location = root + "dimension-name-must-not-contain-only-whitespace.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeDefinitionMustContainNonWhitespaceText() {
        String location = root + "unitType-definition-must-contain-non-whitespace-text.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeDefinitionMustContainText() {
        String location = root + "unitType-definition-must-contain-text.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeDefinitionMustContainXhtml() {
        String location = root + "unitType-definition-must-contain-xhtml.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeDescriptionMustContainNonWhitespaceText() {
        String location = root + "unitType-description-must-contain-non-whitespace-text.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeDescriptionMustContainText() {
        String location = root + "unitType-description-must-contain-text.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeDescriptionMustContainXhtml() {
        String location = root + "unitType-definition-must-contain-xhtml.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeDictionaryConventionMustBeDeclaredOnUnitTypeList() {
        String location = root + "unitType-dictionary-convention-must-be-declared-on-unitTypeList.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeMustBeChildOfUnitTypeList() {
        String location = root + "unitType-must-be-child-of-unitTypeList.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeMustContainDefinition() {
        String location = root + "unitType-must-contain-definition.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeMustHaveDimension() {
        String location = root + "unitType-must-have-dimension.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeMustHaveIdAttribute() {
        String location = root + "unitType-must-have-id-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeMustHaveNameAttribute() {
        String location = root + "unitType-must-have-name-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeMustHaveTitleAttribute() {
        String location = root + "unitType-must-have-title-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeMustNotHaveMultipleDefinition() {
        String location = root + "unitType-must-not-have-multiple-definition.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeMustNotHaveMultipleDescription() {
        String location = root + "unitType-must-not-have-multiple-description.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

     @Test
    public void testUnitTypeNameMustNotBeEmpty() {
        String location = root + "unitType-name-must-not-be-empty.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeNameMustNotBeContainOnlyWhitespace() {
        String location = root + "unitType-name-must-not-contain-only-whitespace.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeTitleMustNotBeEmpty() {
        String location = root + "unitType-title-must-not-be-empty.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeTitleMustNotBeContainOnlyWhitespace() {
        String location = root + "unitType-title-must-not-contain-only-whitespace.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeListMustContainAtLeastOneUnitType() {
        String location = root + "unitTypeList-must-contain-at-least-one-unitType.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeListMustHaveNamespaceAttribute() {
        String location = root + "unitTypeList-must-have-namespace-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeListMustNotHaveMultipleDescriptions() {
        String location = root + "unitTypeList-must-not-have-multiple-descriptions.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeListTitleMustNotBeEmpty() {
        String location = root + "unitTypeList-title-must-not-be-empty.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testUnitTypeListTitleMustNotBeContainOnlyWhitespace() {
        String location = root + "unitTypeList-title-must-not-contain-only-whitespace.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        log.info(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

}

