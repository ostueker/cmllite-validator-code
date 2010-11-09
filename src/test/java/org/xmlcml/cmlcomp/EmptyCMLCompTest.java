package org.xmlcml.cmlcomp;

import java.io.InputStream;
import org.junit.Test;
import org.xmlcml.www.CMLRuleValidator;


import static org.junit.Assert.*;

public class EmptyCMLCompTest {

    CMLRuleValidator validator = new CMLRuleValidator("cmlcomp-rules.xsl");

    @Test
    public void testNormalEmptyCML() {
        InputStream ins = getClass().getResourceAsStream("/cmlcomp/empty/empty-cml.cml");
        assertNotNull(ins);
        boolean validate = validator.validate(ins);
        System.out.println(validate);
        assertTrue(validate);
    }

    @Test
    public void testNormalEmptyCMLComp() {
        InputStream ins = getClass().getResourceAsStream("/cmlcomp/empty/empty-cmlcomp.cml");
        assertNotNull(ins);
        boolean validate = validator.validate(ins);
        assertTrue(validate);
    }

}
