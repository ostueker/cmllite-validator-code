package org.xmlcml.cmlcomp;

import nu.xom.Builder;
import nu.xom.Document;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.www.CMLRuleValidator;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CmlCompValidatorTest {

    CMLRuleValidator validator;
    Builder builder = new Builder();
    Collection<Document> validCmlComp = new LinkedList<Document>();
    Collection<Document> invalidCmlComp = new LinkedList<Document>();

    @Before
    public void setUp() throws Exception {
        validator = new CMLRuleValidator("cmlcomp-rules.xsl");

        Collection<File> validFiles = FileUtils.listFiles(new File("./src/test/resources/cmllite/cmlcomp/valid"), new String[]{"cml"}, false);
        for (File file : validFiles) {
            validCmlComp.add(builder.build(file));
        }

        Collection<File> invalidFiles = FileUtils.listFiles(new File("./src/test/resources/cmllite/cmlcomp/invalid"), new String[]{"cml"}, false);
        for (File file : invalidFiles) {
            invalidCmlComp.add(builder.build(file));
        }

    }

    @Test
    @Ignore
    public void testValidCmlComp() {
        assertFalse("there should be valid test documents", validCmlComp.isEmpty());
        for (Document document : validCmlComp) {
            System.out.println("validating: " + document.getBaseURI());
            boolean isValid = validator.validate(document);
            assertTrue(document.getBaseURI() + " should be valid", isValid);
        }
    }

    @Test
    @Ignore
    public void testInvalidCmlComp() {
        assertFalse("there should be invalid test documents", invalidCmlComp.isEmpty());
        for (Document document : invalidCmlComp) {
            System.out.println("validating: " + document.getBaseURI());
            boolean isValid = validator.validate(document);
            assertFalse(document.getBaseURI() + " should be invalid", isValid);
        }
    }
}
