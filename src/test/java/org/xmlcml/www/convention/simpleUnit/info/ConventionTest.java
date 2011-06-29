package org.xmlcml.www.convention.simpleUnit.info;

import nu.xom.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.www.ConventionValidator;
import org.xmlcml.www.TestUtils;
import org.xmlcml.www.ValidationReport;
import org.xmlcml.www.ValidationResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    private String root = "convention/simpleUnit/info/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }


    @Test
      public void testChildCmlElement1() {
          String location = root+"child-cml-element-1.cml";
          Document input = testUtils.getFileAsDocument(location);
          report = conventionValidator.validate(input);
          assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
          assertTrue(location + " should contain info\n" + report.getReport().toXML(), report.containsInfo());
      }

    @Test
      public void testChildCmlElement2() {
          String location = root+"child-cml-element-2.cml";
          Document input = testUtils.getFileAsDocument(location);
          report = conventionValidator.validate(input);
          assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
          assertTrue(location + " should contain info\n" + report.getReport().toXML(), report.containsInfo());
      }

    @Test
      public void testChildCmlElement3() {
          String location = root+"child-cml-element-3.cml";
          Document input = testUtils.getFileAsDocument(location);
          report = conventionValidator.validate(input);
          assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
          assertTrue(location + " should contain info\n" + report.getReport().toXML(), report.containsInfo());
      }

    @Test
      public void testChildCmlElement4() {
          String location = root+"child-cml-element-4.cml";
          Document input = testUtils.getFileAsDocument(location);
          report = conventionValidator.validate(input);
          assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
          assertTrue(location + " should contain info\n" + report.getReport().toXML(), report.containsInfo());
      }

    @Test
      public void testChildCmlElement5() {
          String location = root+"child-cml-element-5.cml";
          Document input = testUtils.getFileAsDocument(location);
          report = conventionValidator.validate(input);
          assertEquals(location+" should be valid\n"+report.getReport().toXML() , ValidationResult.VALID, report.getValidationResult());
          assertTrue(location + " should contain info\n" + report.getReport().toXML(), report.containsInfo());
      }

}
