package org.xmlcml.www.cmlcomp;

import org.junit.Ignore;
import java.io.InputStream;
import org.junit.Test;
import org.xmlcml.www.CMLRuleValidator;
import org.xmlcml.www.CMLRuleValidator.Rule;


import static org.junit.Assert.*;

public class ValidCMLCompTest {

    CMLRuleValidator validator = new CMLRuleValidator(Rule.CMLComp);

    private static String cmlPathOf(String name) {
        return "valid/" + name;
    }

    private void test(String name) {
        InputStream ins = getClass().getResourceAsStream(cmlPathOf(name));
        assertNotNull(ins);
        boolean validate = validator.validate(ins);
        assertTrue(validate);
    }

    @Test
    @Ignore
    public void testAll() {
        test("cmlcomp-contain-undefined-module.cml");
        test("cmlcomp-contain-undefined-molecule.cml");
    }
}
