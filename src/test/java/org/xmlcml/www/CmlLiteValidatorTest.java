package org.xmlcml.www;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

import static org.junit.Assert.*;

public class CmlLiteValidatorTest {

    CmlLiteValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new CmlLiteValidator();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testValidMolecular() {
        Collection<File> validMolecular = FileUtils.listFiles(new File("./src/test/resources/cmllite/molecular/valid"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", validMolecular.isEmpty());
        for (File file : validMolecular) {
            ValidationReport result = null;
            try {
                result = validator.validate(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            }
            System.out.println(result.getReport().toXML());
            assertEquals(file.getAbsolutePath() + " should be valid", ValidationResult.VALID, result.getValidationResult());
        }
    }
       @Test
    public void testWarningMolecular() {
        Collection<File> warningMolecular = FileUtils.listFiles(new File("./src/test/resources/cmllite/molecular/warning"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", warningMolecular.isEmpty());
        for (File file : warningMolecular) {
            ValidationReport report = null;
            try {
                report = validator.validate(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            }
            assertEquals(file.getAbsolutePath() + " should have warnings", ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
        }
    }
    @Test
    public void testInvalidMolecular() {
        Collection<File> invalidMolecular = FileUtils.listFiles(new File("./src/test/resources/cmllite/molecular/invalid"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", invalidMolecular.isEmpty());
        for (File file : invalidMolecular) {
            ValidationReport report = null;
            try {
                report = validator.validate(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            }
            System.out.println(file.getName()+" "+report.getReport().toXML());
            assertEquals(file.getAbsolutePath() + " should be invalid", ValidationResult.INVALID, report.getValidationResult());
        }
    }

//    @Test
//    public void testValidCmlcompWithoutQNameChecks() {
//        Collection<File> validCmlcomp = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/cmlcomp/valid"), new String[]{"cml"}, false);
//
//        assertFalse("there should be test documents", validCmlcomp.isEmpty());
//        for (File file : validCmlcomp) {
//            ValidationReport report = null;
//            try {
//                report = validator.validate(new FileInputStream(file));
//            } catch (FileNotFoundException e) {
//                fail("should be able to read from "+file.getAbsolutePath());
//            }
//            System.out.println(report.getReport().toXML());
//            assertEquals(file.getAbsolutePath() + " should be valid", ValidationResult.VALID, report.getValidationResult());
//        }
//    }

    @Test   
    public void testInvalidCmlcomp() {
        Collection<File> invalidCmlcomp = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/cmlcomp/invalid"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", invalidCmlcomp.isEmpty());
        for (File file : invalidCmlcomp) {
            ValidationReport report = null;
            try {
                report = validator.validate(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            }            
            assertEquals(file.getAbsolutePath() + " should be invalid", ValidationResult.INVALID, report.getValidationResult());
        }
    }
}
