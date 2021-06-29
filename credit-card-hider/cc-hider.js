const BsonUtils = Java.type("org.restheart.utils.BsonUtils");

export const options = {
    name: "ccHider",
    description: "hides credit card numbers",
    interceptPoint: "RESPONSE",
    pluginClass: "MongoInterceptor"
}

export function handle(request, response) {
    var bson = response.getContent();
    // convert bson to json
    var json = JSON.parse(BsonUtils.toJson(bson));

    var hidden;

    if (Array.isArray(json)) {
        hidden = [];
        json.forEach(doc => hidden.push(hideFromDoc(doc)));
    } else {
        hidden = hideFromDoc(json);
    }

    // set response converting json to bson
    var bson = response.setContent(BsonUtils.parse(JSON.stringify(hidden)));
}

export function resolve(request) {
    return request.isGet() && "creditcards" === request.getCollectionName();
}

function hideFromDoc(doc) {
    if (doc['cc'] && doc['cc'].length > 14) {
        doc['cc'] = doc['cc'].replace(/^.{14}/g, '****-****-****');
    }

    return doc;
}