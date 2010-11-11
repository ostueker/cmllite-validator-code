package org.xmlcml.www;

import nu.xom.Document;
import nu.xom.Element;

/**
 * Created by IntelliJ IDEA.
 * User: jat45
 * Date: 10-Nov-2010
 * Time: 13:01:32
 * To change this template use File | Settings | File Templates.
 */
public class ValidationReport {


    private ValidationResult validationResult = null;

    private Document document = null;

    public ValidationReport() {
        document = new Document(new Element("report", "http://www.xml-cml.org/report"));
        setValidationResult(ValidationResult.INVALID);
    }

    public ValidationResult getValidationResult() {
        return this.validationResult;
    }

    public void setValidationResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    public void addReport(Element report) {
        document.appendChild(new Element(report));
    }

    public Document getReport() {
        return this.document;
    }

}
