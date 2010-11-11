package org.xmlcml.www.cmlcomp;

import org.junit.Test;
import org.xmlcml.www.CMLRuleValidator;
import org.xmlcml.www.CMLRuleValidator.Rule;



public class ValidCMLCompTest extends CMLCompTester {

    public ValidCMLCompTest() {
        validator = new CMLRuleValidator(Rule.CMLComp);
        assertionValue = true;
    }

    @Override
    protected String getTestResourcePath() {
        return super.getTestResourcePath() + "/valid";
    }

    @Test
    //@Ignore
    @Override
    public void testAll() {
        super.testAll();

    }
}
