package org.xmlcml.www;

import nu.xom.*;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jat45
 * Date: 28-Oct-2010
 * Time: 17:15:55
 *
 * @author jat45
 * @author Weerapong Phadungsukanan
 */
public class ConventionValidator {

    private static Logger log = Logger.getLogger(ConventionValidator.class);
    private static Map<URI, XSLTransform> knownConventions = null;
    private final static String conventionNS = "http://www.xml-cml.org/convention/";
    private static URI dummy = null;

    static {
        registerKnownConventionLocations();
    }

    public static Set<URI> getSupportedConventions() {
        return new HashSet<URI>(knownConventions.keySet());
    }

    private static void registerKnownConventionLocations() {
        knownConventions = new HashMap<URI, XSLTransform>();
        try {
            knownConventions.put(new URI(conventionNS + "dictionary"), createXSLTTransform(ConventionValidator.class, "dictionary-rules.xsl"));
            knownConventions.put(new URI(conventionNS + "molecular"), createXSLTTransform(ConventionValidator.class, "molecular-rules.xsl"));
            knownConventions.put(new URI(conventionNS + "compchem"), createXSLTTransform(ConventionValidator.class, "compchem-rules.xsl"));
            knownConventions.put(new URI(conventionNS + "unit-dictionary"), createXSLTTransform(ConventionValidator.class, "unit-dictionary-rules.xsl"));
            knownConventions.put(new URI(conventionNS + "unitType-dictionary"), createXSLTTransform(ConventionValidator.class, "unitType-dictionary-rules.xsl"));

            knownConventions.put(new URI(conventionNS + "simpleUnit"), createXSLTTransform(ConventionValidator.class, "simple-units-rules.xsl"));
        } catch (URISyntaxException e) {
            log.fatal("can't create uris", e);
            throw new RuntimeException(e);
        }

        try {
            dummy = new URI("http://www.xml-cml.org/dictionary/dummy");
        } catch (URISyntaxException e) {
             log.fatal("can't create uris", e);
            throw new RuntimeException(e);
        }
    }


    public ValidationReport validate(Document doc) {
        ValidationReport report = new ValidationReport("convention-validation-test");
        if (doc != null) {
            Map<URI, List<Element>> conventionsMap = findConventions(doc, report);
            if (conventionsMap != null) {
                if (conventionsMap.isEmpty()) {
                    report.addWarning("no conventions found");
                    if (!ValidationResult.INVALID.equals(report.getValidationResult())) {
                        report.setValidationResult(ValidationResult.VALID_WITH_WARNINGS);
                    }
                } else {
                    validate(conventionsMap, report);
                }
            }
        }
        Nodes errors = report.getReport().query("//*[local-name()='"+ValidationReport.errorElementName+"' and namespace-uri()='"+ValidationReport.reportNS+"']");
        if (errors.size() > 0) {
            report.setValidationResult(ValidationResult.INVALID);
        } else {
            if (report.getReport().query("//*[local-name()='"+ValidationReport.warningElementName+"' and namespace-uri()='"+ValidationReport.reportNS+"']").size() > 0) {
                if (!ValidationResult.INVALID.equals(report.getValidationResult())) {
                    report.setValidationResult(ValidationResult.VALID_WITH_WARNINGS);
                }
            }
        }
        if (!ValidationResult.INVALID.equals(report.getValidationResult())) {
            report.addValid("document conforms to all the conventions specified");
        }
        return report;
    }

    private void validate(Map<URI, List<Element>> conventionToElement, ValidationReport report) {
        for (Map.Entry<URI, List<Element>> entry : conventionToElement.entrySet()) {
            for (Element element : entry.getValue()) {
                validate(entry.getKey(), element, report);
            }
        }
    }

    private void validate(URI convention, Element start, ValidationReport report) {
        if (knownConventions.containsKey(convention)) {
            XSLTransform xslt = knownConventions.get(convention);
            Nodes nodes = null;
            try {
                String absoluteXPathToStartElement = generateFullPath(start);
                xslt.setParameter("absoluteXPathToStartElement", absoluteXPathToStartElement);
                nodes = xslt.transform(start.getDocument());
            } catch (XSLException e) {
                log.info(e);
                e.printStackTrace();
                report.addWarning(e.getMessage());
            }
            if (nodes != null) {
                Document result = XSLTransform.toDocument(nodes);

//                CmlLiteValidator.print(result, System.out);
                Nodes failures = result.query("//*[local-name()='"+ValidationReport.errorElementName+"' and namespace-uri()='"+ValidationReport.reportNS+"']");
                for (int index = 0, n = failures.size(); index < n; index++) {
                    Element e = (Element) failures.get(index);
                    report.addError(e);
                }
                Nodes warnings = result.query("//*[local-name()='"+ValidationReport.warningElementName+"' and namespace-uri()='"+ValidationReport.reportNS+"']");
                for (int index = 0, n = warnings.size(); index < n; index++) {
                    Element e = (Element) warnings.get(index);
                    report.addWarning(e);
                }
                Nodes infos = result.query("//*[local-name()='"+ValidationReport.infoElementName+"' and namespace-uri()='"+ValidationReport.reportNS+"']");
                for (int index = 0, n = infos.size(); index < n; index++) {
                    Element e = (Element) infos.get(index);
                    report.addInfo(e);
                }
            }
        } else {
            if (!dummy.equals(convention)) {
                report.addWarning("the validation of convention: '" + convention + "' is not supported by this service");
            }
        }
    }

    private HashMap<URI, List<Element>> findConventions(Document document, ValidationReport report) {
        HashMap<URI, List<Element>> map = new HashMap<URI, List<Element>>();
        Nodes nodes = document.query("//*[namespace-uri()='" + CmlLiteValidator.CML_NS + "']/@convention");
        for (int i = 0, length = nodes.size(); i < length; i++) {
            Attribute attribute = (Attribute) nodes.get(i);
            Element element = (Element) attribute.getParent();
            String[] value = attribute.getValue().split(":");
            String ns = element.getNamespaceURI(value[0]);
            if (null != ns) {


                URI convention;
                try {
                    if (!ns.endsWith("/")) {
                        ns = ns + "/";
                    }
                    convention = new URI(ns+value[1]);
                } catch (URISyntaxException e) {
                    report.addError("Not a valid convention value, it should be a URI: '" + ns + value[1] + "'");
                    return null;
                } catch (Exception e) {
                    report.addError("Not a valid convention value, it should be a URI: '" + ns + value[1] + "'");
                    return null;
                }
                if (convention != null) {
                    if (map.containsKey(convention)) {
//                        map.get(convention).add(element);
                    } else {
                        List<Element> list = new ArrayList<Element>();
                        list.add(element);
                        map.put(convention, list);
                    }
                }
            } else {
                report.addError("no namespace declared for the prefix: " + value[0]);
                report.setValidationResult(ValidationResult.INVALID);
            }
        }
        return map;
    }

    private String generateFullPath(Element element) {
        String localName = element.getLocalName();
        String namespaceUri = element.getNamespaceURI();
        String name = "*[local-name()='" + localName + "' and namespace-uri()='" + namespaceUri + "']";
        Nodes nodes = element.query("preceding-sibling::*[local-name()='"+localName+"' and namespace-uri() = '"+namespaceUri+"']");
        int position = nodes.size();
        if (element.getDocument().getRootElement().equals(element)) {
            return "/" + name + "[" + (1 + position) + "]";
        }
        return generateFullPath(element.getParent()) + "/" + name + "[" + (1 + position) + "]";
    }

    private String generateFullPath(Node node) {
        if (node instanceof Attribute) {
            Attribute attribute = (Attribute) node;
            String name = "@*[local-name()='" + attribute.getLocalName() + "' and namespace-uri()='" + attribute.getNamespaceURI() + "']";
            return generateFullPath((Element) attribute.getParent()) + "/" + name;
        }
        Element element = (Element) node;
        return generateFullPath(element);
    }

    /**
     * Create a saxon 9 XSLTransform from a given xslt name. Leading "/" in xsltName
     * indicates absolute path in classpath, otherwise relative to derived Class's
     * package.
     *
     * @param xsltName
     * @return
     * @throws Exception
     */
    protected XSLTransform createXSLTTransform(String xsltName) throws Exception {
        return createXSLTTransform(getClass(), xsltName);
    }

    /**
     * Create a saxon 9 XSLTransform from a given xslt name. Leading "/" in xsltName
     * indicates absolute path in classpath, otherwise relative to derived Class's
     * package.
     *
     * @param clzz     Class where the resource is belong to.
     * @param xsltName
     * @return
     * @throws Exception
     */
    protected static XSLTransform createXSLTTransform(Class clzz, String xsltName) {
        Builder builder = new Builder();
        URL xslt = clzz.getResource(xsltName);
        /* set up to use saxon 9 */
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        Document stylesheet = null;
        InputStream is = null;
        try {
            is = xslt.openStream();
            try {
                Builder b = new Builder();
                stylesheet = builder.build(is);
            } catch (ParsingException e) {
                log.fatal("error parsing stylesheet at: " + xslt.toString() + " " + e);
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            log.fatal("can't load stylesheet at: " + xslt.toString() + " " + e);
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        try {
            return new XSLTransform(stylesheet);
        } catch (XSLException e) {
            log.fatal("can't create style sheet from: " + xslt.toString() + " " + e);
            throw new RuntimeException(e);
        }
    }
}
