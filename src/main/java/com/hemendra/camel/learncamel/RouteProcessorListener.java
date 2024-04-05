package com.hemendra.camel.learncamel;

import com.hemendra.camel.learncamel.controller.AddRoutesController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RouteProcessorListener {

    private static final Logger logger = LoggerFactory.getLogger(RouteProcessorListener.class);

    public RouteProcessorListener() {
        System.out.println("RouteProcessorListener instantiated");
    }

    @EventListener
    public void onRouteProcessorEvent(RouteProcessorEvent event) {
        String threadName = AddRoutesController.threadLocal.get();
        logger.info("Thread name: {}", threadName); //yei jinsa darkar kain na amara multitenancy re darekar
        logger.info("Received route processor event: {}", event);
    }

}
