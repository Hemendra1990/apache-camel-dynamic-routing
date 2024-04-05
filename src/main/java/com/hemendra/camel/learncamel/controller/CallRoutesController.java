package com.hemendra.camel.learncamel.controller;

import org.apache.camel.ProducerTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * This class is responsible for calling the routes that we have added to the Camel context. We have three methods in this class:

 */
@RestController
@RequestMapping("/api/call-routes")
public class CallRoutesController {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;
    
    @GetMapping
    public String callRoutes() {
        this.camelContext.createProducerTemplate().sendBody("direct:multitenancy", "Calling multitenancy route");
        return "Routes called successfully";
    }
    
    @GetMapping("/rest-routes")
    public String callRestRoutes() {
        String response = producerTemplate.requestBody("direct:getUuid", null, String.class);

        // Do something with the response
        System.out.println("Response from REST route: " + response);
        return "Rest Routes called successfully";
    }
    @GetMapping("/rest-routes-dynamic")
    public String callRestRouteDynamic() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("id", 1);
        String response = producerTemplate
                .requestBodyAndHeader("direct:postDelay", null, "id", 123, String.class);
        //String anotherResponse = producerTemplate.requestBodyAndHeaders("direct:postDelay", null, null, String.class);
        System.out.println("Response from REST route: " + response);
        return "Rest Routes called successfully";
    }

    @GetMapping("/rest-routes-basic-auth")
    public String callRestRouteBasicAuth() {
        Map<String, Object> headers = new HashMap<>();
        String username = "hemendra";
        String password = "admin";
        headers.put("username", username);
        headers.put("password", password);
        headers.put("basicAuth", Base64.getEncoder().encodeToString((username+":"+password).getBytes()));

        String anotherResponse = producerTemplate.requestBodyAndHeaders("direct:basicAuth", null, headers, String.class);
        System.out.println("Response from REST route: " + anotherResponse);
        return "Basic Rest Routes called successfully";
    }

    @GetMapping("/rest-routes-httpbin")
    public ResponseEntity callRestRoutesHttpBin(@RequestParam String clientName, @RequestParam String endpointPath) {
        String endpointUrl = "direct:"+clientName+"-"+endpointPath;
        Object response = producerTemplate.requestBodyAndHeaders(endpointUrl, null, null, Object.class);
        return ResponseEntity.ok(response);
    }
    
}
