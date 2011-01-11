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
public class QNameValidator {

    static Logger log = Logger.getLogger(QNameValidator.class);

    private URI dummy = createUri("http://www.xml-cml.org/dictionary/", "dummy", null);
    /**
     * Create a QNameValidator which will not log
     */
    public QNameValidator() {
    }

    public ValidationReport validate(Document document) {
        ValidationReport report = new ValidationReport("qname-reachable-test");
        report.setValidationResult(ValidationResult.VALID);
        Nodes nodes = document.query("//*[namespace-uri()='" + CmlLiteValidator.CML_NS + "']/@*[namespace-uri()='' and (local-name()='dictRef' or local-name()='convention')]");
        Set<URI> uris = new HashSet<URI>(nodes.size());
        for (int i = 0, n = nodes.size(); i < n; i++) {
            Attribute att = (Attribute) nodes.get(i);
            String[] a = att.getValue().split(":");
            Element parent = (Element) att.getParent();
            String namespace = parent.getNamespaceURI(a[0]);
            if (null == namespace) {
                report.addError("no namespace declared for the prefix: " + a[0]);
                report.setValidationResult(ValidationResult.INVALID);
            } else {
                URI uri = createUri(namespace, a[1], report);
                uris.add(uri);
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
