package org.xmlcml.www;

import nu.xom.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchemaValidator {

    private static Validator validator;
    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;

    private static Logger log = Logger.getLogger(SchemaValidator.class);

    private SchemaValidator() {
        // private constructor
    }

    public static synchronized SchemaValidator newInstance() {
        if (validator == null) {
            validator = createValidator();
        }
        if (documentBuilderFactory == null) {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
        }
        if (documentBuilder == null) {
            documentBuilder = createDocumentBuilder();
        }
        return new SchemaValidator();
    }

    private static Validator createValidator() {
        // create a SchemaFactory capable of understanding WXS schemas
        SchemaFactory factory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // load a WXS schema, represented by a Schema instance
        URL schemaUrl = Thread.currentThread().getContextClassLoader().getResource("CMLSchema.xsd");
        Source schemaFile;
        try {
            schemaFile = new StreamSource(schemaUrl.openStream());
        } catch (IOException e) {
            log.fatal("cannot load schema", e);
            throw new RuntimeException(e);
        }
        Schema schema;
        try {
            schema = factory.newSchema(schemaFile);
            return schema.newValidator();
        } catch (SAXException e) {
            log.fatal("can't create new validator", e);
            throw new RuntimeException(e);
        }
    }

    private static DocumentBuilder createDocumentBuilder() {
        try {
            return documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.fatal("can't create a document parser", e);
            throw new RuntimeException(e);
        }
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


    public boolean validate(String input) {        
        List<InputStream> inputs = getEldestCmlChildren(input);
        for (InputStream is : inputs) {
            Document doc = buildDocument(is);
            if (doc != null) {
                try {
                    validator.validate(new DOMSource(doc));
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

    private List<InputStream> getEldestCmlChildren(String input) {
        List<InputStream> list = Collections.emptyList();
        try {
            nu.xom.Document document = new Builder().build(IOUtils.toInputStream(input));            
            Nodes nodes = document.query("//*[namespace-uri()='" + org.xmlcml.www.Validator.CmlNS + "' and not(ancestor::*[namespace-uri()='" + org.xmlcml.www.Validator.CmlNS + "'])]");
            int size = nodes.size();
            list = new ArrayList<InputStream>(size);
            for (int i = 0; i < size; i++) {
                Element e = new Element((Element) nodes.get(i));
                nu.xom.Document d = new nu.xom.Document(e);
                list.add(print(d));
            }
        } catch (ParsingException e) {
            log.error("invalid document", e);
        } catch (IOException e) {
            log.error("invalid document", e);
        }

        return list;
    }

     /**
     * Prints a XOM document to an OutputStream without having to remember the
     * serializer voodoo. The encoding is always UTF-8.
     *
     * @param doc the XOM Document to print
     */
    public InputStream print(nu.xom.Document doc) {
        Serializer serializer;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            serializer = new Serializer(baos, "UTF-8");
            serializer.setIndent(0);
            serializer.write(doc);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
		}
        return IOUtils.toInputStream(baos.toString());
	}
}
