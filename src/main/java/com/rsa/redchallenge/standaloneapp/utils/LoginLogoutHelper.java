package com.rsa.redchallenge.standaloneapp.utils;

import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by kars2 on 3/2/16.
 */
@Component
public class LoginLogoutHelper {


        final static Log logger = LogFactory.getLog(LoginLogoutHelper.class);

        public String loginSA(String usn,String pwd) throws Exception {

        try {
            HttpMessageConverter<MultiValueMap<String, ?>> formHttpMessageConverter = new FormHttpMessageConverter();
            HttpMessageConverter<String> stringHttpMessageConverter = new StringHttpMessageConverter();
            List<HttpMessageConverter<?>> messageConverters = new LinkedList<>();

            messageConverters.add(formHttpMessageConverter);
            messageConverters.add(stringHttpMessageConverter);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("j_username", usn);
            map.add("j_password", pwd);

            String authURL = ApplicationConstant.SA_BASE_URL+ "j_spring_security_check";
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
                throw (new Exception("Login failure!!. No Jsession Id received"+ HttpStatus.UNAUTHORIZED));
            }

        } catch (ResourceAccessException e) {
            throw e;
        } catch (HttpClientErrorException e) {
            throw e;
        }
    }

    public void logoutSA(String sessionId) {
        String uri = ApplicationConstant.SA_BASE_URL+ "j_spring_security_logout";
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        setFactory(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept:", "application/json");
        headers.add("Cookie:", sessionId);

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
    }

//    public static void main(String[] args) throws Exception {
//        System.out.println("trying to login..");
//        String saUsn = "admin";
//        String saPwd = "netwitness";
//      //  System.out.println("login result ="+new LoginLogoutHelper().loginSA(saUsn,saPwd));
//       // System.out.println("performing logout..");
//        //new LoginLogoutHelper().logoutSA("JSESSIONID=xcn0miep3t84idmxxmb79ex1");
//
//        String jId = new LoginLogoutHelper().loginSA("admin","netwitness");
//        RestInteractor restInteractor =  new RestInteractor();
//        String response =  restInteractor.performGet(String.class,"https://10.31.252.122/ajax/incident/77/INC-2112?_dc=1457076976235&id=INC-2112",null,jId);
//        System.out.println("received reponse from SA server:"+response);
//
//    }

    private  void setFactory(RestTemplate restTemplate) {
        //if (configuration.getActivationServerPath().contains("https://"))
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(getHttpClient()));
    }

    private  DefaultHttpClient getHttpClient() {
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
