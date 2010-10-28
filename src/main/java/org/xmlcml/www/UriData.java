package org.xmlcml.www;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UriData {

    private Set<URI> uris = new HashSet<URI>();

    public synchronized Set<URI> getUris() {
        return new HashSet<URI>(this.uris);
    }

    synchronized void add(URI uri) {
        this.uris.add(uri);
    }

    synchronized void addAll(Collection<URI> uris) {
        this.uris.addAll(uris);
    }

    synchronized void clear() {
        this.uris.clear();
    }

}
