package org.restheart.examples;

import com.mongodb.MongoClient;
import org.bson.Document;
import org.restheart.handlers.exchange.ByteArrayRequest;
import org.restheart.handlers.exchange.ByteArrayResponse;
import org.restheart.plugins.ByteArrayService;
import org.restheart.plugins.InjectMongoClient;
import org.restheart.plugins.RegisterPlugin;
import org.restheart.utils.HttpStatus;

@RegisterPlugin(
        name = "serverstatus",
        description = "returns MongoDB serverStatus",
        enabledByDefault = true,
        defaultURI = "/status")
public class MongoServerStatusService implements ByteArrayService {

    private MongoClient mongoClient;

    @InjectMongoClient
    public void init(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void handle(ByteArrayRequest request, ByteArrayResponse response) throws Exception {
        if (request.isGet()) {
            Document serverStatus = mongoClient.getDatabase("admin")
                    .runCommand(new Document("serverStatus", 1));
            byte[] jsonResponse = serverStatus.toJson().getBytes();
            response.setContent(jsonResponse);
            response.setStatusCode(HttpStatus.SC_OK);
            response.setContentType("application/json");
        } else {
            // Any other HTTP verb is a bad request
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
    }

}
