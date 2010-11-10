package org.xmlcml.www.cmlcomp;

import org.junit.Ignore;
import org.junit.Test;
import org.xmlcml.www.CMLRuleValidator;
import org.xmlcml.www.CMLRuleValidator.Rule;

public class InvalidCMLCompTest extends CMLCompTest {

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
    public void testAllZ() {
        testAll();
    }

    @Test
    public void testIndividual() {
        // test a cmlcomp joblist which contains two empty jobs with two unique titles.
        test("joblist-two-empty-jobs-with-two-titles.cml");
    }
//
//    @Test
//    //@Ignore
//    public void testAll() {
//        // test a cmlcomp which has a module of unknown role.
//        test("undefined-module.cml");
//        // test a cmlcomp which has a molecule under cml root
//        test("undefined-molecule.cml");
//        // test a cmlcomp which has an empty joblist
//        test("empty-joblist.cml");
//        // test a cmlcomp joblist which contains an empty job (+no title).
//        test("joblist-one-empty-job.cml");
//        // test a cmlcomp joblist which contains an empty job but titled).
//        test("joblist-one-empty-job-with-title.cml");
//        // test a cmlcomp joblist which contains two empty jobs (no title).
//        test("joblist-two-empty-jobs.cml");
//        // test a cmlcomp joblist which contains two empty jobs with one title.
//        test("joblist-two-empty-jobs-with-one-title.cml");
//    }
}
