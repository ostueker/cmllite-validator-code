package org.xmlcml.www;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
			log.fatal("can't create a document parser",e);
			throw new RuntimeException(e);
		}
	}
	
	private Document buildDocument(InputStream input) {
		Document document = null;
		try {
			 document = documentBuilder.parse(input);
		} catch (SAXException e) {
			log.error("can't create a document",e);
		} catch (IOException e) {
			log.error("can't create a document",e);
		}
		return document;	
	}
	

	public boolean validate(InputStream input) {
		Document document = buildDocument(input);
		if (document != null) {
			try {
				validator.validate(new DOMSource(document));
				return true;
			} catch (SAXException e) {
				log.error("invalid document",e);
			} catch (IOException e) {
				log.error("invalid document",e);
			}
		} 
		return false;		
	}
	
}
