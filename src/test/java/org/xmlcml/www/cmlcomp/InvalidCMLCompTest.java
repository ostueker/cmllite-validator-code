package org.xmlcml.www.cmlcomp;

import java.io.InputStream;
import org.junit.Test;
import org.xmlcml.www.CMLRuleValidator;


import static org.junit.Assert.*;

public class InvalidCMLCompTest {

    CMLRuleValidator validator = new CMLRuleValidator("cmlcomp-rules.xsl");

    private static String cmlPathOf(String name) {
        return "/cmlcomp/invalid/" + name;
    }

    private void test(String name) {
        InputStream ins = getClass().getResourceAsStream(cmlPathOf(name));
        assertNotNull(ins);
        boolean validate = validator.validate(ins);
        assertFalse(validate);
    }

    @Test
    public void testAll() {
        // test a cmlcomp which has a module of unknown role.
        test("cmlcomp-contain-undefined-module.cml");
        // test a cmlcomp which has a molecule under cml root
        test("cmlcomp-contain-undefined-molecule.cml");
    }
}
