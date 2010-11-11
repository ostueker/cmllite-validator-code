package org.xmlcml.www;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Serializer;
import nu.xom.xslt.XSLTransform;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Weerapong Phadungsukanan
 */
public abstract class AbstractValidator {

    private static Logger log = Logger.getLogger(AbstractValidator.class);
    public final static String CML_NS = "http://www.xml-cml.org/schema";

    /**
     * Validate a string of XML document.
     *
     * @param input a string of XML document.
     * @return true if valid, false otherwise.
     */
    public final boolean validate(String input) {
        return validate(buildDocumentFromString(input));
    }

    /**
     * Validate an XML file object.
     *
     * @param file an XML file object.
     * @return true if valid, false otherwise.
     */
    public final boolean validate(File file) {
        return validate(buildDocumentFromFile(file));
    }

    /**
     * Validate an XML from given input stream.
     *
     * @param ins an input stream of XML
     * @return true if valid, false otherwise.
     */
    public final boolean validate(InputStream ins) {
        return validate(buildDocumentFromInputStream(ins));
    }

    /**
     * Validate a XOM Document object.
     *
     * @param doc a XOM Document object.
     * @return true if valid, false otherwise.
     */
    public abstract boolean validate(Document doc);

    /**
     * Try building a XOM Document object from string without validation.
     *
     * @param input
     * @return null if fails else return a document
     */
    protected static Document buildDocumentFromString(String input) {
        return buildDocumentFromInputStream(IOUtils.toInputStream(input));
    }

    /**
     * Try building a XOM Document object from input stream without validation.
     *
     * @param ins input stream of XML document.
     * @return null if fails else return a document
     */
    protected static Document buildDocumentFromInputStream(InputStream ins) {
        Document doc = null;
        try {
            doc = new Builder().build(ins);
        } catch (Exception ex) {
            log.error(ex);
        }
        return doc;
    }

    /**
     * Try building a XOM Document object from file without validation.
     *
     * @param file
     * @return null if fails else return a document
     */
    protected static Document buildDocumentFromFile(File file) {
        Document doc = null;
        try {
            doc = new Builder().build(file);
        } catch (Exception ex) {
            log.error(ex);
        }
        return doc;
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
            serializer.setIndent(4);
            serializer.write(doc);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Prints a XOM document to an OutputStream without having to remember the
     * serializer voodoo. The encoding is always UTF-8.
     *
     * @param doc the XOM Document to print
     * @return
     */
    protected static InputStream print(Document doc) {
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
     * @param clzz Class where the resource is belong to.
     * @param xsltName
     * @return
     * @throws Exception
     */
    protected static XSLTransform createXSLTTransform(Class clzz, String xsltName) throws Exception {
        Builder builder = new Builder();
        URL xslt = clzz.getResource(xsltName);
        /* set up to use saxon 9 */
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        Document stylesheet = builder.build(xslt.openStream());
        return new XSLTransform(stylesheet);
    }
}
