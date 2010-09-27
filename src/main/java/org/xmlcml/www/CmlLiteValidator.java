package org.xmlcml.www;

import java.io.OutputStream;
import java.net.URL;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.Serializer;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;

import org.apache.log4j.Logger;

public class CmlLiteValidator {

	private static Logger log = Logger.getLogger(SchemaValidator.class);

	private static XSLTransform transform = null;

	private CmlLiteValidator() {
	}

	public static synchronized CmlLiteValidator newInstance() {
		if (transform == null) {
			transform = createTransform();
		}
		return new CmlLiteValidator();
	}

	private static XSLTransform createTransform() {
		Builder builder = new Builder();
		URL xslt = Thread.currentThread().getContextClassLoader().getResource(
				"cmllite-rules.xsl");
		/* set up to use saxon 9 */
		System.setProperty("javax.xml.transform.TransformerFactory",
				"net.sf.saxon.TransformerFactoryImpl");
		try {
			Document stylesheet = builder.build(xslt.openStream());
			return new XSLTransform(stylesheet);
		} catch (Exception e) {
			log.error(e);			
			throw new RuntimeException(e);
		}
	}

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
		Nodes failures = result.query("//*[local-name()='failed-assert' and namespace-uri()='http://purl.oclc.org/dsdl/svrl']");
		for (int index = 0, n = failures.size(); index < n; index ++) {
			Node node = failures.get(index);
			//log.error("\n"+node.toXML());
		}
		return failures.size() == 0;
	}

	/**
	 * Prints a XOM document to an OutputStream without having to remember the
	 * serializer voodoo. The encoding is always UTF-8.
	 * 
	 * @param doc
	 *            the XOM Document to print
	 * @param out
	 *            where to print to
	 */
	public static void print(Document doc, OutputStream out) {
		Serializer serializer;
		try {
			serializer = new Serializer(out, "UTF-8");
			serializer.setIndent(2);
			serializer.write(doc);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}

}
