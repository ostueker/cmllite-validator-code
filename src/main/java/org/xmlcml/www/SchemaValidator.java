package org.xmlcml.www;

import java.io.File;
import nu.xom.*;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchemaValidator extends AbstractValidator {

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

    @Override
    public boolean validate(nu.xom.Document doc) {
        List<InputStream> inputs = getEldestCmlChildren(doc);
        for (InputStream is : inputs) {
            Document document = buildDocument(is);
            if (document != null) {
                try {
                    validator.validate(new DOMSource(document));
                } catch (SAXException e) {
                    log.error("invalid document", e);
                    return false;
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
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
        List<InputStream> list = Collections.emptyList();
        Nodes nodes = document.query("//*[namespace-uri()='" + org.xmlcml.www.Validator.CmlNS + "' and not(ancestor::*[namespace-uri()='" + org.xmlcml.www.Validator.CmlNS + "'])]");
        int size = nodes.size();
        list = new ArrayList<InputStream>(size);
        for (int i = 0; i < size; i++) {
            Element e = new Element((Element) nodes.get(i));
            nu.xom.Document d = new nu.xom.Document(e);
            list.add(print(d));
        }
        return list;
    }

}
