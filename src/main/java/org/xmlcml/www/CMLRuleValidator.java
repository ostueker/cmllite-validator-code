package org.xmlcml.www;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;
import org.apache.log4j.Logger;

/**
 * Default XSLT rule validator.
 * @author jat45
 * @author Weerapong Phadungsukanan
 */
public class CMLRuleValidator {

    public static enum Rule {

        CMLLite("molecular-rules.xsl"),
        CMLComp("cmlcomp-rules.xsl");
        private final String name;

        Rule(String name) {
            this.name = name;
        }

        String ruleName() {
            return name;
        }
    }
    private static Logger log = Logger.getLogger(SchemaValidator.class);
    private XSLTransform transform = null;
    private Document report = null;

    public CMLRuleValidator(String cmlrule) {
        try {
//            transform = createXSLTTransform(cmlrule);
        } catch (Exception ex) {
            throw new RuntimeException("Exception thrown while creating XML tansformation", ex);
        }
    }

    public CMLRuleValidator(Rule rule) {
        this(rule.ruleName());
    }

    public boolean validate(Document document) {
        report = null;
        Nodes nodes;
        try {
            nodes = transform.transform(document);
        } catch (XSLException e) {
            log.info(e);
            return false;
        }
        report = XSLTransform.toDocument(nodes);
        Nodes failures = report.query("//*[local-name()='"+ValidationReport.errorElementName+"' and namespace-uri()='"+ValidationReport.reportNS+"']");
        return failures.size() == 0;
    }

    public Document getReport() {
        return report;
    }

    public Document getShortReport() {
        Document copyOfReport = new Document(report);
        Nodes allNodes = copyOfReport.getRootElement().query("child::*");
        for (int i = 0; i < allNodes.size(); i++) {
            Element el = (Element) allNodes.get(i);
            Attribute locAttr = el.getAttribute("location");
            if (locAttr != null) {
                el.removeAttribute(locAttr);
            }
        }
        return copyOfReport;
    }
}
