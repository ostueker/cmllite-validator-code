package org.xmlcml.www.cmlcomp;

import java.io.InputStream;
import org.junit.Test;
import org.xmlcml.www.CMLRuleValidator;
import org.xmlcml.www.CMLRuleValidator.Rule;

import static org.junit.Assert.*;

public class EmptyCMLCompTest {

    CMLRuleValidator validator = new CMLRuleValidator(Rule.CMLComp);

    private static String cmlPathOf(String name) {
        return "empty/" + name;
    }

    private void testValid(String name) {
        InputStream ins = getClass().getResourceAsStream(cmlPathOf(name));
        assertNotNull(ins);
        boolean validate = validator.validate(ins);
        assertTrue(validate);
    }

    @Test
    public void testAll() {
        // test an empty cml which has no child and no convention - it is valid.
        testValid("empty-cml.cml");
        // test an empty cmlcomp which has no child - it is valid.
        testValid("empty-cmlcomp.cml");
    }
}
