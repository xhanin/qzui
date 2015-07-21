package qzui.domain;

public class HttpConfiguration {

    public static final String TRUST_ALL_CERTS = "http.ssl.trustAllCerts";
    public static final String TRUST_ALL_HOSTS = "http.ssl.trustAllHosts";

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
