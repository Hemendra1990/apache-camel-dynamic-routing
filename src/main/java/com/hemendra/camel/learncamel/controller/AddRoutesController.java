package com.hemendra.camel.learncamel.controller;

import com.hemendra.camel.learncamel.RouteProcessorEvent;
import com.hemendra.camel.learncamel.config.ClientConfig;
import com.hemendra.camel.learncamel.config.ClientConfigs;
import com.hemendra.camel.learncamel.service.TenantResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.rest.RestDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * This class is responsible for adding routes to the Camel context. We have three methods in this class:
 */
@RestController
@RequestMapping("/api/add-routes")
public class AddRoutesController {

    private static final Logger logger = LoggerFactory.getLogger(AddRoutesController.class);

    public static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private TenantResolver tenantResolver;
    
    @GetMapping
    public String addRoutes() throws Exception {
        logger.info("{}", camelContext);
        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:multitenancy")
                    .log("Multitenancy route")
                    .to("mock:multitenancy");
            }
        };
        camelContext.addRoutes(routeBuilder);
        camelContext.getRoutes().forEach(System.out::println);
        return "Routes added successfully";
    }

    @GetMapping("/add-rest-route")
    public String addRestRoute() throws Exception {
        String baseUrl = "http://localhost:8977";
        String method = "get";
        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:getUuid")
                        .log("UUID route")
                        .to(baseUrl + "/uuid");//actual rest call yeithi hauchi, this is defined

                from("direct:postDelay")
                        .log("Post Delay route")
                        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                        .toD("http://localhost:8977/delay/${header.id}");
            }
        };
        camelContext.addRoutes(routeBuilder);
        return "Rest Route added ";
    }

    /**
     * The Difference between to and toD is that toD is used when you want to build the URI dynamically at runtime. You can use placeholders in the URI and replace them with actual values at runtime.
     * @return
     */

    @GetMapping("/add-rest-route-basic-auth")
    public String addBasicAuthRoute() throws Exception {

        String baseUrl = "http://localhost:8977";
        String basicAuthUrl = "http://localhost:8977/basic-auth"; // {{username}} and {{password}} are property placeholders that will be replaced with the actual username and password at runtime. You can define these properties in a properties file or as environment variables, depending on your needs.

        //Process is after na before??

        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                RouteDefinition from = from("direct:basicAuth");
                from
                        .log("Basic Auth route")
                        //.id("basicAuthRoute")
                        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                        .setHeader("Authorization", simple("Basic ${header.basicAuth}"))
                        .process(exchange -> {
                            logger.info("First Processor {}", exchange.getIn().getBody(String.class));
                            exchange.getIn().setHeader("username", "hemendra");
                            String name = Thread.currentThread().getName();
                            logger.info("Thread name In Processor: {}", name);
                            threadLocal.set(name);
                        })
                        .toD(basicAuthUrl + "/${header.username}/${header.password}")
                        .process(exchange -> {
                            logger.info("Second Processor {}", exchange.getIn().getBody(String.class));
                            String response = exchange.getIn().getBody(String.class);
                            System.out.println("Response from REST route: " + response);
                            RouteProcessorEvent routeProcessorEvent = new RouteProcessorEvent(response);

                            applicationContext.publishEvent(routeProcessorEvent);

                        });
            }
        };

        this.camelContext.addRoutes(routeBuilder);

        return "Basic Auth Rest Route added ";
    }

    @GetMapping("/add-rest-route-with-json-config")
    public String addRoutesAsPerClients() {
        ClientConfigs clientConfigs = tenantResolver.getClientConfig();
        clientConfigs.getClientConfigs().stream().filter(clientConfig -> clientConfig.getClientName().equals("HttpBin"))
                .forEach(clientConfig -> {
                    RouteBuilder routeBuilder = new RouteBuilder() {
                        @Override
                        public void configure() throws Exception {
                            String clientBaseUrl = clientConfig.getBaseUrl();
                            List<ClientConfig.Resource> resources = clientConfig.getResources();
                            resources.forEach(routeResource -> {
                                routeResource.getEndpoints()
                                        .forEach(endpoint -> {
                                            RouteDefinition routeDefinition = from("direct:" + clientConfig.getClientName() + "-" + endpoint.getName());
                                            if (endpoint.getMethod().equalsIgnoreCase("get")) {
                                                routeDefinition
                                                        .log("GET route")
                                                        .setHeader(Exchange.HTTP_METHOD, constant("GET"));

                                            } else if (endpoint.getMethod().equalsIgnoreCase("post")) {
                                                routeDefinition
                                                        .log("POST route")
                                                        .setHeader(Exchange.HTTP_METHOD, constant("POST"));
                                            }
                                            routeDefinition
                                                    .toD(clientBaseUrl + endpoint.getPath())
                                                    .process(exchange -> {
                                                        logger.info("Response from REST route: {} fror the client {}", exchange.getIn().getBody(String.class), clientConfig.getClientName());
                                                        logger.info("Response from REST route: {}", exchange.getIn().getBody(String.class));
                                                    });
                                        });

                            });

                        }
                    };
                    try {
                        this.camelContext.addRoutes(routeBuilder);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return "Routes added successfully";
    }
    
}
