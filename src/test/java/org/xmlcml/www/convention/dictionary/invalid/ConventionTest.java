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
    public void testDefinitionMustBeChildOfEntry() {
        String location = root + "definition-must-be-child-of-entry.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDefinitionMustContainNonWhitespaceText() {
        String location = root + "definition-must-contain-non-whitespace-text.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDefinitionMustContainText() {
        String location = root + "definition-must-contain-text.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDefinitionMustHaveXhtmlChild() {
        String location = root + "definition-must-have-xhtml-child.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDescriptionMustBeChildOfDictionaryOrEntry1() {
        String location = root + "description-must-be-child-of-dictionary-or-entry-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDescriptionMustBeChildOfDictionaryOrEntry2() {
        String location = root + "description-must-be-child-of-dictionary-or-entry-2.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDescriptionMustContainNonWhitespaceText() {
        String location = root + "description-must-contain-non-whitespace-text.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDescriptionMustContainText() {
        String location = root + "description-must-contain-text.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }


    @Test
    public void testDescriptionMustHaveXhtmlChild() {
        String location = root + "description-must-have-xhtml-child.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }


    @Test
    public void testEntryCannotHaveMultipleDescriptions() {
        String location = root + "entry-cannot-have-multiple-descriptions.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }


    @Test
    public void testEntryIdMustBeUniqueWithinDictionary() {
        String location = root + "entry-id-must-be-unique-within-dictionary.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testEntryMustBeChildOfDictionary() {
        String location = root + "entry-must-be-child-of-dictionary.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testEntryMustHaveIdAttribute() {
        String location = root + "entry-must-have-id-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testEntryMustSingleDefinitionChild1() {
        String location = root + "entry-must-have-single-definition-child-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testEntryMustSingleDefinitionChild2() {
        String location = root + "entry-must-have-single-definition-child-2.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testEntryMustHaveTermAttribute() {
        String location = root + "entry-must-have-term-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testEntryWithNoneUnitTypeMustHaveNoneUnit() {
        String location = root + "entry-with-none-unitType-must-have-none-unit.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testEntryWithUnknownUnitTypeMustNotHaveUnit() {
        String location = root + "entry-with-unknown-unitType-must-not-have-unit.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }


    @Test
    public void testMissingNamespaceAttribute() {
        String location = root + "missing-namespace-attribute.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testMultipleDesciptionsOfDictionaryPresent() {
        String location = root + "multiple-descriptions-of-dictionary-present.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }


    @Test
    public void testMultipleDictionaryElementsPresent() {
        String location = root + "multiple-dictionary-elements-present.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testNoDictionaryElementPresent1() {
        String location = root + "no-dictionary-element-present-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
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
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }


    @Test
    public void testNonDictionaryCmlElementPresent1() {
        String location = root + "non-dictionary-cml-element-present-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testNonDictionaryCmlElementPresent2() {
        String location = root + "non-dictionary-cml-element-present-2.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testNonDictionaryCmlElementPresent3() {
        String location = root + "non-dictionary-cml-element-present-3.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testNonDictionaryCmlElementPresent4() {
        String location = root + "non-dictionary-cml-element-present-4.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testNonDictionaryCmlElementPresent5() {
        String location = root + "non-dictionary-cml-element-present-5.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testNonDictionaryCmlElementPresent6() {
        String location = root + "non-dictionary-cml-element-present-6.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testNonDictionaryCmlElementPresent7() {
        String location = root + "non-dictionary-cml-element-present-7.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testNonDictionaryCmlElementPresent8() {
        String location = root + "non-dictionary-cml-element-present-8.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testOnlyXhtmlElementsCanBeChildrenOfDefinition() {
        String location = root + "only-xhtml-elements-can-be-children-of-definition.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testOnlyXhtmlElementsCanBeChildrenOfDescription() {
        String location = root + "only-xhtml-elements-can-be-children-of-description.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testTermCannotContainOnlyWhitespace() {
        String location = root + "term-cannot-contain-only-whitespace.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testTermMustNotBeEmpty() {
        String location = root + "term-must-not-be-empty.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }


}

