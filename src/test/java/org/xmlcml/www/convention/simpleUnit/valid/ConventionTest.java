package org.xmlcml.www.convention.simpleUnit.valid;

import nu.xom.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.www.ConventionValidator;
import org.xmlcml.www.TestUtils;
import org.xmlcml.www.ValidationReport;
import org.xmlcml.www.ValidationResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by IntelliJ IDEA.
 * User: jat45
 * Date: 23/12/10
 * Time: 14:49
 */
public class ConventionTest {

    private TestUtils testUtils = new TestUtils();
    private ConventionValidator conventionValidator = new ConventionValidator();
    private ValidationReport report = null;
    private String root = "convention/simpleUnit/valid/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
    public void testForeignElementsAreAllowed1() {
        String location = root+"valid1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
        assertFalse(location+" should not contain info\n"+report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testForeignElementsAreAllowed2() {
        String location = root+"valid1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
        assertFalse(location+" should not contain info\n"+report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testForeignElementsAreAllowed3() {
        String location = root+"valid1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
        assertFalse(location+" should not contain info\n"+report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testForeignElementsAreAllowed4() {
        String location = root+"valid1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
        assertFalse(location+" should not contain info\n"+report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testForeignElementsAreAllowed5() {
        String location = root+"valid1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
        assertFalse(location+" should not contain info\n"+report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testMultipleSimpleUnitInSameDocument() {
        String location = root+"valid1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
        assertFalse(location+" should not contain info\n"+report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testUnitListDoesNotHaveToBeRootElement1() {
        String location = root+"valid1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
        assertFalse(location+" should not contain info\n"+report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testUnitListDoesNotHaveToBeRootElement2() {
        String location = root+"valid1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
        assertFalse(location+" should not contain info\n"+report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testUnitListDoesNotHaveToBeRootElement3() {
        String location = root+"valid1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
        assertFalse(location+" should not contain info\n"+report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testUnitListDoesNotHaveToBeRootElement4() {
        String location = root+"valid1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
        assertFalse(location+" should not contain info\n"+report.getReport().toXML(), report.containsInfo());
    }

    @Test
    public void testValid1() {
        String location = root+"valid1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
        assertFalse(location+" should not contain info\n"+report.getReport().toXML(), report.containsInfo());
    }


}
