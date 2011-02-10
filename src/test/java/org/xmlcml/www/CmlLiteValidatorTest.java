package org.xmlcml.www;

import nu.xom.Builder;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;

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

    @Test
    public void testMolecularQNamesAreURLs() {
        Collection<File> files = FileUtils.listFiles(new File("./src/test/resources/org/xmlcml/www/convention/molecular/valid"), new String[]{"cml"}, false);
        assertFalse("there should be test documents", files.isEmpty());
        for (File file : files) {
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
            assertEquals(file.getAbsolutePath() + " QNames etc should URLs\n"+report.getReport().toXML()+"\n", ValidationResult.VALID, report.getValidationResult());
        }
    }
}
