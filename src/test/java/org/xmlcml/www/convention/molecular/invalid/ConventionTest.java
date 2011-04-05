package org.xmlcml.www.convention.molecular.invalid;

import nu.xom.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.www.ConventionValidator;
import org.xmlcml.www.TestUtils;
import org.xmlcml.www.ValidationReport;
import org.xmlcml.www.ValidationResult;

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
    private String root = "convention/molecular/invalid/";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        report = null;
    }

    @Test
    public void testMoleculeMissingId() {
        String location = root + "molecule-missing-id.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testAtomMustBeChildOfAtomArray() {
        String location = root + "atom-must-be-child-of-atomArray.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        System.out.println(report.getReport().toXML());
        assertEquals(location + " should be invalid " + report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testMoleculeCannotBeChildOfAtom() {
        String location = root + "molecule-cannot-be-child-of-atom.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDuplicateAtomIdsPresent1() {
        String location = root + "duplicate-atom-ids-present-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testDuplicateAtomIdsPresent2() {
        String location = root + "duplicate-atom-ids-present-2.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testAtomRefsPointingToNonExistentAtom1() {
        String location = root + "atomRefs-pointing-to-non-existent-atom-1.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
        public void testAtomRefsPointingToNonExistentAtom2() {
            String location = root + "atomRefs-pointing-to-non-existent-atom-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testAtomRefsPointingToOutOfScopeAtom() {
            String location = root + "atomRefs2-pointing-to-out-of-scope-atom.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
    public void testAtomMissingId() {
        String location = root + "atom-missing-id.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }
    @Test
    public void testNoAtomsInAtomArray() {
        String location = root + "no-atoms-in-atomArray.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }
    @Test
    public void testMultipleAtomArraysPresent() {
        String location = root + "multiple-atomArrays-present.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }
    @Test
    public void testMultipleBondArraysPresent() {
        String location = root + "multiple-bondArrays-present.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testMoleculeWithAtomArrayAndChildMolecule() {
        String location = root + "molecule-with-atomArray-and-child-molecule.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
    public void testBondMustBeChildOfBondArray() {
        String location = root + "bond-must-be-child-of-bondArray.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        System.out.println(report.getReport().toXML());
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }


    @Test
    public void testBondArrayInvalidParent() {
        String location = root + "bondArray-invalid-parent.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }
    @Test
    public void testConventionPrefixMustBeBound() {
        String location = root + "convention-prefix-must-be-bound.cml";
        Document input = testUtils.getFileAsDocument(location);
        report = conventionValidator.validate(input);
        assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
    }

    @Test
        public void testBondStereoInvalidParent1() {
            String location = root + "bondStereo-invalid-parent-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoInvalidParent2() {
            String location = root + "bondStereo-invalid-parent-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testAtomRefs2MustContainTwoDifferentIds1() {
            String location = root + "atomRefs2-must-contain-two-different-ids-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
     @Test
        public void testAtomRefs2MustContainTwoDifferentIds2() {
            String location = root + "atomRefs2-must-contain-two-different-ids-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testAtomArrayInvalidParent1() {
            String location = root + "atomArray-invalid-parent-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testAtomArrayInvalidParent2() {
            String location = root + "atomArray-invalid-parent-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testX2WithoutY2() {
            String location = root + "x2-without-y2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testY2WithoutX2() {
            String location = root + "y2-without-x2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testX3AndY3WithoutZ3() {
            String location = root + "x3-and-y3-without-z3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testX3AndZ3WithoutY3() {
            String location = root + "x3-and-z3-without-y3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testY3AndZ3WithoutX3() {
            String location = root + "y3-and-z3-without-x3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testX3WithoutY3AndZ3() {
            String location = root + "x3-without-y3-and-z3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testY3WithoutX3AndZ3() {
            String location = root + "y3-without-x3-and-z3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testZ3WithoutX3AndY3() {
            String location = root + "z3-without-x3-and-y3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }


    @Test
        public void testHatchBondStereoMustNotHaveAtomRefs4() {
            String location = root + "hatch-bondStereo-must-not-have-atomRefs4.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testWedgeBondStereoMustNotHaveAtomRefs4() {
            String location = root + "wedge-bondStereo-must-not-have-atomRefs4.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testCisBondStereoMustHaveAtomRefs4() {
            String location = root + "cis-bondStereo-must-have-atomRefs4.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testTransBondStereoMustHaveAtomRefs4() {
            String location = root + "trans-bondStereo-must-have-atomRefs4.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }


    @Test
        public void testBondStereoAtomRefs2MustMatchParentBonds1() {
            String location = root + "bondStereo-atomRefs2-must-match-parent-bonds-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testBondStereoAtomRefs2MustMatchParentBonds2() {
            String location = root + "bondStereo-atomRefs2-must-match-parent-bonds-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testBondStereoAtomRefs2MustMatchParentBonds3() {
            String location = root + "bondStereo-atomRefs2-must-match-parent-bonds-3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testBondStereoAtomRefs2MustMatchParentBonds4() {
            String location = root + "bondStereo-atomRefs2-must-match-parent-bonds-4.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds1() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds2() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds3() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds4() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-4.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds5() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-5.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds6() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-6.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds7() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-7.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testBondStereoAtomRefs4MustContainParentBonds8() {
            String location = root + "bondStereo-atomRefs4-must-contain-parent-bonds-8.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testAtomParityInvalidParent() {
            String location = root + "atomParity-invalid-parent.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testPropertyMustHaveDictRef() {
            String location = root + "property-must-have-dictRef.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testScalarMustHaveUnitsAndDataType1() {
            String location = root + "scalar-must-have-units-and-datatype-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testScalarMustHaveUnitsAndDataType2() {
            String location = root + "scalar-must-have-units-and-datatype-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testScalarMustHaveUnitsAndDataType3() {
            String location = root + "scalar-must-have-units-and-datatype-3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }



    @Test
        public void testScalarInvalidParent1() {
            String location = root + "scalar-invalid-parent-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testScalarInvalidParent2() {
            String location = root + "scalar-invalid-parent-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testMolecularConventionMustBeSpecifiedOnCmlOrMolecule() {
            String location = root + "molecular-convention-must-be-specified-on-cml-or-molecule.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }

    @Test
        public void testFormulaMustHaveAtomArrayOrConciseOrInline() {
            String location = root + "formula-must-have-atomArray-or-concise-or-inline.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testMolecularConventionMustContainMolecule1() {
            String location = root + "molecular-convention-must-contain-molecule-1.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testMolecularConventionMustContainMolecule2() {
            String location = root + "molecular-convention-must-contain-molecule-2.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }
    @Test
        public void testMolecularConventionMustContainMolecule3() {
            String location = root + "molecular-convention-must-contain-molecule-3.cml";
            Document input = testUtils.getFileAsDocument(location);
            report = conventionValidator.validate(input);
            assertEquals(location + " should be invalid "+report.getReport().toXML(), ValidationResult.INVALID, report.getValidationResult());
        }


}
