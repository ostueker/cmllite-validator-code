package org.xmlcml.www.convention.simpleUnit.warning;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.www.*;

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
public class ConventionTest {

    private TestUtils testUtils = new TestUtils();
    private ConventionValidator conventionValidator = new ConventionValidator();
    private ValidationReport report = null;
    private String root = "convention/compchem/warning/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
    public void testInvalidAreSchemaValid() {
        Collection<File> files = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/simpleUnit/warning"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", files.isEmpty());
        for (File file : files) {
            ValidationReport report = conventionValidator.validate(testUtils.getFileAsDocument(file));
//            CmlLiteValidator validator = new CmlLiteValidator();
//              ValidationReport report = validator.validate(testUtils.getFileAsDocument(file));

            System.out.println("report for: "+file.getAbsolutePath());
            CmlLiteValidator.print(report.getReport(), System.out);
            assertEquals(file.getAbsolutePath() + " should be schema valid " + report.getReport().toXML(), ValidationResult.VALID, report.getValidationResult());
        }
    }

}
