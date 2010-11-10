package org.xmlcml.www.cmlcomp;

import org.junit.Test;
import org.xmlcml.www.CMLRuleValidator;
import org.xmlcml.www.CMLRuleValidator.Rule;


public class EmptyCMLCompTest extends CMLCompTest {

    public EmptyCMLCompTest() {
        assertionValue = true;
        validator = new CMLRuleValidator(Rule.CMLComp);
    }

    @Override
    protected String getTestResourcePath() {
        return super.getTestResourcePath() + "/empty";
    }

    @Test
    public void testAllZ() {
        testAll();
    }
}
