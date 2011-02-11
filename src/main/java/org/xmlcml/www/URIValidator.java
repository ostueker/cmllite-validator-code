package org.xmlcml.www;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jat45
 * @author Weerapong Phadungsukanan
 */

public class URIValidator {

    static Logger log = Logger.getLogger(URIValidator.class);
    static String xsdPrefix = "xsd";
    static Set<String> xsdDataTypes = new HashSet<String>(50);

    private URI dummy = createUri("http://www.xml-cml.org/dictionary/", "dummy", null);

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

    /**
     * Create a URIValidator
     */
    public URIValidator() {
    }

    // we currently check dictRef and convention attributes but we need to add
    // namespace attribute, units, unitType

    public ValidationReport validate(Document document, ValidationReport report) {
        checkNamespaceAttributes(document, report);

        Nodes nodes = document.query("//*[namespace-uri()='" +
                CmlLiteValidator.CML_NS +
                "']/@*[namespace-uri()='' and (local-name()='dictRef' or local-name()='convention' or local-name()='units' or local-name()='unitType' or local-name()='dataType')]");
        Set<URI> uris = new HashSet<URI>(nodes.size());
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

                } else {
                    URI uri = createUri(namespace, a[1], report);
                    uris.add(uri);
                }
            }
        }
        Set<URI> unreachableUris = findUnreachableURIs(uris);
        if (!unreachableUris.isEmpty()) {
            if (ValidationResult.VALID.equals(report.getValidationResult())) {
                report.setValidationResult(ValidationResult.VALID_WITH_WARNINGS);
            }
            for (URI uri : unreachableUris) {
                report.addWarning(uri.toString() + " is not reachable");
            }
        }
        return report;
    }

    private void checkNamespaceAttributes(Document document, ValidationReport report) {
        Nodes namespaces = document.query("//*[namespace-uri()='" + CmlLiteValidator.CML_NS + "']/@*[namespace-uri()='' and local-name()='namespace']");
        Set<URI> uniqueUris = new HashSet<URI>(namespaces.size());
        for (int index = 0, size = namespaces.size(); index < size; index++) {
            String possibleUri = namespaces.get(index).getValue();
            try {
                URI uri = new URI(possibleUri);
                uniqueUris.add(uri);
            } catch (URISyntaxException e) {
                log.info("invalid uri " + possibleUri + " " + e);
                report.addError("'" + possibleUri + "' is not a valid uri");
                report.setValidationResult(ValidationResult.INVALID);
            } catch (Exception e) {
                log.info("unexpected exception " + possibleUri + " " + e);
                report.addError("'" + possibleUri + "' is not a valid uri and caused an unexpected exception");
                report.setValidationResult(ValidationResult.INVALID);
            }
        }
        for (URI uri : uniqueUris) {
            if (!isReachable(uri)) {
                report.addWarning(uri.toString() + " is not reachable");
                if (!ValidationResult.INVALID.equals(report.getValidationResult())) {
                    report.setValidationResult(ValidationResult.VALID_WITH_WARNINGS);
                }
            }
        }
    }

    private Set<URI> findUnreachableURIs(Set<URI> uris) {
        Set<URI> reachableURIs = new HashSet<URI>(uris.size());
        reachableURIs.add(dummy);
        Set<URI> unreachableURIs = new HashSet<URI>(uris.size());
        for (URI uri : uris) {
            if (reachableURIs.contains(uri)) {
                // do nothing
            } else {
                if (unreachableURIs.contains(uri)) {
                    // do nothing
                } else {
                    if (isReachable(uri)) {
                        reachableURIs.add(uri);
                    } else {
                        unreachableURIs.add(uri);
                    }
                }
            }
        }
        return unreachableURIs;
    }

    private URI createUri(String namespace, String path, ValidationReport report) {
        if (!namespace.endsWith("/")) {
            namespace += "/";
        }
        try {
            return new URI(namespace + path);
        } catch (URISyntaxException e) {
            log.info("bad uri: " + namespace + path);
            log.info(e);
            return null;
        }
    }

    private boolean isReachable(URI uri) {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = 500;
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 500;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient client = new DefaultHttpClient(httpParameters);
        HttpHead head = new HttpHead(uri);
        try {
            HttpResponse response = client.execute(head);
            return (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
        } catch (ClientProtocolException e) {
            log.debug(e);
            return false;
        } catch (IOException e) {
            log.debug(e);
            return false;
        }
    }
}
