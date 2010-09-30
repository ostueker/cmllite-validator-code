package org.xmlcml.cmlcomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import nu.xom.Builder;
import nu.xom.Document;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.www.CMLRuleValidator;

public class CmlCompMinValidatorTest {

    CMLRuleValidator validator;
    Builder builder = new Builder();
    Collection<Document> validCmlComp = new LinkedList<Document>();

    @Before
    public void setUp() throws Exception {
        validator = new CMLRuleValidator("cmlcomp-min.xsl");

        Collection<File> validFiles = FileUtils.listFiles(new File("./src/test/resources/cmlcomp/min"), new String[]{"cml"}, false);
        for (File file : validFiles) {
            validCmlComp.add(builder.build(file));
        }

    }

    @Test
    public void testValidCmlComp() {
        assertFalse("there should be valid test documents", validCmlComp.isEmpty());
        for (Document document : validCmlComp) {
            System.out.println("validating: " + document.getBaseURI());
            boolean isValid = validator.validate(document);
        }
    }

}
