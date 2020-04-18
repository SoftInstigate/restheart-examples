package org.restheart.examples;

import com.mongodb.MongoClient;
import org.bson.Document;
import org.restheart.handlers.exchange.ByteArrayRequest;
import org.restheart.handlers.exchange.ByteArrayResponse;
import org.restheart.plugins.ByteArrayService;
import org.restheart.plugins.InjectMongoClient;
import org.restheart.plugins.RegisterPlugin;
import org.restheart.utils.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RegisterPlugin(
        name = "serverstatus",
        description = "returns MongoDB serverStatus",
        enabledByDefault = true,
        defaultURI = "/status")
public class MongoServerStatusService implements ByteArrayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoServerStatusService.class);

    private MongoClient mongoClient;

    @InjectMongoClient
    public void init(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void handle(ByteArrayRequest request, ByteArrayResponse response) throws Exception {
        if (request.isGet()) {
            LOGGER.debug("### QueryParameters: '{}'", request.getExchange().getQueryParameters());
            final Document command = (request.getExchange().getQueryParameters().get("command") != null)
                    ? Document.parse(request.getExchange().getQueryParameters().get("command").getFirst())
                    : new Document("serverStatus", 1);
            LOGGER.debug("### command=" + command.toJson());
            Document serverStatus = mongoClient.getDatabase("admin").runCommand(command);
            response.setContent(serverStatus.toJson().getBytes());
            response.setStatusCode(HttpStatus.SC_OK);
            response.setContentType("application/json");
        } else {
            // Any other HTTP verb is a bad request
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
    }

}
