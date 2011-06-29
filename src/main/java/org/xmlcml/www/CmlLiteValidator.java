package org.xmlcml.www;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;

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
    private URIValidator uriValidator = new URIValidator();

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
        switch (schemaReport.getValidationResult()) {
            case INVALID: {
                return createFinalReport(schemaReport.getValidationResult(), xmlWellFormedReport, schemaReport);
            }
            default: {
                ValidationReport conventionsReport = conventionValidator.validate(document);
                // we can do qname validation even if the document is not fully convention valid

                ValidationReport qnameReachableReport = uriValidator.validate(document);
                if (ValidationResult.VALID.equals(qnameReachableReport.getValidationResult())) {
                    qnameReachableReport.addValid("all dictRefs are resolvable");
                }
                switch (conventionsReport.getValidationResult()) {
                    case VALID: {
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

    private ValidationReport createFinalReport(ValidationResult result, ValidationReport... reports) {
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

    public static void main(String[] args) {
        if (args.length == 1) {
            File file = new File(args[0]);
            InputStream inputStream = null;
            try {
                inputStream = new BufferedInputStream(new FileInputStream(file));
            } catch (IOException e) {
                System.err.println("Can't read from " + file.getAbsolutePath() + " " + e);
                System.exit(1);
            }
            CmlLiteValidator validator = new CmlLiteValidator();
            ValidationReport report = validator.validate(inputStream);
            switch (report.getValidationResult()) {
                case VALID: {
                    System.exit(0);
                    break;
                }
                case VALID_WITH_WARNINGS: {
                    CmlLiteValidator.print(report.getReport(), System.out);
                    System.exit(0);
                }
                default: {
                    CmlLiteValidator.print(report.getReport(), System.out);
                    System.exit(1);
                }
            }

        } else {
            System.out.println("usage: CmlLiteValidator <in-file>");
            System.out.println("returns 0 if valid or valid with warnings and 1 otherwise");
            System.exit(1);
        }

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
