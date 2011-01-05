package org.xmlcml.www.convention.molecular.invalid;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.www.SchemaValidator;
import org.xmlcml.www.TestUtils;
import org.xmlcml.www.ValidationReport;
import org.xmlcml.www.ValidationResult;

import java.io.File;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by IntelliJ IDEA.
 * User: jat45
 * Date: 23/12/10
 * Time: 14:49
 */
public class SchemaTest {

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
    public void testInvalidAreSchemaValid() {
        Collection<File> files = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/molecular/invalid"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", files.isEmpty());
        for (File file : files) {
            ValidationReport report = schemaValidator.validate(testUtils.getFileAsDocument(file));
            assertEquals(file.getAbsolutePath() + " should be schema valid", ValidationResult.VALID, report.getValidationResult());
        }
    }
}
