package org.xmlcml.www.convention.dictionary.warning;

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
    public void testEntryShouldHaveDataTypeAttribute() {
        String location = root + "entry-should-have-dataType-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning "+report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testEntryShouldHaveUnitsAttribute() {
        String location = root + "entry-should-have-units-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning "+report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testEntryShouldHaveUnitTypeAttribute() {
        String location = root + "entry-should-have-unitType-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning "+report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testMissingDictionaryConvention() {
        String location = root + "missing-dictionary-convention.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning "+report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testMissingDictionaryDescription() {
        String location = root + "missing-dictionary-description.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning "+report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testMissingDictionaryPrefixAttribute() {
        String location = root + "missing-dictionaryPrefix-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning "+report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testMissingTitleAttribute() {
        String location = root + "missing-title-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning "+report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testNamespaceShouldBeResolvableUrl() {
        String location = root + "namespace-should-be-resolvable-url.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        System.out.println("report 1");
        CmlLiteValidator.print(report.getReport(), System.out);
        assertEquals(location + "should be valid "+report.getReport().toXML(), ValidationResult.VALID, report.getValidationResult());
        URIValidator uriValidator = new URIValidator();
        System.out.println("report 2");
        report = uriValidator.validate(input);
        CmlLiteValidator.print(report.getReport(), System.out);
        assertEquals(location + " should be warning " + report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testNamespaceShouldEndInSlashOrHash() {
        String location = root + "namespace-should-end-in-slash-or-hash.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning "+report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testTermAttributeShouldOnlyContainAscii1() {
        String location = root + "term-attribute-should-only-contain-ascii-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning "+report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testTermAttributeShouldOnlyContainAscii2() {
        String location = root + "term-attribute-should-only-contain-ascii-2.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be warning "+report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }

    @Test
    public void testUnitValueNotFoundInDictionary() {
        String location = root + "unit-value-not-found-in-dictionary.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be valid "+report.getReport().toXML(), ValidationResult.VALID, report.getValidationResult());
        CmlLiteValidator.print(report.getReport(), System.out);
        URIValidator uriValidator = new URIValidator();
        report = uriValidator.validate(input);
        assertEquals(location + " should be warning "+report.getReport().toXML(), ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }


}
