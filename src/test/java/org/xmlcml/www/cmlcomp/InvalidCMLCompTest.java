package org.xmlcml.www.cmlcomp;

import java.io.InputStream;
import org.junit.Test;
import org.xmlcml.www.CMLRuleValidator;
import org.xmlcml.www.CMLRuleValidator.Rule;

import static org.junit.Assert.*;

public class InvalidCMLCompTest {

    CMLRuleValidator validator = new CMLRuleValidator(Rule.CMLComp);

    private static String cmlPathOf(String name) {
        return "invalid/" + name;
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
