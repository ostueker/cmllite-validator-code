package org.xmlcml.www;

import nu.xom.Builder;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author jat45
 * @author Weerapong Phadungsukanan
 */
public class XmlWellFormednessValidator {

    public static final String reportTitle =    "well-formed-test";

    /**
     * An XML document is well-formed if it is not null.
     * Method validate(String xml) and validate(File file) in the CmlLiteValidator
     * try to create XOM document first and pass it as argument to this method. If
     * XOM document cannot be created by any reason it is considered malformed.
     * 
     * @param xml
     * @return
     */
    public ValidationReport validate(String xml) {
        ValidationReport report = new ValidationReport(reportTitle);
        try {
            new Builder().build(IOUtils.toInputStream(xml, "UTF-8"));
            report.setValidationResult(ValidationResult.VALID);
            report.addValid("xml is well formed");
        } catch (Exception ex) {
            report.addError(ex.getMessage());
            report.setValidationResult(ValidationResult.INVALID);
        }
        return report;
    }
}
