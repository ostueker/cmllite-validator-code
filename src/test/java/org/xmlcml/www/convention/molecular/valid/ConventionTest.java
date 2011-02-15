package org.xmlcml.www.convention.molecular.valid;

import nu.xom.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.www.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: jat45
 * Date: 23/12/10
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class ConventionTest {

     private TestUtils testUtils = new TestUtils();
    private ConventionValidator conventionValidator = new ConventionValidator();
    private ValidationReport report = null;
        private String root = "convention/molecular/valid/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
        public void testMinimalMolecule1() {
            String location = root+"minimal-molecule-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testMinimalMolecule2() {
            String location = root+"minimal-molecule-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testMinimalMolecule3() {
            String location = root+"minimal-molecule-3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testMinimalMolecule4() {
            String location = root+"minimal-molecule-4.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }

    @Test
        public void testMinimalMoleculeWithNonCmlContent1() {
            String location = root+"minimal-molecule-with-non-cml-content-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testMinimalMoleculeWithNonCmlContent2() {
            String location = root+"minimal-molecule-with-non-cml-content-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testMinimalMoleculeWithNonCmlContent3() {
            String location = root+"minimal-molecule-with-non-cml-content-3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testAtomWithNonCmlContent() {
            String location = root+"atom-with-non-cml-content.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }

    @Test
        public void testMolecularConventionNotAsRootElement() {
            String location = root+"molecular-convention-not-as-root-element.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testMolecularMoleculeWithinNonMolecular() {
            String location = root+"molecular-molecule-within-non-molecular.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testRepeatedAtomIdsInDifferentMolecules() {
            String location = root+"repeated-atom-ids-in-different-molecules.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testMinimalMoleculeWithForeignSibling() {
            String location = root+"minimal-molecule-with-foreign-sibling.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testAtomsInFomrulaDoNotNeedId() {
            String location = root+"atoms-in-formula-do-not-need-id.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testBondOrderOtherShouldHaveDictRef() {
            String location = root+"bond-order-other-should-have-dictRef.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoOtherShouldHaveDictRef() {
            String location = root+"bondStereo-other-should-have-dictRef.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }

    @Test
           public void testBondStereoAtomRefs2MustMatchParentBonds1() {
               String location = root + "bondStereo-atomRefs2-must-match-parent-bonds-1.cml";
               Document input = testUtils.getFileAsDocument(location);
               report = conventionValidator.validate(input);
               assertEquals(location + " should be valid", ValidationResult.VALID, report.getValidationResult());
           }
    @Test
           public void testBondStereoAtomRefs2MustMatchParentBonds2() {
               String location = root + "bondStereo-atomRefs2-must-match-parent-bonds-2.cml";
               Document input = testUtils.getFileAsDocument(location);
               report = conventionValidator.validate(input);
               assertEquals(location + " should be valid", ValidationResult.VALID, report.getValidationResult());
           }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds1() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds2() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds3() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds4() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-4.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds5() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-5.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds6() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-6.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds7() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-7.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds8() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-8.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be valid", ValidationResult.VALID, report.getValidationResult());
        }


    @Test
        public void testPropertyMustHaveDictRefAndTitle() {
            String location = root+"property-must-have-dictRef-and-title.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testScalarMustHaveUnitsAndDataTypeAndBeChildOfProperty() {
            String location = root+"scalar-must-have-units-and-datatype-and-be-child-of-property.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testMolecularConventionMoleculeWithNonMolecularSibling1() {
            String location = root+"molecular-convention-molecule-with-non-molecular-sibling-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testMolecularConventionMoleculeWithNonMolecularSibling2() {
            String location = root+"molecular-convention-molecule-with-non-molecular-sibling-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testFormulaMustHaveAtomArrayOrConciseOrInline1() {
            String location = root+"formula-must-have-atomArray-or-concise-or-inline-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testFormulaMustHaveAtomArrayOrConciseOrInline2() {
            String location = root+"formula-must-have-atomArray-or-concise-or-inline-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testFormulaMustHaveAtomArrayOrConciseOrInline3() {
            String location = root+"formula-must-have-atomArray-or-concise-or-inline-3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testFormulaMustHaveAtomArrayOrConciseOrInline4() {
            String location = root+"formula-must-have-atomArray-or-concise-or-inline-4.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testFormulaMustHaveAtomArrayOrConciseOrInline5() {
            String location = root+"formula-must-have-atomArray-or-concise-or-inline-5.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid "+report.getReport().toXML(), ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testFormulaMustHaveAtomArrayOrConciseOrInline6() {
            String location = root+"formula-must-have-atomArray-or-concise-or-inline-6.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testFormulaMustHaveAtomArrayOrConciseOrInline7() {
            String location = root+"formula-must-have-atomArray-or-concise-or-inline-7.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testMustBeAtLeastOneMoleculeInMolecularConvention1() {
            String location = root+"must-be-at-least-one-molecule-in-molecular-convention-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
    @Test
        public void testMustBeAtLeastOneMoleculeInMolecularConvention2() {
            String location = root+"must-be-at-least-one-molecule-in-molecular-convention-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location+" should be valid", ValidationResult.VALID, report.getValidationResult());
        }
}
