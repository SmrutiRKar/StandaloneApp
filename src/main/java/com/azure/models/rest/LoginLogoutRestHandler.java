package com.azure.models.rest;

/*
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLSocketFactory;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
*/
import config.MyAppConfig;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Named;
import javax.net.ssl.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Created by kars2 on 3/2/16.
 */
@Named
@RestController
@EnableAutoConfiguration
@Path("/security")
public class LoginLogoutRestHandler {

    //@Path("/login")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String login() throws SecurityException {
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
                return (cookies.substring(0, cookies.indexOf(";")));
            else {
                //throw (new SecurityException("Login failure!!. No Jsession Id received", 202));
                System.out.println("Cookies is comming as null");
            }

        } catch (ResourceAccessException e) {
            //throw new SecurityException(e, HttpStatus.REQUEST_TIMEOUT);
            e.printStackTrace();
        } catch (HttpClientErrorException e) {
            //throw new SecurityException(e, e.getStatusCode());
            e.printStackTrace();
        } catch (Exception e) {
            //throw new SecurityException(e, e.getStatusCode());
            e.printStackTrace();
        }
        return "Not sucessful";
    }

    /*private void logout(String sessionId) {
        String uri = "https://10.31.252.122/j_spring_security_logout";
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        setFactory(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept:", "application/json");
        headers.add("Cookie:", sessionId);

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
    }*/

    private void setFactory(RestTemplate restTemplate) {
        //if (configuration.getActivationServerPath().contains("https://"))
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(getHttpClient()));
    }

    private DefaultHttpClient getHttpClient() {
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return false;
            }
        };

        SSLSocketFactory sf = null;
        try {
            sf = new SSLSocketFactory(acceptingTrustStrategy, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("https", 443, sf));
        ClientConnectionManager ccm = new PoolingClientConnectionManager(registry);

        return new DefaultHttpClient(ccm);
    }

}
