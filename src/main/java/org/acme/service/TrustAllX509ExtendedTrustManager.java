package org.acme.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TrustAllX509ExtendedTrustManager extends X509ExtendedTrustManager {

    private static final Logger log = LoggerFactory.getLogger(TrustAllX509ExtendedTrustManager.class);

    private static final X509ExtendedTrustManager INSTANCE = new TrustAllX509ExtendedTrustManager();
    private static final String CERTIFICATE_LOG_MESSAGE = "Accepting the following {} certificates without validating: {}";
    private static final X509Certificate[] EMPTY_CERTIFICATES = new X509Certificate[0];

    private TrustAllX509ExtendedTrustManager() {
    }

    public static X509ExtendedTrustManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certificates, String authType) {
        logCertificate(certificates, "client");
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certificates, String authType, Socket socket) {
        logCertificate(certificates, "client");
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certificates, String authType, SSLEngine sslEngine) {
        logCertificate(certificates, "client");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certificates, String authType) {
        logCertificate(certificates, "server");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certificates, String authType, Socket socket) {
        logCertificate(certificates, "server");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certificates, String authType, SSLEngine sslEngine) {
        logCertificate(certificates, "server");
    }

    private static void logCertificate(X509Certificate[] certificates, String serverOrClient) {
        String principals = extractPrincipals(certificates);
        log.info(CERTIFICATE_LOG_MESSAGE, serverOrClient, principals);
    }

    private static String extractPrincipals(X509Certificate[] certificates) {
        return Arrays.stream(certificates)
                .map(X509Certificate::getSubjectX500Principal)
                .map(Principal::toString)
                .map(principal -> String.format("{%s}", principal))
                .collect(Collectors.joining(",", "[", "]"));
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return EMPTY_CERTIFICATES;
    }
}
