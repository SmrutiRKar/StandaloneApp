package com.rsa.redchallenge.standaloneapp.parsers;


import com.rsa.redchallenge.standaloneapp.constants.ApplicationConstant;

import com.rsa.redchallenge.standaloneapp.model.Archer.RequestType;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kars2 on 3/4/16.
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ArcherParser {

    private static final Log log = LogFactory.getLog(ArcherParser.class);

    @Autowired
    RequestType requestType;

    @Autowired
    public static XmlParser xmlParser;

    public static String soapDevicesAction = "http://archer-tech.com/webservices/ExecuteSearch";
   // public static String result;
    public static String riskRequest = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ExecuteSearch xmlns=\"http://archer-tech.com/webservices/\"> "
            + "<sessionToken></sessionToken>" + "<searchOptions>"
            // +
            // "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;8177&lt;/DisplayField&gt;&lt;DisplayField&gt;23499&lt;/DisplayField&gt;&lt;DisplayField&gt;2751&lt;/DisplayField&gt;&lt;DisplayField&gt;8728&lt;/DisplayField&gt;&lt;DisplayField&gt;23685&lt;/DisplayField&gt;&lt;DisplayField&gt;23390&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;71&lt;/Module&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;588&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildDown&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;594&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildUp&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;/Children&gt;&lt;/Children&gt;&lt;Filter&gt;&lt;Conditions&gt;&lt;TextFilterCondition&gt;&lt;Operator&gt;Contains&lt;/Operator&gt;&lt;Field&gt;120&lt;/Field&gt;&lt;Value&gt;mysql.rtp2fa.local&lt;/Value&gt;&lt;/TextFilterCondition&gt;&lt;/Conditions&gt;&lt;/Filter&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;10082&lt;/DisplayField&gt;&lt;DisplayField&gt;10830&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;PageSize&gt;100&lt;/PageSize&gt;&lt;Criteria&gt;&lt;Filter&gt;&lt;Conditions&gt;&lt;ValueListFilterCondition&gt;&lt;Field&gt;10139&lt;/Field&gt;&lt;Operator&gt;Equals&lt;/Operator&gt;&lt;IsNoSelectionIncluded&gt;False&lt;/IsNoSelectionIncluded&gt;&lt;IncludeChildren&gt;False&lt;/IncludeChildren&gt;&lt;Values&gt;&lt;Value&gt;155&lt;/Value&gt;&lt;/Values&gt;&lt;/ValueListFilterCondition&gt;&lt;/Conditions&gt;&lt;/Filter&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;338&lt;/Module&gt;&lt;IsKeywordModule&gt;True&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;SortFields&gt;&lt;SortField&gt;&lt;Field&gt;10082&lt;/Field&gt;&lt;SortType&gt;Ascending&lt;/SortType&gt;&lt;/SortField&gt;&lt;/SortFields&gt;&lt;/ModuleCriteria&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "</searchOptions>"
            + "<pageNumber>1</pageNumber>	    </ExecuteSearch>	  </soap:Body>	</soap:Envelope>";
    public static String lossRequest = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ExecuteSearch xmlns=\"http://archer-tech.com/webservices/\"> "
            + "<sessionToken></sessionToken>" + "<searchOptions>"
            // +
            // "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;8177&lt;/DisplayField&gt;&lt;DisplayField&gt;23499&lt;/DisplayField&gt;&lt;DisplayField&gt;2751&lt;/DisplayField&gt;&lt;DisplayField&gt;8728&lt;/DisplayField&gt;&lt;DisplayField&gt;23685&lt;/DisplayField&gt;&lt;DisplayField&gt;23390&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;71&lt;/Module&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;588&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildDown&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;594&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildUp&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;/Children&gt;&lt;/Children&gt;&lt;Filter&gt;&lt;Conditions&gt;&lt;TextFilterCondition&gt;&lt;Operator&gt;Contains&lt;/Operator&gt;&lt;Field&gt;120&lt;/Field&gt;&lt;Value&gt;mysql.rtp2fa.local&lt;/Value&gt;&lt;/TextFilterCondition&gt;&lt;/Conditions&gt;&lt;/Filter&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;10082&lt;/DisplayField&gt;&lt;DisplayField&gt;10114&lt;/DisplayField&gt;&lt;DisplayField&gt;10126&lt;/DisplayField&gt;&lt;DisplayField&gt;10106&lt;/DisplayField&gt;&lt;DisplayField&gt;10125&lt;/DisplayField&gt;&lt;DisplayField&gt;10083&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;PageSize&gt;50&lt;/PageSize&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;338&lt;/Module&gt;&lt;IsKeywordModule&gt;True&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;SortFields&gt;&lt;SortField&gt;&lt;Field&gt;10082&lt;/Field&gt;&lt;SortType&gt;Ascending&lt;/SortType&gt;&lt;/SortField&gt;&lt;/SortFields&gt;&lt;/ModuleCriteria&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "</searchOptions>"
            + "<pageNumber>1</pageNumber>	    </ExecuteSearch>	  </soap:Body>	</soap:Envelope>";
    public static String complianceRequest = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ExecuteSearch xmlns=\"http://archer-tech.com/webservices/\"> "
            + "<sessionToken></sessionToken>" + "<searchOptions>"
            // +
            // "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;8177&lt;/DisplayField&gt;&lt;DisplayField&gt;23499&lt;/DisplayField&gt;&lt;DisplayField&gt;2751&lt;/DisplayField&gt;&lt;DisplayField&gt;8728&lt;/DisplayField&gt;&lt;DisplayField&gt;23685&lt;/DisplayField&gt;&lt;DisplayField&gt;23390&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;71&lt;/Module&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;588&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildDown&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;594&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildUp&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;/Children&gt;&lt;/Children&gt;&lt;Filter&gt;&lt;Conditions&gt;&lt;TextFilterCondition&gt;&lt;Operator&gt;Contains&lt;/Operator&gt;&lt;Field&gt;120&lt;/Field&gt;&lt;Value&gt;mysql.rtp2fa.local&lt;/Value&gt;&lt;/TextFilterCondition&gt;&lt;/Conditions&gt;&lt;/Filter&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;10082&lt;/DisplayField&gt;&lt;DisplayField&gt;10830&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;PageSize&gt;100&lt;/PageSize&gt;&lt;Criteria&gt;&lt;Filter&gt;&lt;Conditions&gt;&lt;ValueListFilterCondition&gt;&lt;Field&gt;10139&lt;/Field&gt;&lt;Operator&gt;Equals&lt;/Operator&gt;&lt;IsNoSelectionIncluded&gt;False&lt;/IsNoSelectionIncluded&gt;&lt;IncludeChildren&gt;False&lt;/IncludeChildren&gt;&lt;Values&gt;&lt;Value&gt;155&lt;/Value&gt;&lt;/Values&gt;&lt;/ValueListFilterCondition&gt;&lt;/Conditions&gt;&lt;/Filter&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;338&lt;/Module&gt;&lt;IsKeywordModule&gt;True&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;SortFields&gt;&lt;SortField&gt;&lt;Field&gt;10082&lt;/Field&gt;&lt;SortType&gt;Ascending&lt;/SortType&gt;&lt;/SortField&gt;&lt;/SortFields&gt;&lt;/ModuleCriteria&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "</searchOptions>"
            + "<pageNumber>1</pageNumber>	    </ExecuteSearch>	  </soap:Body>	</soap:Envelope>";
    public static String threatRequest = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ExecuteSearch xmlns=\"http://archer-tech.com/webservices/\"> "
            + "<sessionToken></sessionToken>" + "<searchOptions>"
            // +
            // "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;8177&lt;/DisplayField&gt;&lt;DisplayField&gt;23499&lt;/DisplayField&gt;&lt;DisplayField&gt;2751&lt;/DisplayField&gt;&lt;DisplayField&gt;8728&lt;/DisplayField&gt;&lt;DisplayField&gt;23685&lt;/DisplayField&gt;&lt;DisplayField&gt;23390&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;71&lt;/Module&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;588&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildDown&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;594&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildUp&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;/Children&gt;&lt;/Children&gt;&lt;Filter&gt;&lt;Conditions&gt;&lt;TextFilterCondition&gt;&lt;Operator&gt;Contains&lt;/Operator&gt;&lt;Field&gt;120&lt;/Field&gt;&lt;Value&gt;mysql.rtp2fa.local&lt;/Value&gt;&lt;/TextFilterCondition&gt;&lt;/Conditions&gt;&lt;/Filter&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;10082&lt;/DisplayField&gt;&lt;DisplayField&gt;10114&lt;/DisplayField&gt;&lt;DisplayField&gt;10126&lt;/DisplayField&gt;&lt;DisplayField&gt;10106&lt;/DisplayField&gt;&lt;DisplayField&gt;10125&lt;/DisplayField&gt;&lt;DisplayField&gt;10083&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;PageSize&gt;50&lt;/PageSize&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;338&lt;/Module&gt;&lt;IsKeywordModule&gt;True&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;SortFields&gt;&lt;SortField&gt;&lt;Field&gt;10082&lt;/Field&gt;&lt;SortType&gt;Ascending&lt;/SortType&gt;&lt;/SortField&gt;&lt;/SortFields&gt;&lt;/ModuleCriteria&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "</searchOptions>"
            + "<pageNumber>1</pageNumber>	    </ExecuteSearch>	  </soap:Body>	</soap:Envelope>";

    static String devicexpathArray = new String("/Records");

    public String getDashboardReports(String sessionId, String user) throws Exception {
        String lossResult =  getResponse(lossRequest, sessionId,requestType.LOSSREQUEST);
       // getDevicesContext(complianceRequest, sessionId);
      //  getDevicesContext(riskRequest, sessionId);
        String riskResult = getResponse(riskRequest, sessionId,requestType.RISKREQUEST);
        return lossResult + riskResult;
    }

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

            return xmlParser.getxpathDetails(devicexpathArray, result, requestType);

        } else
            System.out.println("\nArcher Response Error: " + error);
        return null;
    }



    public boolean checkIfJsessionIdExpired(String result,String user) throws  Exception{
       return true;
    }




}