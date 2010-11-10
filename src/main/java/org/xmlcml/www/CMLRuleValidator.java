package org.xmlcml.www;

import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;
import org.apache.log4j.Logger;

/**
 * Default XSLT rule validator.
 * @author jat45
 * @author Weerapong Phadungsukanan
 */
public class CMLRuleValidator extends AbstractValidator {

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

    public CMLRuleValidator(String cmlrule) {
        try {
            transform = createXSLTTransform(cmlrule);
        } catch (Exception ex) {
            throw new RuntimeException("Exception thrown while creating XML tansformation", ex);
        }
    }

    public CMLRuleValidator(Rule rule) {
        this(rule.ruleName());
    }

    @Override
    public boolean validate(Document document) {
        Nodes nodes;
        try {
            nodes = transform.transform(document);
        } catch (XSLException e) {
            log.info(e);
            return false;
        }
        Document result = XSLTransform.toDocument(nodes);
        print(result, System.out);
        Nodes failures = result.query("//*[local-name()='error' and namespace-uri()='http://www.xml-cml.org/report']");
        for (int index = 0, n = failures.size(); index < n; index++) {
            Node node = failures.get(index);
            //log.error("\n"+node.toXML());
        }
        return failures.size() == 0;
    }
}
