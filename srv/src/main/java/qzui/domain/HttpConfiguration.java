package qzui.domain;

public class HttpConfiguration {

    public static final String TRUST_ALL_CERTS_PROPERTY = "http.ssl.trustAllCerts";
    public static final String TRUST_ALL_HOSTS_PROPERTY = "http.ssl.trustAllHosts";

    public static final String TRUST_ALL_CERTS_FIELD = "httpSSLTrustAllCerts";
    public static final String TRUST_ALL_HOSTS_FIELD = "httpSSLTrustAllHosts";

    private boolean trustAllCerts;

    private boolean trustAllHosts;

    public boolean isTrustAllCerts() {
        return trustAllCerts;
    }

    public boolean isTrustAllHosts() {
        return trustAllHosts;
    }

    public HttpConfiguration setTrustAllCerts(final boolean trustAllCerts) {
        this.trustAllCerts = trustAllCerts;
        return this;
    }

    public HttpConfiguration setTrustAllHosts(final boolean trustAllHosts) {
        this.trustAllHosts = trustAllHosts;
        return this;
    }
}
