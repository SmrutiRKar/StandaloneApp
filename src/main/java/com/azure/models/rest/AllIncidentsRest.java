package com.azure.models.rest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by kars2 on 3/1/16.
 */

@Named
@RestController
@EnableAutoConfiguration
@Path("/allincidents")
public class AllIncidentsRest {

    @Inject
    private RestTemplate restTemplate;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonIncidents() throws Exception {
        String responseString = "Inside getJsonIncidents";
        return responseString;
    }

}
