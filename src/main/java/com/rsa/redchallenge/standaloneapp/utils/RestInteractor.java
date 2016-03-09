package com.rsa.redchallenge.standaloneapp.utils;

import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
import org.apache.catalina.util.URLEncoder;
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

import org.springframework.stereotype.Component;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anjana on 3/4/16.
 */

@Component
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
            T result = restTemplate.exchange(uri, HttpMethod.GET, request, response,getObjectParams(params)).getBody();
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

    private static Object[] getObjectParams(Map<String, Object> params) {
        //RestTemplate considers curly braces {...} in the given URL as a placeholder for URI variables and tries to replace them based on their name
        //hence passing the values as it gets replaced by {}
        if (params != null && !params.isEmpty()) {
            return params.values().toArray();
        }
        return new Object[0];
    }

//    public static void main(String[] args) throws Exception{
//        Map<String, Object> params =  new HashMap<>();
//        params.put("requestParams","{\"property\":\"priority\",\"value\":[\"CRITICAL\"]}");
//
//        String jsid = new LoginLogoutHelper().loginSA("admin","netwitness");
//              String path = "http://localhost:9191/ios/incidents/summary";
//        String respons = performGet(String.class,path,params,jsid);
//        System.out.println(respons);
//    }


    private static DefaultHttpClient getHttpClient() {

        if(ApplicationConstant.SA_BASE_URL.contains("https")) {
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
        } else {
            return new DefaultHttpClient();
        }
    }

    private static void setFactory(RestTemplate restTemplate) {
      //  if (configuration.getActivationServerPath().contains("https://"))
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(getHttpClient()));
    }


}
