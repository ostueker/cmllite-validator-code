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
public class InvalidSchemaValidatorTest {

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
    public void testInvalidAttributePresent1() {
        String location = "schema/invalid/invalid-attribute-present-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = schemaValidator.validate(input);
        assertEquals(location+" should be invalid", ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testInvalidElementPresent1() {
        String location = "schema/invalid/invalid-element-present-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = schemaValidator.validate(input);
        assertEquals(location+" should be invalid", ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testInvalidElementPresent2() {
        String location = "schema/invalid/invalid-element-present-2.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = schemaValidator.validate(input);
        assertEquals(location+" should be invalid", ValidationResult.INVALID, report.getValidationResult());
    }
}
