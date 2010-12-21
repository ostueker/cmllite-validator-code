package org.xmlcml.www;

import nu.xom.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author jat45
 * @author Weerapong Phadungsukanan
 */
public class CmlLiteValidator {

    private static Logger log = Logger.getLogger(CmlLiteValidator.class);

    public final static String CML_NS = "http://www.xml-cml.org/schema";

    private XmlWellFormednessValidator xmlWellFormednessValidator = new XmlWellFormednessValidator();
    private SchemaValidator schemaValidator = new SchemaValidator();
    private ConventionValidator conventionValidator = new ConventionValidator();
    private QNameValidator qNameValidator = new QNameValidator();

     /**
     * Validate an XML from given input stream.
     *
     * @param ins an input stream of XML
     * @return true if valid, false otherwise.
     */
    public ValidationReport validate(InputStream ins) {
        String input;
        try {
            input = IOUtils.toString(ins, "UTF-8");
        } catch (IOException e) {
            ValidationReport report = new ValidationReport("exception");
            report.addError(e.getMessage());
            return createFinalReport(ValidationResult.INVALID, report);
        }
        return validate(input);
    }

    /**
     * Validate a XOM Document object.
     *
     * @param document a XOM Document object.
     * @return true if valid, false otherwise.
     */
    public ValidationReport validate(Document document) {
        ValidationReport xmlWellFormedReport = new ValidationReport(XmlWellFormednessValidator.reportTitle);
        xmlWellFormedReport.addValid("xml is well formed");
        ValidationReport schemaReport = schemaValidator.validate(document);
        switch (schemaReport.getValidationResult()){
            case INVALID: {
                return createFinalReport(schemaReport.getValidationResult(), xmlWellFormedReport, schemaReport);
            }
            default: {
                schemaReport.addValid("document conforms to the schema");
                ValidationReport conventionsReport = conventionValidator.validate(document);

                ValidationReport qnameReachableReport = qNameValidator.validate(document);
                if (ValidationResult.VALID.equals(qnameReachableReport.getValidationResult())) {
                    qnameReachableReport.addValid("all dictRefs are resolvable");
                }
                switch (conventionsReport.getValidationResult()) {
                    case VALID: {
                        conventionsReport.addValid("document conforms to the convention(s) specified");
                        return createFinalReport(qnameReachableReport.getValidationResult(), xmlWellFormedReport, schemaReport, conventionsReport, qnameReachableReport);
                    }
                    default : {
                        return createFinalReport(conventionsReport.getValidationResult(), xmlWellFormedReport, schemaReport, conventionsReport, qnameReachableReport);
                    }
                }
            }
        }
    }

    public ValidationReport validate(String xml) {
        ValidationReport wellFormedReport = xmlWellFormednessValidator.validate(xml);
        switch (wellFormedReport.getValidationResult()) {
            case VALID:
                return validate(buildDocument(xml));
            default:
                return createFinalReport(wellFormedReport.getValidationResult(), wellFormedReport);
        }
    }

    private ValidationReport createFinalReport(ValidationResult result, ValidationReport ... reports) {
        ValidationReport finalReport = new ValidationReport("final-report");
        finalReport.setValidationResult(result);
        for (ValidationReport report : reports) {
            finalReport.addReport(report.getReport());
        }
        return finalReport;
    }

    private Document buildDocument(String xml) {
        try {
            return new Builder().build(IOUtils.toInputStream(xml, "UTF-8"));
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

        /**
     * Prints a XOM document to an OutputStream without having to remember the
     * serializer voodoo. The encoding is always UTF-8.
     *
     * @param doc the XOM Document to print
     * @param out where to print to
     */
    public static void print(Document doc, OutputStream out) {
        Serializer serializer;
        try {
            serializer = new Serializer(out, "UTF-8");
            serializer.setIndent(4);
            serializer.write(doc);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }
}
