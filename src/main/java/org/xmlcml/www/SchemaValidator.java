package org.xmlcml.www;

import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.Serializer;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jat45
 * @author Weerapong Phadungsukanan
 */
public class SchemaValidator {

    private Logger log = Logger.getLogger(getClass());
    private Validator validator;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;

    public SchemaValidator() {
        if (validator == null) {
            validator = createCMLSchemaValidator();
        }
        if (documentBuilderFactory == null) {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
        }
        if (documentBuilder == null) {
            documentBuilder = createDocumentBuilder();
        }
    }

    private Validator createCMLSchemaValidator() {
        // create a SchemaFactory capable of understanding WXS schemas
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // load a WXS schema, represented by a Schema instance
        InputStream cmlSchemaInputStream = getClass().getResourceAsStream("CMLSchema.xsd");
        if (cmlSchemaInputStream != null) {
            Source schemaFile = new StreamSource(cmlSchemaInputStream);
            try {
                Schema schema = factory.newSchema(schemaFile);
                return schema.newValidator();
            } catch (SAXException e) {
                log.fatal("can't create new validator", e);
                throw new RuntimeException(e);
            }
        } else {
            String msg = "CMLSchema.xsd is not found under " + getClass().getName();
            log.fatal(msg);
            throw new RuntimeException(msg);
        }
    }

    private DocumentBuilder createDocumentBuilder() {
        try {
            return documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.fatal("can't create a document parser", e);
            throw new RuntimeException(e);
        }
    }

    public ValidationReport validate(nu.xom.Document doc) {
        ValidationReport report = new ValidationReport("schema-validation-test");
        report.setValidationResult(ValidationResult.VALID);
        List<InputStream> inputs = getEldestCmlChildren(doc);
        for (InputStream is : inputs) {
            Document document = buildDocument(is);
            if (document != null) {
                try {
                    validator.validate(new DOMSource(document));
                    System.out.println("valid "+doc.toXML());
                } catch (SAXException e) {
                    report.addError(e.getMessage());
                    report.setValidationResult(ValidationResult.INVALID);
                } catch (IOException e) {
                    report.addError(e.getMessage());
                    report.setValidationResult(ValidationResult.INVALID);
                } catch (Exception e) {
                    report.addError(e.getMessage());
                    report.setValidationResult(ValidationResult.INVALID);
                }
            }
        }
        return report;
    }

    private Document buildDocument(InputStream input) {
        Document document = null;
        try {
            document = documentBuilder.parse(input);
        } catch (SAXException e) {
            log.error("can't create a document", e);
        } catch (IOException e) {
            log.error("can't create a document", e);
        }
        return document;
    }

    private List<InputStream> getEldestCmlChildren(nu.xom.Document document) {
        Nodes nodes = document.query("//*[namespace-uri()='" + CmlLiteValidator.CML_NS + "' and not(ancestor::*[namespace-uri()='" + CmlLiteValidator.CML_NS + "'])]");
        int size = nodes.size();
        List<InputStream> list = new ArrayList<InputStream>(size);
        for (int i = 0; i < size; i++) {
            Element e = new Element((Element) nodes.get(i));
            nu.xom.Document d = new nu.xom.Document(e);
            list.add(toInputStream(d));
        }
        return list;
    }

    /**
     * Prints a XOM document to an OutputStream without having to remember the
     * serializer voodoo. The encoding is always UTF-8.
     *
     * @param doc the XOM Document to convert to an InputStream
     * @return
     */
    public InputStream toInputStream(nu.xom.Document doc) {
        Serializer serializer;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            serializer = new Serializer(baos, "UTF-8");
            serializer.setIndent(0);
            serializer.write(doc);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
        return IOUtils.toInputStream(baos.toString());
    }

}
