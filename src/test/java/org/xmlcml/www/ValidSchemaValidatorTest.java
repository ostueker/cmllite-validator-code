package org.xmlcml.www;

import nu.xom.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: jat45
 * Date: 22/12/10
 * Time: 10:12
 */
public class ValidSchemaValidatorTest {

    TestUtils testUtils = new TestUtils();
    SchemaValidator schemaValidator = new SchemaValidator();
    ValidationReport report = null;


     @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
    public void testForeignNamespacedAttributes1() {
        String location = "schema/valid/foreign-namespaced-attributes-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = schemaValidator.validate(input);
        assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
    }

    @Test
    public void testMixedCmlAndNonCml1() {
        String location = "schema/valid/mixed-cml-and-non-cml-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = schemaValidator.validate(input);
        assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
    }

    @Test
    public void testMixedCmlAndNonCml2() {
        String location = "schema/valid/mixed-cml-and-non-cml-2.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = schemaValidator.validate(input);
        assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
    }

    @Test
    public void testMixedCmlAndNonCml3() {
        String location = "schema/valid/mixed-cml-and-non-cml-3.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = schemaValidator.validate(input);
        assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
    }

    @Test
    public void testMixedCmlAndNonCml4() {
        String location = "schema/valid/mixed-cml-and-non-cml-4.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = schemaValidator.validate(input);
        assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
    }



}
