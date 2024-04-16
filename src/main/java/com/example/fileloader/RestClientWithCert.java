package com.example.fileloader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

public class RestClientWithCert {

    public static void main(String[] args) throws Exception {
        // Load certificate and private key
        String certificateFilePath = "/path/to/your/certificate.crt";
        String privateKeyFilePath = "/path/to/your/privatekey.key";
        String passphrase = "your_passphrase";

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream certificateInputStream = new FileInputStream(certificateFilePath);
        FileInputStream privateKeyInputStream = new FileInputStream(privateKeyFilePath);
        keyStore.load(null, null); // Initialize empty keystore
        keyStore.setCertificateEntry("certificate", certificateInputStream);
        keyStore.setKeyEntry("privatekey", privateKeyInputStream, passphrase.toCharArray(), null);

        // Create and initialize SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, passphrase.toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        // Create HttpClient with custom SSLContext
        HttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();

        // Perform your HTTP requests using the HttpClient
        // For example:
        HttpGet httpGet = new HttpGet("https://api.example.com/resource");
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        System.out.println(responseString);
    }
}
