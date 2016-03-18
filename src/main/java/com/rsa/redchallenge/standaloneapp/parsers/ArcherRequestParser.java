package com.rsa.redchallenge.standaloneapp.parsers;

import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;
import com.rsa.redchallenge.standaloneapp.model.Archer.RequestType;
import org.apache.commons.io.IOUtils;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vishwk on 3/18/2016.
 */
public class ArcherRequestParser {


    public static String soapDevicesAction = "http://archer-tech.com/webservices/ExecuteSearch";

    public static String getResponse(String request, String sessionId,RequestType requestType) throws Exception {

        String result = "";
        String url = ApplicationConstant.ARCHER_BASE_URL + "/ws/search.asmx", error = null;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("content-type", "text/xml; charset=utf-8");
        con.setRequestProperty("SOAPAction", soapDevicesAction);

        String newRequest = request.replaceAll("<sessionToken></sessionToken>",
                "<sessionToken>" + sessionId + "</sessionToken>");

        con.setDoOutput(true);
        OutputStream out = con.getOutputStream();
        Writer wout = new OutputStreamWriter(out);
        out.write(newRequest.getBytes());
        wout.flush();
        wout.close();

        if (con.getResponseCode() == 200) {
            result = IOUtils.toString(con.getInputStream(), "UTF-8");
            System.out.println(result);
        } else {
            error = IOUtils.toString(con.getErrorStream(), "UTF-8");
            System.out.println("\nArcher Response Error: " + error);
        }
        if (error == null) {
            result = result.replaceAll("&gt;", ">");
            result = result.replaceAll("&lt;", "<");
            result = result.substring(result.indexOf("<Records"),
                    result.indexOf("</Records>") + 10);

            return result;

        } else
            System.out.println("\nArcher Response Error: " + error);
        return null;
    }



    public boolean checkIfJsessionIdExpired(String result,String user) throws  Exception{
        return true;
    }
}
