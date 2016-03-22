package com.rsa.redchallenge.standaloneapp.parsers;

import com.rsa.redchallenge.standaloneapp.model.Archer.OpenRiskDetails;
import com.rsa.redchallenge.standaloneapp.model.Archer.RequestType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by vishwk on 3/17/2016.
 */
public class ArcherDDownParser {


    static ArcherRequestParser requestParser;
    static XmlParser xmlParser;

    public static String riskDDownRequest = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ExecuteSearch xmlns=\"http://archer-tech.com/webservices/\"> "
            + "<sessionToken></sessionToken>" + "<searchOptions>"
            // +
            // "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;8177&lt;/DisplayField&gt;&lt;DisplayField&gt;23499&lt;/DisplayField&gt;&lt;DisplayField&gt;2751&lt;/DisplayField&gt;&lt;DisplayField&gt;8728&lt;/DisplayField&gt;&lt;DisplayField&gt;23685&lt;/DisplayField&gt;&lt;DisplayField&gt;23390&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;71&lt;/Module&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;588&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildDown&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;594&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildUp&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;/Children&gt;&lt;/Children&gt;&lt;Filter&gt;&lt;Conditions&gt;&lt;TextFilterCondition&gt;&lt;Operator&gt;Contains&lt;/Operator&gt;&lt;Field&gt;120&lt;/Field&gt;&lt;Value&gt;mysql.rtp2fa.local&lt;/Value&gt;&lt;/TextFilterCondition&gt;&lt;/Conditions&gt;&lt;/Filter&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;10082&lt;/DisplayField&gt;&lt;DisplayField&gt;10114&lt;/DisplayField&gt;&lt;DisplayField&gt;10126&lt;/DisplayField&gt;&lt;DisplayField&gt;10106&lt;/DisplayField&gt;&lt;DisplayField&gt;10125&lt;/DisplayField&gt;&lt;DisplayField&gt;10083&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;PageSize&gt;50&lt;/PageSize&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;338&lt;/Module&gt;&lt;IsKeywordModule&gt;True&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;SortFields&gt;&lt;SortField&gt;&lt;Field&gt;10082&lt;/Field&gt;&lt;SortType&gt;Ascending&lt;/SortType&gt;&lt;/SortField&gt;&lt;/SortFields&gt;&lt;/ModuleCriteria&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "</searchOptions>"
            + "<pageNumber>1</pageNumber>	    </ExecuteSearch>	  </soap:Body>	</soap:Envelope>";
    public static String lossDDownRequest = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ExecuteSearch xmlns=\"http://archer-tech.com/webservices/\"> "
            + "<sessionToken></sessionToken>" + "<searchOptions>"
            // +
            // "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;8177&lt;/DisplayField&gt;&lt;DisplayField&gt;23499&lt;/DisplayField&gt;&lt;DisplayField&gt;2751&lt;/DisplayField&gt;&lt;DisplayField&gt;8728&lt;/DisplayField&gt;&lt;DisplayField&gt;23685&lt;/DisplayField&gt;&lt;DisplayField&gt;23390&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;71&lt;/Module&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;588&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildDown&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;594&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildUp&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;/Children&gt;&lt;/Children&gt;&lt;Filter&gt;&lt;Conditions&gt;&lt;TextFilterCondition&gt;&lt;Operator&gt;Contains&lt;/Operator&gt;&lt;Field&gt;120&lt;/Field&gt;&lt;Value&gt;mysql.rtp2fa.local&lt;/Value&gt;&lt;/TextFilterCondition&gt;&lt;/Conditions&gt;&lt;/Filter&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;5048&lt;/DisplayField&gt;&lt;DisplayField&gt;17239&lt;/DisplayField&gt;&lt;DisplayField&gt;17233&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;PageSize&gt;50&lt;/PageSize&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;250&lt;/Module&gt;&lt;IsKeywordModule&gt;True&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;SortFields&gt;&lt;SortField&gt;&lt;Field&gt;4505&lt;/Field&gt;&lt;SortType&gt;Ascending&lt;/SortType&gt;&lt;/SortField&gt;&lt;/SortFields&gt;&lt;/ModuleCriteria&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "</searchOptions>"
            + "<pageNumber>1</pageNumber>	    </ExecuteSearch>	  </soap:Body>	</soap:Envelope>";
    public static String complianceDDownRequest = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ExecuteSearch xmlns=\"http://archer-tech.com/webservices/\"> "
            + "<sessionToken></sessionToken>" + "<searchOptions>"
            // +
            // "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;8177&lt;/DisplayField&gt;&lt;DisplayField&gt;23499&lt;/DisplayField&gt;&lt;DisplayField&gt;2751&lt;/DisplayField&gt;&lt;DisplayField&gt;8728&lt;/DisplayField&gt;&lt;DisplayField&gt;23685&lt;/DisplayField&gt;&lt;DisplayField&gt;23390&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;71&lt;/Module&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;588&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildDown&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;594&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildUp&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;/Children&gt;&lt;/Children&gt;&lt;Filter&gt;&lt;Conditions&gt;&lt;TextFilterCondition&gt;&lt;Operator&gt;Contains&lt;/Operator&gt;&lt;Field&gt;120&lt;/Field&gt;&lt;Value&gt;mysql.rtp2fa.local&lt;/Value&gt;&lt;/TextFilterCondition&gt;&lt;/Conditions&gt;&lt;/Filter&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;2492&lt;/DisplayField&gt;&lt;DisplayField&gt;3048&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;PageSize&gt;50&lt;/PageSize&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;149&lt;/Module&gt;&lt;IsKeywordModule&gt;True&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;SortFields&gt;&lt;SortField&gt;&lt;Field&gt;2492&lt;/Field&gt;&lt;SortType&gt;Ascending&lt;/SortType&gt;&lt;/SortField&gt;&lt;/SortFields&gt;&lt;/ModuleCriteria&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "</searchOptions>"
            + "<pageNumber>1</pageNumber>	    </ExecuteSearch>	  </soap:Body>	</soap:Envelope>";
    public static String threatDDownRequest = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ExecuteSearch xmlns=\"http://archer-tech.com/webservices/\"> "
            + "<sessionToken></sessionToken>" + "<searchOptions>"
            // +
            // "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;8177&lt;/DisplayField&gt;&lt;DisplayField&gt;23499&lt;/DisplayField&gt;&lt;DisplayField&gt;2751&lt;/DisplayField&gt;&lt;DisplayField&gt;8728&lt;/DisplayField&gt;&lt;DisplayField&gt;23685&lt;/DisplayField&gt;&lt;DisplayField&gt;23390&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;71&lt;/Module&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;588&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildDown&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;Children&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;594&lt;/Module&gt;&lt;IsKeywordModule&gt;false&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;LeveledBuildoutOptions&gt;BuildUp&lt;/LeveledBuildoutOptions&gt;&lt;/ModuleCriteria&gt;&lt;/Children&gt;&lt;/Children&gt;&lt;Filter&gt;&lt;Conditions&gt;&lt;TextFilterCondition&gt;&lt;Operator&gt;Contains&lt;/Operator&gt;&lt;Field&gt;120&lt;/Field&gt;&lt;Value&gt;mysql.rtp2fa.local&lt;/Value&gt;&lt;/TextFilterCondition&gt;&lt;/Conditions&gt;&lt;/Filter&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "&lt;SearchReport&gt;&lt;DisplayFields&gt;&lt;DisplayField&gt;10082&lt;/DisplayField&gt;&lt;DisplayField&gt;10114&lt;/DisplayField&gt;&lt;DisplayField&gt;10126&lt;/DisplayField&gt;&lt;DisplayField&gt;10106&lt;/DisplayField&gt;&lt;DisplayField&gt;10125&lt;/DisplayField&gt;&lt;DisplayField&gt;10083&lt;/DisplayField&gt;&lt;/DisplayFields&gt;&lt;PageSize&gt;50&lt;/PageSize&gt;&lt;Criteria&gt;&lt;ModuleCriteria&gt;&lt;Module&gt;338&lt;/Module&gt;&lt;IsKeywordModule&gt;True&lt;/IsKeywordModule&gt;&lt;BuildoutRelationship&gt;Union&lt;/BuildoutRelationship&gt;&lt;SortFields&gt;&lt;SortField&gt;&lt;Field&gt;10082&lt;/Field&gt;&lt;SortType&gt;Ascending&lt;/SortType&gt;&lt;/SortField&gt;&lt;/SortFields&gt;&lt;/ModuleCriteria&gt;&lt;/Criteria&gt;&lt;/SearchReport&gt;"
            + "</searchOptions>"
            + "<pageNumber>1</pageNumber>	    </ExecuteSearch>	  </soap:Body>	</soap:Envelope>";

    public static String getLossDDown(String requestParams, String sessionId, String requestUser) throws Exception{
        String lossResult =  requestParser.getResponse(riskDDownRequest, sessionId, RequestType.LOSSDRILLDOWNREQUEST);
        xmlParser.getxpathDetails(lossResult, RequestType.LOSSDRILLDOWNREQUEST);
        xmlParser.findLossDrillDown(requestParams);
        return XmlParser.writeListToJsonArray(RequestType.LOSSDRILLDOWNREQUEST);
    }

    public static String getRiskDDown(String requestParams, String sessionId, String requestUser) throws Exception{
        String riskResult =  requestParser.getResponse(riskDDownRequest, sessionId, RequestType.RISKDRILLDOWNREQUEST);
        xmlParser.getxpathDetails(riskResult, RequestType.RISKDRILLDOWNREQUEST);
        xmlParser.findRiskDrillDown(requestParams);
        return XmlParser.writeListToJsonArray(RequestType.RISKDRILLDOWNREQUEST);
    }

    public static String getComplianceDDown(String requestParams, String sessionId, String requestUser) throws Exception{
        String complianceResult =  requestParser.getResponse(riskDDownRequest, sessionId, RequestType.COMPLIANCEDRILLDOWNREQUEST);
        xmlParser.getxpathDetails(complianceResult, RequestType.COMPLIANCEDRILLDOWNREQUEST);
        xmlParser.findComplianceDrillDown(requestParams);
        return XmlParser.writeListToJsonArray(RequestType.COMPLIANCEDRILLDOWNREQUEST);
    }

    public static String getThreatDDown(String requestParams, String sessionId, String requestUser) throws Exception{
        String threatResult =  requestParser.getResponse(riskDDownRequest, sessionId, RequestType.THREATDRILLDOWNREQUEST);
        xmlParser.getxpathDetails(threatResult, RequestType.THREATDRILLDOWNREQUEST);
        xmlParser.findThreatDrillDown(requestParams);
        return XmlParser.writeListToJsonArray(RequestType.THREATDRILLDOWNREQUEST);
    }
}
