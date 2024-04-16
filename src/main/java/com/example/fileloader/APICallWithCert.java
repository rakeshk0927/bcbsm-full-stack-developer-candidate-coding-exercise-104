package com.example.fileloader;

import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

public class ApiCaller {

    public static void main(String[] args) throws Exception {
        // Load certificate from string
        byte[] certBytes = certificateString.getBytes(StandardCharsets.UTF_8); // Replace certificateString with your certificate string
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream certInputStream = new ByteArrayInputStream(certBytes);
        X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(certInputStream);

        // Load private key from string
        byte[] keyBytes = privateKeyString.getBytes(StandardCharsets.UTF_8); // Replace privateKeyString with your private key string
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // Setup key store with certificate and private key
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null);
        keyStore.setCertificateEntry("certificate", certificate);
        keyStore.setKeyEntry("key", privateKey, password.toCharArray(), new Certificate[]{certificate});

        // Setup SSL context with key store
        SslContext sslContext = SslContextBuilder.forClient()
                .keyManager(keyStore, password) // Use the same password for the private key
                .build();

        // Setup HTTP client with SSL context
        HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

        // Setup Spring WebClient with custom HTTP client
        ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);
        WebClient webClient = WebClient.builder().clientConnector(httpConnector).build();

        // Make API call using WebClient
        String apiUrl = "https://api.example.com/endpoint"; // Your API endpoint
        String response = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }
}

