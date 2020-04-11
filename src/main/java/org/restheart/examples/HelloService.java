/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.restheart.examples;

import org.restheart.handlers.exchange.ByteArrayRequest;
import org.restheart.handlers.exchange.ByteArrayResponse;
import org.restheart.plugins.ByteArrayService;
import org.restheart.plugins.RegisterPlugin;
import org.restheart.utils.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mturatti
 */
@RegisterPlugin(
        name = "hello",
        description = "simple hello service",
        enabledByDefault = true,
        defaultURI = "/hello")
public class HelloService implements ByteArrayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloService.class);

    @Override
    public void handle(ByteArrayRequest request, ByteArrayResponse response) throws Exception {
        String message = "";
        if (request.isGet()) {
            LOGGER.info("query string: '{}'", request.getExchange().getQueryString());
            message = "Hello " + request.getExchange().getQueryParameters().get("name").getFirst();
            response.setStatusCode(HttpStatus.SC_OK);
        } else if (request.isPost() || request.isPut()) {
            String requestAsString = new String(request.getContent());
            LOGGER.info("request: '{}'", requestAsString);
            message = "Hello " + requestAsString;
            response.setStatusCode(HttpStatus.SC_OK);
        } else {
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
        response.setContent(message.getBytes());
        response.setContentType("text/plain");
        LOGGER.info("response: '{}'", message);
    }

}
