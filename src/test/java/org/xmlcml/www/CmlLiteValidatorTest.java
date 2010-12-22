package org.xmlcml.www;

import nu.xom.Builder;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class CmlLiteValidatorTest {

    CmlLiteValidator validator;
    XmlWellFormednessValidator xmlWellFormednessValidator;
    SchemaValidator schemaValidator;
    ConventionValidator conventionValidator;
    QNameValidator qNameValidator;

    @Before
    public void setUp() throws Exception {
        validator = new CmlLiteValidator();
        xmlWellFormednessValidator = new XmlWellFormednessValidator();
        schemaValidator = new SchemaValidator();
        conventionValidator = new ConventionValidator();
        qNameValidator = new QNameValidator();
    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
//    public void testIndividualValidMolecular() {
//        File file = new File("./src/test/resources/org/xmlcml/www/xml/invalid/non-xml-3.cml");
//            ValidationReport result = null;
//            try {
//                result = validator.validate(new FileInputStream(file));
//            } catch (FileNotFoundException e) {
//                fail("should be able to read from "+file.getAbsolutePath());
//            }
//            System.out.println(file.getName()+"\n"+result.getReport().toXML());
//            assertEquals(file.getAbsolutePath() + " should not be valid", ValidationResult.INVALID, result.getValidationResult());
//    }

    @Test
    public void testMolecularAreWellFormedXml() {
        Collection<File> validMolecular = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/molecular/valid"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", validMolecular.isEmpty());
        for (File file : validMolecular) {
            ValidationReport report = null;
            try {
                report = xmlWellFormednessValidator.validate(IOUtils.toString(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (IOException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            }
            assertEquals(file.getAbsolutePath() + " should be well formed xml", ValidationResult.VALID, report.getValidationResult());
        }
    }

    @Test
    public void testMolecularAreSchemaCompliant() {
        Collection<File> validMolecular = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/molecular/valid"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", validMolecular.isEmpty());
        for (File file : validMolecular) {
            ValidationReport report = null;
            try {
                report = schemaValidator.validate(new Builder().build(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (IOException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (ValidityException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (ParsingException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            }
            assertEquals(file.getAbsolutePath() + " should be schema valid", ValidationResult.VALID, report.getValidationResult());
        }
    }

    @Test
    public void testMolecularAreConventionValid() {
        Collection<File> validMolecular = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/molecular/valid"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", validMolecular.isEmpty());
        for (File file : validMolecular) {
            ValidationReport report = null;
            try {
                report = conventionValidator.validate(new Builder().build(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (IOException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (ValidityException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (ParsingException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            }
            assertEquals(file.getAbsolutePath() + " should be schema valid", ValidationResult.VALID, report.getValidationResult());
        }
    }

    @Test
    public void testMolecularQNamesAreURLs() {
        Collection<File> validMolecular = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/molecular/valid"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", validMolecular.isEmpty());
        for (File file : validMolecular) {
            ValidationReport report = null;
            try {
                report = qNameValidator.validate(new Builder().build(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (IOException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (ValidityException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (ParsingException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            }
            assertEquals(file.getAbsolutePath() + " should be schema valid", ValidationResult.VALID, report.getValidationResult());
        }
    }


    @Test
    public void testValidMolecular() {
        Collection<File> validMolecular = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/molecular/valid"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", validMolecular.isEmpty());
        for (File file : validMolecular) {
            System.out.println("file "+file.getName());
            ValidationReport result = null;
            try {
                result = validator.validate(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            }
            System.out.println(file.getName()+"\n"+result.getReport().toXML());
            assertEquals(file.getAbsolutePath() + " should be valid", ValidationResult.VALID, result.getValidationResult());
        }
    }
       @Test
    public void testWarningMolecular() {
        Collection<File> warningMolecular = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/molecular/warning"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", warningMolecular.isEmpty());
        for (File file : warningMolecular) {
            ValidationReport report = null;
            try {
                report = validator.validate(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            }
            System.out.println(report.getReport().toXML());
            assertEquals(file.getAbsolutePath() + " should have warnings", ValidationResult.VALID_WITH_WARNINGS, report.getValidationResult());
        }
    }

     @Test
    public void testWarningMolecularAreWellFormedXml() {
        Collection<File> validMolecular = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/molecular/warning"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", validMolecular.isEmpty());
        for (File file : validMolecular) {
            ValidationReport report = null;
            try {
                report = xmlWellFormednessValidator.validate(new IOUtils().toString(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (IOException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            }
            assertEquals(file.getAbsolutePath() + " should be well formed xml", ValidationResult.VALID, report.getValidationResult());
        }
    }

     @Test
    public void testWarningMolecularAreSchemaCompliant() {
        Collection<File> validMolecular = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/molecular/warning"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", validMolecular.isEmpty());
        for (File file : validMolecular) {
            ValidationReport report = null;
            try {
                report = schemaValidator.validate(new Builder().build(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (IOException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (ValidityException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            } catch (ParsingException e) {
                fail("should be able to read from "+file.getAbsolutePath());
            }
            System.out.println("file "+file.getName()+"\n"+report.getReport().toXML());
            assertEquals(file.getAbsolutePath() + " should be schema  compliant "+report.getReport().toXML(), ValidationResult.VALID, report.getValidationResult());
        }
    }

    @Test
    public void testInvalidMolecular() {
        Collection<File> invalidMolecular = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/molecular/invalid"), new String[]{"cml"}, false);
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
        Collection<File> invalidCmlcomp = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/cmlcomp/invalid"), new String[]{"cml"}, false);
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
