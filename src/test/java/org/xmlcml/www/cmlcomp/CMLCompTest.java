package org.xmlcml.www.cmlcomp;

import java.util.Collection;
import org.apache.commons.io.FileUtils;
import java.io.File;
import nu.xom.Builder;
import nu.xom.Document;
import org.apache.log4j.Logger;
import org.xmlcml.www.CMLRuleValidator;

import static org.junit.Assert.*;

/**
 * In order to use this class, the derived class must overide getTestResourcePath() method
 * to return path to its test resource folder containing test cml files.
 *
 * Each cml file must have a cml as a root container with attribute cmlx:note
 * where xmlns:cmlx="http://www.xml-cml.org/schema/cmlx". This allows the test to
 * be able to extract the note for each cml test file which is very useful for debugging.
 *
 * You can put any note in cmlx:note but preferably in a single line.
 *
 * @author Weerapong Phadungsukanan
 */
public class CMLCompTest {

    private static Logger logger = Logger.getLogger(CMLCompTest.class);
    protected Logger log = Logger.getLogger(getClass());
    protected CMLRuleValidator validator = null;
    protected boolean assertionValue = true;
    protected final String CMLX_NS = "http://www.xml-cml.org/schema/cmlx";

    /**
     * Get resource folder. This method must be overridden by its derived class.
     * 
     * @return
     */
    protected String getTestResourcePath() {
        return "./src/test/resources/" + getClass().getPackage().getName().replace(".", "/");
    }

    /**
     * Get resource file for a given resource name. It resolves the full path.
     * 
     * @param name
     * @return
     */
    protected File getTestResourceFile(String name) {
        return new File(getTestResourcePath() + "/" + name);
    }

    /**
     * Build a XOM document from a file.
     *
     * @param file
     * @return
     */
    protected static Document buildDocumentFromFile(File file) {
        Document doc = null;
        try {
            doc = new Builder().build(file);
        } catch (Exception ex) {
            logger.error(ex);
        }
        return doc;
    }

    /**
     * Test for a given cml file name in the resource folder.
     * 
     * @param name
     */
    protected void test(String name) {
        test(getTestResourceFile(name));
    }

    /**
     * Test for a given cml file in the resource folder.
     * @param file
     */
    protected void test(File file) {
        Document doc = buildDocumentFromFile(file);
        assertNotNull(doc);
        String note = doc.getRootElement().getAttributeValue("note", CMLX_NS);
        log.info("Test cml file   : " + file.toString());
        log.info("#    for reason : " + note);
        boolean validate = validator.validate(doc);
        log.info("#    result is  : " + ((validate) ? "valid" : "invalid"));
        assertEquals(assertionValue, validate);
        log.info("======== End of test ========");
    }

    /**
     * Batch test for all files in the resource folder.
     */
    protected void testAll() {
        System.out.println(getTestResourcePath());
        File resourceDir = new File(getTestResourcePath());
        Collection<File> resources = FileUtils.listFiles(resourceDir, new String[]{"cml"}, false);
        for (File file : resources) {
            test(file);
        }
    }
}
