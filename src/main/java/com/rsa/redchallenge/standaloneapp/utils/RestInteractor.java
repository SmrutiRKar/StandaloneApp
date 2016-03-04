package com.rsa.redchallenge.standaloneapp.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.web.client.HttpClientErrorException;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.util.UriComponentsBuilder;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.Map;

/**
 * Created by anjana on 3/4/16.
 */

public class RestInteractor {

    final static Log logger = LogFactory.getLog(RestInteractor.class);


    /**
     * Function to call GET interface for a REST server
     *
     * @param response Format in this the response of the call needs to be parsed
     * @param path     path of the interface in the REST server to be called
     * @param params   parameter for the call
     * @param <T>      template class according to which response needs to be parsed
     * @return returns the response in format specified
     * @throws RestClientException throws in case of error
     */
    public static <T> T performGet(Class<T> response, String path, Map<String, Object> params,String jsessionId) throws SecurityException {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        setFactory(restTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "RSA_SA_LICENSE=true; "+jsessionId);
        String uri = populatePath(params, path);
        logger.info("performing get request for uri:"+uri);
        HttpEntity<String> request = new HttpEntity<>(headers);
        try {
            T result = restTemplate.exchange(uri, HttpMethod.GET, request, response).getBody();
            return result;
        } catch (HttpClientErrorException e) {
            throw  e;
        }
    }


    /**
     * Function to call POST interface for a REST server
     *
     * @param path path of the interface in the REST server to be called
     * @return returns the response
     */
    public static String performPost(String path,  Map<String, Object> params,String jsessionId) throws SecurityException {

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        setFactory(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "RSA_SA_LICENSE=true; "+jsessionId);
        String uri = populatePath(params, path);
        logger.info("performing post request for uri:"+uri);
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            String result =restTemplate.exchange(uri, HttpMethod.GET, request, String.class).getBody();
            return result;
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Helper function to create the URL to be called
     *
     * @param params   parameter for the call
     * @param restPath path of the interface in the REST server to be called
     * @return URL to be called
     */
    private static String populatePath(Map<String, Object> params, String restPath) {
        String uri = null;
        if (params != null && !params.isEmpty()) {
            String url = restPath;
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

            for (Map.Entry<String, Object> param : params.entrySet())
                builder.queryParam(param.getKey(), param.getValue());

            uri = builder.build().toUriString();
        } else {
            uri = restPath;
        }

        return uri;
    }


    private static DefaultHttpClient getHttpClient() {
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

    private static void setFactory(RestTemplate restTemplate) {
      //  if (configuration.getActivationServerPath().contains("https://"))
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(getHttpClient()));
    }


}
