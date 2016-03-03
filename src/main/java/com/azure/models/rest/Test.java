package com.azure.models.rest;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import javax.net.ssl.SSLSocketFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kars2 on 3/3/16.
 */
public class Test {

    public static void main(String[] args) {
        simple();

    }

    public static void simple() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            URL url = new URL("https://10.31.252.122/login");
            URLConnection con = url.openConnection();
            Reader reader = new InputStreamReader(con.getInputStream());
            while (true) {
                int ch = reader.read();
                if (ch==-1) {
                    break;
                }
                System.out.print((char)ch);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void withoutRestTemplate(){
        try {
            URL url = new URL("https://10.31.252.122/login");
            URLConnection con = url.openConnection();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("j_username", "admin");
            map.add("j_password", "netwitness");
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, requestHeaders);


        }
        catch(Exception e) {

        }

    }

    public static void restTemplate(){
        try {

            trustSelfSignedSSL();

            URL url = new URL("https://10.31.252.122/login");
            URLConnection con = url.openConnection();
            HttpMessageConverter<MultiValueMap<String, ?>> formHttpMessageConverter = new FormHttpMessageConverter();
            HttpMessageConverter<String> stringHttpMessageConverter = new StringHttpMessageConverter();
            List<HttpMessageConverter<?>> messageConverters = new LinkedList<>();

            messageConverters.add(formHttpMessageConverter);
            messageConverters.add(stringHttpMessageConverter);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("j_username", "admin");
            map.add("j_password", "netwitness");

            String authURL = "https://10.31.252.122/j_spring_security_check";
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
            restTemplate.setMessageConverters(messageConverters);
            setFactory(restTemplate);

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, requestHeaders);

            ResponseEntity<String> result = restTemplate.exchange(authURL, HttpMethod.POST, entity, String.class);
            HttpHeaders respHeaders = result.getHeaders();

            String cookies = respHeaders.getFirst("Set-Cookie");
            if (cookies != null)
            System.out.println(cookies.substring(0, cookies.indexOf(";")));
            else {
                //throw (new SecurityException("Login failure!!. No Jsession Id received", 202));
                System.out.println("Cookies is comming as null");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void setFactory(RestTemplate restTemplate) {
        //if (configuration.getActivationServerPath().contains("https://"))
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(getHttpClient()));
    }

    private static DefaultHttpClient getHttpClient() {
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return false;
            }
        };

        org.apache.http.conn.ssl.SSLSocketFactory sf = null;
        try {
            sf = new org.apache.http.conn.ssl.SSLSocketFactory(acceptingTrustStrategy, org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("https", 443, sf));
        ClientConnectionManager ccm = new PoolingClientConnectionManager(registry);

        return new DefaultHttpClient(ccm);
    }

    public static void trustSelfSignedSSL() {
        try {
            String keystoreType = "JKS";
            InputStream keystoreLocation = null;
            char [] keystorePassword = null;
            char [] keyPassword = null;

            KeyStore keystore = KeyStore.getInstance(keystoreType);
            keystore.load(keystoreLocation, keystorePassword);
            KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmfactory.init(keystore, keyPassword);

            InputStream truststoreLocation = null;
            char [] truststorePassword = null;
            String truststoreType = "JKS";

            KeyStore truststore = KeyStore.getInstance(truststoreType);
            truststore.load(truststoreLocation, truststorePassword);
            TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            KeyManager [] keymanagers = kmfactory.getKeyManagers();
            TrustManager [] trustmanagers =  tmfactory.getTrustManagers();

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keymanagers, trustmanagers, new SecureRandom());
            SSLContext.setDefault(sslContext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
