package org.xmlcml.www;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;

import org.apache.commons.io.FileUtils;
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

import com.thoughtworks.xstream.XStream;

public class QNameValidator {
	
	static Logger log = Logger.getLogger(QNameValidator.class);
	
	private boolean logging = false;

	private File reachableUriStorageFile = null;
	private File unreachableUriStorageFile = null;

	private UriData reachableUrisFromCurrentDocument = new UriData();
	private UriData unreachableUrisFromCurrentDocument = new UriData();

	private UriData allReachableUris = new UriData();
	private UriData allUnreachableUris = new UriData();

	public Collection<URI> getReachableUrisFromCurrentDocument() {
		return reachableUrisFromCurrentDocument.getUris();
	}

	public Collection<URI> getUnreachableUrisFromCurrentDocument() {
		return unreachableUrisFromCurrentDocument.getUris();
	}

	public Collection<URI> getAllReachableUris() {
		return allReachableUris.getUris();
	}

	public Collection<URI> getAllUnreachableUris() {
		return allUnreachableUris.getUris();
	}

	/**
	 * Create a QNameValidator which will not log
	 */
	public QNameValidator() {

	}

	/**
	 * Create a QNameValidatr which will log
	 * 
	 * @param reachableUriStoragePath
	 * @param unreachableUriStoragePath
	 */
	public QNameValidator(String reachableUriStoragePath,
			String unreachableUriStoragePath) {
		this.reachableUriStorageFile = new File(reachableUriStoragePath);
		this.unreachableUriStorageFile = new File(unreachableUriStoragePath);
		setupLogging();
	}

	private void setupLogging() {
		logging = true;
		if (!this.reachableUriStorageFile.exists()) {
			createFile(this.reachableUriStorageFile);
		}
		if (!this.unreachableUriStorageFile.exists()) {
			createFile(this.unreachableUriStorageFile);
		}

		XStream xstream = new XStream();
		String reachableXml;
		String unreachableXml;

		try {
			reachableXml = FileUtils
					.readFileToString(this.reachableUriStorageFile);
			unreachableXml = FileUtils
					.readFileToString(this.unreachableUriStorageFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if (reachableXml.length() > 0) {
			Collection<URI> reachableUrls = (Set<URI>) xstream
					.fromXML(reachableXml);
			this.allReachableUris.addAll(reachableUrls);
		}
		if (unreachableXml.length() > 0) {
			Collection<URI> unreachableUrls = (Set<URI>) xstream
					.fromXML(unreachableXml);
			this.allUnreachableUris.addAll(unreachableUrls);
		}
	}

	/**
	 * creates the file given as the argument
	 * 
	 * @param file
	 *            - the file to create
	 */
	private void createFile(File file) {
		try {
			FileUtils.touch(file);
		} catch (IOException e) {
			log.fatal("can't create file",e);
			throw new RuntimeException(e);
		}
	}

	public boolean validate(Document document) {
		cleanCurrentStorage();
		boolean valid = true;
		Nodes nodes = document
				.query("//*[namespace-uri()='http://www.xml-cml.org/schema']/@*[local-name()='dictRef' and namespace-uri()='']");
		for (int i = 0, n = nodes.size(); i < n; i++) {
			Attribute att = (Attribute) nodes.get(i);
			String[] a = att.getValue().split(":");
			Element parent = (Element) att.getParent();
			String namespace = parent.getNamespaceURI(a[0]);
			URI uri = createUri(namespace,a[1]);	
			if (QNameValidator.isReachable(uri)) {
				this.reachableUrisFromCurrentDocument.add(uri);
			} else {
				valid = false;
				this.unreachableUrisFromCurrentDocument.add(uri);				
			}
		}
		if (logging) {
			this.allReachableUris.addAll(this.reachableUrisFromCurrentDocument.getUris());
			this.allUnreachableUris.addAll(this.unreachableUrisFromCurrentDocument.getUris());
			XStream xStream = new XStream();
			try {
				FileUtils.writeStringToFile(this.reachableUriStorageFile, xStream.toXML(this.allReachableUris.getUris()));
			} catch (IOException e) {
				log.error("couldn't store reachable uris", e);				
			}
			try {
				FileUtils.writeStringToFile(this.unreachableUriStorageFile, xStream.toXML(this.allUnreachableUris.getUris()));
			} catch (IOException e) {
				log.error("couldn't store unreachable uris", e);				
			}
		}
		return valid;
	}
	
	private URI createUri(String namespace, String path) {
		if (!namespace.endsWith("/")) {
			namespace += "/";
		}
		try {
			return new URI(namespace + path);
		} catch (URISyntaxException e) {
			log.info("bad uri: "+namespace+path);
			log.info(e);
			return null;
		}
	}

	private void cleanCurrentStorage() {
		this.reachableUrisFromCurrentDocument.clear();
		this.unreachableUrisFromCurrentDocument.clear();		
	}


	// public String getData(URI uri) {
	// String data = "";
	// if (isReachable(uri)) {
	// HttpURLConnection.setFollowRedirects(false);
	// URL url = null;
	// try {
	// url = uri.toURL();
	// } catch (MalformedURLException e1) {
	// e1.printStackTrace();
	// return data;
	// }
	// HttpURLConnection connection = null;
	// try {
	// connection = (HttpURLConnection) url.openConnection();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// return data;
	// }
	// try {
	// connection.setRequestMethod("BODY");
	// } catch (ProtocolException e) {
	// e.printStackTrace();
	// return data;
	// }
	// BufferedInputStream buffer;
	// try {
	// if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
	// buffer = new BufferedInputStream(connection.getInputStream());
	//		            
	// StringBuilder builder = new StringBuilder();
	// int byteRead;
	// while ((byteRead = buffer.read()) != -1)
	// builder.append((char) byteRead);
	// buffer.close();
	// } else {
	// return data;
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// return data;
	// }
	// }
	// return data;
	//		
	// }

	public static Map<URI, Boolean> areReachable(Set<URI> uris) {
		Map<URI, Boolean> reachable = new HashMap<URI, Boolean>();
		Map<URI, Boolean> unreachable = new HashMap<URI, Boolean>();
		for (URI uri : uris) {
			if (isReachable(uri)) {
				reachable.put(uri, true);
			} else {
				unreachable.put(uri, false);
			}
		}
		Map<URI, Boolean> map = new HashMap<URI, Boolean>();
		map.putAll(reachable);
		map.putAll(unreachable);
		return map;
	}

	public static boolean isReachable(URI uri) {
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
