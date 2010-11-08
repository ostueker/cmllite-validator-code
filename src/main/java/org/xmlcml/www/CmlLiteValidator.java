package org.xmlcml.www;

/**
 * XSLT CMLLite validator. Delegate most stuffs from CMLRuleValidator.
 * @author jat45
 * @author Weerapong Phadungsukanan
 */
public class CmlLiteValidator extends CMLRuleValidator {

    public CmlLiteValidator() {
        super("molecular-rules.xsl");
    }
}
