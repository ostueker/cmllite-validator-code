package org.xmlcml.www;

import nu.xom.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author jat45
 * @author Weerapong Phadungsukanan
 */

public class URIValidator {

    static Logger log = Logger.getLogger(URIValidator.class);
    static String xsdPrefix = "xsd";
    static Set<String> xsdDataTypes = new HashSet<String>(50);

    private String dummyNamespace = "http://www.xml-cml.org/dictionary/";
    private String dummyPath = "dummy";
    private URI dummy = createUri(dummyNamespace,dummyPath);

    static {
        xsdDataTypes.add("string");
        xsdDataTypes.add("boolean");
        xsdDataTypes.add("float");
        xsdDataTypes.add("double");
        xsdDataTypes.add("decimal");
        xsdDataTypes.add("duration");
        xsdDataTypes.add("dateTime");
        xsdDataTypes.add("time");
        xsdDataTypes.add("date");
        xsdDataTypes.add("gYearMonth");
        xsdDataTypes.add("gYear");
        xsdDataTypes.add("gMonthDay");
        xsdDataTypes.add("gDay");
        xsdDataTypes.add("gMonth");
        xsdDataTypes.add("hexBinary");
        xsdDataTypes.add("base64Binary");
        xsdDataTypes.add("anyURI");
        xsdDataTypes.add("QName");
        xsdDataTypes.add("NOTATION");
        xsdDataTypes.add("normalizedString");
        xsdDataTypes.add("token");
        xsdDataTypes.add("language");
        xsdDataTypes.add("IDREFS");
        xsdDataTypes.add("ENTITIES");
        xsdDataTypes.add("NMTOKEN");
        xsdDataTypes.add("NMTOKENS");
        xsdDataTypes.add("Name");
        xsdDataTypes.add("NCName");
        xsdDataTypes.add("ID");
        xsdDataTypes.add("IDREF");
        xsdDataTypes.add("ENTITY");
        xsdDataTypes.add("integer");
        xsdDataTypes.add("nonPositiveInteger");
        xsdDataTypes.add("negativeInteger");
        xsdDataTypes.add("long");
        xsdDataTypes.add("int");
        xsdDataTypes.add("short");
        xsdDataTypes.add("byte");
        xsdDataTypes.add("nonNegativeInteger");
        xsdDataTypes.add("unsignedLong");
        xsdDataTypes.add("unsignedInt");
        xsdDataTypes.add("unsignedShort");
        xsdDataTypes.add("unsignedByte");
        xsdDataTypes.add("positiveInteger");
    }

    private final HttpClient client;
    private final Builder builder;

    /**
     * Create a URIValidator
     */
    public URIValidator() {
        this(1500, 1500);
    }

    private URIValidator(int timeoutConnection, int timeoutSocket) {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        this.client = new DefaultHttpClient(httpParameters);
        this.builder = new Builder();
    }


    /**
     * Create a URIValidator
     */
    static URIValidator createForTest(int timeoutConnection, int socketConnection) {
        return new URIValidator(timeoutConnection,socketConnection);
    }

    public ValidationReport validate(Document document, ValidationReport report) {
        checkNamespaceAttributes(document, report);
        checkConventionAttributes(document, report);
        Nodes nodes = document.query("//*[namespace-uri()='" +
                CmlLiteValidator.CML_NS +
                "']/@*[namespace-uri()='' and (local-name()='dictRef' or local-name()='units' or local-name()='unitType' or local-name()='dataType')]");
        Map<URI, Set<String>> map = new HashMap<URI, Set<String>>(nodes.size());
        for (int i = 0, n = nodes.size(); i < n; i++) {
            Attribute att = (Attribute) nodes.get(i);
            String[] a = att.getValue().split(":");
            Element parent = (Element) att.getParent();
            String prefix = a[0];
            if (xsdPrefix.equals(prefix)) {
                if (!xsdDataTypes.contains(a[1])) {
                    report.addError("'" + a[1] + "' is not a valid xsd dataType");
                    report.setValidationResult(ValidationResult.INVALID);
                }
            } else {
                String namespace = parent.getNamespaceURI(prefix);
                if (null == namespace) {
                    report.addError("no namespace declared for the prefix: " + a[0]);
                    report.setValidationResult(ValidationResult.INVALID);
                } else if (dummyNamespace.equals(namespace) && dummyPath.equals(a[1])) {
                    // ignore the dummy dictRef
                } else {
                    URI uri = createUri(namespace);
                        if (map.containsKey(uri)) {
                            map.get(uri).add(a[1]);
                        } else {
                            Set<String> set = new HashSet<String>();
                            set.add(a[1]);
                            map.put(uri, set);
                        }

                }
            }
        }
        Map<URI, Set<String>> unreachableUris = findUnreachableURIs(map);
        if (!unreachableUris.isEmpty()) {
            if (ValidationResult.VALID.equals(report.getValidationResult())) {
                report.setValidationResult(ValidationResult.VALID_WITH_WARNINGS);
            }
            for (Map.Entry<URI, Set<String>> entry : unreachableUris.entrySet()) {
                for (String path : entry.getValue()) {
                    report.addWarning("the entry '" + path + "' was not reachable in the document found at " + entry.getKey().toString());
                }
            }
        }
        return report;
    }

    private void checkNamespaceAttributes(Document document, ValidationReport report) {
        Nodes namespaces = document.query("//*[namespace-uri()='" + CmlLiteValidator.CML_NS + "']/@*[namespace-uri()='' and local-name()='namespace']");
        List<Attribute> attributes = getAsAttributeList(namespaces);
        Set<URI> uniqueUris = getUniqueUris(attributes, report);
        uniqueUris.remove(dummy);
        checkUrisAreReachable(uniqueUris, report);
    }

    private void checkConventionAttributes(Document document, ValidationReport report) {
        Nodes conventions = document.query("//*[namespace-uri()='" + CmlLiteValidator.CML_NS + "']/@*[namespace-uri()='' and local-name()='convention']");
        List<Attribute> attributes = getAsAttributeList(conventions);
        Set<URI> uniqueUris = getUniqueUris(attributes, report);
        uniqueUris.remove(dummy);
        uniqueUris.removeAll(ConventionValidator.getSupportedConventions());
        checkUrisAreReachable(uniqueUris, report);
    }

    private List<Attribute> getAsAttributeList(Nodes nodes) {
        int n = nodes.size();
        List<Attribute> attributes = new ArrayList<Attribute>(n);
        for (int index = 0; index < n; index++) {
            Node node = nodes.get(index);
            if (node instanceof Attribute) {
                attributes.add((Attribute) node);
            } else {
                log.error("was expecting the nodes to all be attributes. found " + node.toXML());
            }
        }
        return attributes;
    }

    private Set<URI> getUniqueUris(List<Attribute> attributes, ValidationReport report) {
        int n = attributes.size();
        Set<URI> uniqueUris = new HashSet<URI>(n);
        for (Attribute attribute : attributes) {
            URI uri = null;
            if ("convention".equals(attribute.getLocalName())) {
                Element parent = (Element) attribute.getParent();
                String [] a = attribute.getValue().split(":");
                String prefix = a[0];
                String namespace = parent.getNamespaceURI(prefix);
                uri = createUri(namespace, a[1]);
            } else {
                uri = createUri(attribute.getValue());
            }

            if (uri != null) {
                uniqueUris.add(uri);
            } else {
                log.info("invalid uri " + uri.toString());
                report.addError("'" + uri.toString() + "' is not a valid uri");
                report.setValidationResult(ValidationResult.INVALID);
            }
        }
        return uniqueUris;
    }

    private void checkUrisAreReachable(Set<URI> uris, ValidationReport report) {
        for (URI uri : uris) {
            if (!isReachable(uri)) {
                report.addWarning(uri.toString() + " is not reachable. The server may be having problems - suggest you try again later");
                if (!ValidationResult.INVALID.equals(report.getValidationResult())) {
                    report.setValidationResult(ValidationResult.VALID_WITH_WARNINGS);
                }
            }
        }
    }


    private Map<URI, Set<String>> findUnreachableURIs(Map<URI, Set<String>> uris) {
        Map<URI, Set<String>> unreachableUris = new HashMap<URI, Set<String>>(uris.size());
        for (Map.Entry<URI, Set<String>> entry : uris.entrySet()) {
            Document document = getDocument(entry.getKey());
            if (document == null) {
                unreachableUris.put(entry.getKey(), entry.getValue());
            } else {
                Nodes nodes = document.query("//*[namespace-uri()='" +
                        CmlLiteValidator.CML_NS +
                        "']/@*[namespace-uri()='' and local-name()='id']");
                Set<String> ids = new HashSet(nodes.size());
                for (int i = 0, size = nodes.size(); i < size; i++) {
                    ids.add(nodes.get(i).getValue());
                }
                for (String id : entry.getValue()) {
                    if (!ids.contains(id)) {
                        if (unreachableUris.containsKey(entry.getKey())) {
                            unreachableUris.get(entry.getKey()).add(id);
                        } else {
                            Set<String> set = new HashSet<String>();
                            set.add(id);
                            unreachableUris.put(entry.getKey(), set);
                        }
                    }
                }
            }
        }
        return unreachableUris;
    }

    private URI createUri(String namespace, String path) {
        if (namespace.endsWith("/") || namespace.endsWith("#")) {
            // do nothing
        } else {
            namespace += "#";
        }

        try {
            return new URI(namespace + path);
        } catch (URISyntaxException e) {
            log.info("bad url: " + namespace + path);
            log.info(e);
            return null;
        }
    }

    private URI createUri(String possibleUri) {
        try {
            return new URI(possibleUri);
        } catch (URISyntaxException e) {
            log.info("bad uri: " + possibleUri + " " + e);
        } catch (Exception e) {
            log.info("bad uri: " + possibleUri + " " + e);
        }
        return null;
    }


    private Document getDocument(URI uri) {
        Document document = null;
        HttpGet request = new HttpGet(uri);
        try {
            HttpResponse response = client.execute(request);
            try {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    byte[] bytes = IOUtils.toByteArray(response.getEntity().getContent());
                    String s = new String(bytes);
                    s = s.replaceAll("<!DOCTYPE.*?>\\s*", "");
                    document = builder.build(new ByteArrayInputStream(s.getBytes("UTF-8")));
                }
            } catch (ParsingException e) {
                log.debug("couldn't build the document found at " + uri.toString() + " " + e);
            } finally {
                response.getEntity().consumeContent();
            }
        } catch (IOException e) {
            log.debug("couldn't get the document from " + uri.toString() + " " + e);
            return document;
        }
        return document;
    }

    private boolean isReachable(URI uri) {
        boolean isReachable = false;
        HttpHead head = new HttpHead(uri);
        try {
            HttpResponse response = client.execute(head);
            try {
                return (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
            } finally {
                if (response.getEntity() != null) {
                    try {
                        response.getEntity().consumeContent();
                    } catch (IOException e) {
                        log.warn("Error cleaning up connection", e);
                    }
                }
            }
        } catch (ClientProtocolException e) {
            log.debug(e);
        } catch (IOException e) {
            log.debug(e);
        }
        return isReachable;
    }
}
