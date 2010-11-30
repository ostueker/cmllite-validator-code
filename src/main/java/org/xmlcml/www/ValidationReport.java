package org.xmlcml.www;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

/**
 * Created by IntelliJ IDEA.
 * User: jat45
 * Date: 10-Nov-2010
 * Time: 13:01:32
 * To change this template use File | Settings | File Templates.
 */
public class ValidationReport {

    public static final String reportNS = "http://www.xml-cml.org/report/";
    public static final String warningElementName = "warning";
    public static final String errorElementName = "error";

    private ValidationResult validationResult = null;

    private Document document = null;
    private Element element = null;

    public ValidationReport(String testType) {
        document = new Document(new Element("report", reportNS));
        element = new Element(testType, reportNS);
        document.getRootElement().appendChild(element);
        setValidationResult(ValidationResult.INVALID);
    }

    public ValidationResult getValidationResult() {
        return this.validationResult;
    }

    public void setValidationResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    public void addReport(Document report) {
        Element root = report.getRootElement();
        Elements elements = root.getChildElements();
        for (int i = 0, size = elements.size(); i < size; i++) {
            Element child = elements.get(i);
            child.detach();
            element.appendChild(child);
        }
    }

    public Document getReport() {
        return this.document;
    }

    public void addError(String message) {
        Element e = new Element(errorElementName, reportNS);
        e.appendChild(message);
        element.appendChild(e);
    }

    public void addError(Element error) {
        error.detach();
        element.appendChild(error);
    }


    public void addWarning(String message) {
        Element e = new Element(warningElementName, reportNS);
        e.appendChild(message);
        element.appendChild(e);
    }

     public void addWarning(Element warning) {
        warning.detach();
        element.appendChild(warning);
    }

    public void addValid(String message) {
        Element e = new Element("valid", reportNS);
        e.appendChild(message);
        element.appendChild(e);
    }

}
