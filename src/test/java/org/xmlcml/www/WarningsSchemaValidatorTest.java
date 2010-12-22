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
public class WarningsSchemaValidatorTest {

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
    public void testNoCmlPresent1() {
        String location = "schema/warning/no-cml-present.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = schemaValidator.validate(input);
        assertEquals(location+" should be valid with warnings", ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
    }
}
