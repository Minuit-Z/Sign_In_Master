package com.zjmy.signin.utils.network;

/**
 * 临时跳过ssl验证
 */

import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSocketFactory;

public class BypassSSLCert {
    private SSLSocketFactory mDefaultSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
    private HostnameVerifier mDefaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();

    /* dummy TrustManager to trust all host, test only */
    private static X509TrustManager getDummyTrustManager() {
        return new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        };
    }

    /* by pass SSL connection error to localhost */
    public void bypassLocalhostSSL(boolean enable) throws Exception {
        HostnameVerifier hostnameVerifier;
        SSLSocketFactory sslSocketFactory;
        if (enable) {
            TrustManager[] tma = new TrustManager[] {getDummyTrustManager()};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, tma, new java.security.SecureRandom());
            sslSocketFactory = sc.getSocketFactory();
            hostnameVerifier = (String hostname, SSLSession session)-> true;
        } else {
            sslSocketFactory = mDefaultSSLSocketFactory;
            hostnameVerifier = mDefaultHostnameVerifier;
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    }
}
