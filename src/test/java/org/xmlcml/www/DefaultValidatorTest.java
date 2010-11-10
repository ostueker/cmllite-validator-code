package org.xmlcml.www;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

public class DefaultValidatorTest {

    DefaultValidator validator;
    Collection<File> validMolecular = Collections.EMPTY_LIST;
    Collection<File> invalidMolecular = Collections.EMPTY_LIST;
    Collection<File> validCmlcomp = Collections.EMPTY_LIST;
    Collection<File> invalidCmlcomp = Collections.EMPTY_LIST;

    @Before
    public void setUp() throws Exception {
        validator = new DefaultValidator(false);
        validMolecular = FileUtils.listFiles(new File("./src/test/resources/cmllite/molecular/valid"), new String[]{"cml"}, false);
        invalidMolecular = FileUtils.listFiles(new File("./src/test/resources/cmllite/molecular/invalid"), new String[]{"cml"}, false);
//        validCmlcomp = FileUtils.listFiles(new File("./src/test/resources/cmllite/cmlcomp/valid"), new String[]{"cml"}, false);
//        invalidCmlcomp = FileUtils.listFiles(new File("./src/test/resources/cmllite/cmlcomp/invalid"), new String[]{"cml"}, false);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testValidMolecularWithoutQNameChecks() {
        assertFalse("there should be test documents", validMolecular.isEmpty());
        for (File file : validMolecular) {
            assertTrue(file.getAbsolutePath() + " should be valid", validator.validate(file));
        }
    }

    @Test
    public void testInvalidMolecularWithoutQNameChecks() {
        assertFalse("there should be test documents", invalidMolecular.isEmpty());
        for (File file : invalidMolecular) {
            assertFalse(file.getAbsolutePath() + " should be invalid", validator.validate(file));
        }
    }
//    @Test
//    @Ignore
//    public void testValidCmlcompWithoutQNameChecks() {
//        assertFalse("there should be test documents", validMolecular.isEmpty());
//        for (File file : validCmlcomp) {
//            InputStream input = null;
//            try {
//                input = FileUtils.openInputStream(file);
//            } catch (IOException e) {
//                fail("should be able to create an InputStream from: " + file.getAbsolutePath());
//            }
//            try {
//                assertTrue(file.getAbsolutePath() + " should be valid", validator.validate(IOUtils.toString(input), false));
//            } catch (IOException e) {
//                fail("should be able to read the InputStream from: " + file);
//            }
//        }
//    }
//
//    @Test
//    @Ignore
//    public void testInvalidCmlcompWithoutQNameChecks() {
//        assertFalse("there should be test documents", invalidCmlcomp.isEmpty());
//        for (File file : invalidCmlcomp) {
//            InputStream input = null;
//            try {
//                input = FileUtils.openInputStream(file);
//            } catch (IOException e) {
//                fail("should be able to create an InputStream from: " + file.getAbsolutePath());
//            }
//            try {
//                assertFalse(file.getAbsolutePath() + " should be invalid", validator.validate(IOUtils.toString(input), false));
//            } catch (IOException e) {
//                fail("should be able to read the InputStream from: " + file);
//            }
//        }
//    }
}