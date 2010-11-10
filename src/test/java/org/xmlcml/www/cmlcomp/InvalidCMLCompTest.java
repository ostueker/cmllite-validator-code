package org.xmlcml.www.cmlcomp;

import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.www.CMLRuleValidator;
import org.xmlcml.www.CMLRuleValidator.Rule;

public class InvalidCMLCompTest extends CMLCompTester {

    public InvalidCMLCompTest() {
        assertionValue = false;
        validator = new CMLRuleValidator(Rule.CMLComp);
    }

    @Override
    protected String getTestResourcePath() {
        return super.getTestResourcePath() + "/invalid";
    }

    @Test
    @Ignore
    @Override
    public void testAll() {
        super.testAll();
    }

    @Test
    public void testIndividual() {
        test("one-job-empty-init-empty-final.cml");
    }
}
