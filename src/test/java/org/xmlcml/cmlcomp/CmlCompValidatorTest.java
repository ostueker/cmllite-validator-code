package org.xmlcml.cmlcomp;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.www.CMLRuleValidator;

import java.io.File;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CmlCompValidatorTest {

    CMLRuleValidator validator;
    Collection<File> validFiles = null;
    Collection<File> invalidFiles = null;

    @Before
    public void setUp() throws Exception {
        validator = new CMLRuleValidator("cmlcomp-rules.xsl");
        validFiles = FileUtils.listFiles(new File("./src/test/resources/cmllite/cmlcomp/valid"), new String[]{"cml"}, false);
        invalidFiles = FileUtils.listFiles(new File("./src/test/resources/cmllite/cmlcomp/invalid"), new String[]{"cml"}, false);
    }

    @Test
    @Ignore
    public void testValidCmlComp() {
        assertFalse("there should be valid test documents", validFiles.isEmpty());
        for (File file : validFiles) {
            System.out.println("Validating file : " + file.getPath());
            boolean isValid = validator.validate(file);
            assertTrue(file.getPath() + " should be valid", isValid);
        }
    }

    @Test
    @Ignore
    public void testInvalidCmlComp() {
        assertFalse("there should be invalid test documents", invalidFiles.isEmpty());
        for (File file : invalidFiles) {
            System.out.println("Validating file : " + file.getPath());
            boolean isValid = validator.validate(file);
            assertTrue(file.getPath() + " should be invalid", isValid);
        }
    }
}
