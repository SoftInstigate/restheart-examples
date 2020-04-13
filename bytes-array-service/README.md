# Hello World example

This is a basic Java REST Web service implemented with few lines of code.

We are using [httpie](https://httpie.org) for testing API calls, but use whatever you like.

Source code without log statements:

```java
@RegisterPlugin(
        name = "hello",
        description = "Basic ByteArrayService example",
        enabledByDefault = true,
        defaultURI = "/hello")
public class HelloByteArrayService implements ByteArrayService {
    @Override
    public void handle(ByteArrayRequest request, ByteArrayResponse response) throws Exception {
        String message = "";
        if (request.isGet()) {
            message = "Hello " + request.getExchange().getQueryParameters().get("name").getFirst();
            response.setStatusCode(HttpStatus.SC_OK);
        } else if (request.isPost() || request.isPut()) {
            String requestAsString = new String(request.getContent());
            message = "Hello " + requestAsString;
            response.setStatusCode(HttpStatus.SC_OK);
        } else {
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        }
        response.setContent(message.getBytes());
        response.setContentType("text/plain");
    }
}
```

## How to build and run

You must have both JDK 11+ and Maven to build and run this example.

Clone this repo, [Download RESTHeart v5](https://github.com/SoftInstigate/restheart/releases/tag/5.0.0-RC3) and uncompress it (`unzip restheart.zip` or `tar -xvf restheart.tar.gz`)

### With Docker 

If you have __docker__ running, then just executing the `run.sh` shell script will:

1. Build the JAR with Maven
1. Copy the JAR file into `../restheart/plugins/` folder
1. Starts a volatile MongoDB docker container
1. Starts RESTHeart Java process on port 8080

> When finished testing, send a `CTRL-C` command to stop the script, kill RESTHeart and clean-up the MongoDB container.

### Without Docker

1. Build the plugin with `mvn package`.
1. Copy the JAR into the plugins folder `cp target/bytes-array-service.jar ../restheart/plugins/`.
1. Start MongoDB in your localhost.
1. cd into the restheart distribution you have previously downloaded and uncompressed.
1. Start the process: `java -jar restheart.jar etc/restheart.yml -e etc/default.properties`.

In both cases you should see the following logs:


```
...
 16:23:52.163 [main] INFO  o.r.mongodb.db.MongoClientSingleton - Connecting to MongoDB...
 16:23:52.214 [main] INFO  o.r.mongodb.db.MongoClientSingleton - MongoDB version 4.2.1
 16:23:52.215 [main] WARN  o.r.mongodb.db.MongoClientSingleton - MongoDB is a standalone instance.
 16:23:52.370 [main] INFO  org.restheart.mongodb.MongoService - URI / bound to MongoDB resource /restheart
 16:23:52.657 [main] INFO  org.restheart.Bootstrapper - HTTP listener bound at 0.0.0.0:8080
 16:23:52.657 [main] DEBUG org.restheart.Bootstrapper - Content buffers maximun size is 16777216 bytes
 16:23:52.669 [main] INFO  org.restheart.Bootstrapper - URI / bound to service mongo, secured: true
 16:23:52.670 [main] INFO  org.restheart.Bootstrapper - URI /hello bound to service hello, secured: false
 16:23:52.671 [main] INFO  org.restheart.Bootstrapper - URI /ic bound to service cacheInvalidator, secured: false
 16:23:52.671 [main] INFO  org.restheart.Bootstrapper - URI /csv bound to service csvLoader, secured: false
 16:23:52.674 [main] INFO  org.restheart.Bootstrapper - URI /roles bound to service roles, secured: true
 16:23:52.674 [main] INFO  org.restheart.Bootstrapper - URI /ping bound to service ping, secured: false
 16:23:52.674 [main] INFO  org.restheart.Bootstrapper - URI /tokens bound to service rndTokenService, secured: false
 16:23:52.674 [main] DEBUG org.restheart.Bootstrapper - No proxies specified
 16:23:52.680 [main] DEBUG org.restheart.Bootstrapper - Allow unescaped characters in URL: true
 16:23:53.132 [main] INFO  org.restheart.Bootstrapper - Pid file /var/folders/pk/56szmnfn5zlfxh2x6tkd5wqw0000gn/T/restheart-security-877806527.pid
 16:23:53.141 [main] INFO  org.restheart.Bootstrapper - RESTHeart started
```

Call the Web service from another terminal or from a [browser](http://localhost:8080/hello?name=Mary).

## Test GET

Pass the parameter `name` to get 'Hello "name"', for example


```
$ http localhost:8080/hello?name=Mary

HTTP/1.1 200 OK
Access-Control-Allow-Credentials: true
Access-Control-Allow-Origin: *
Access-Control-Expose-Headers: Location, ETag, Auth-Token, Auth-Token-Valid-Until, Auth-Token-Location, X-Powered-By
Connection: keep-alive
Content-Encoding: gzip
Content-Length: 34
Content-Type: text/plain
Date: Mon, 13 Apr 2020 13:32:52 GMT
X-Powered-By: restheart.org

Hello Mary

```

## Test POST or PUT

Same for POST or PUT


```
$ printf 'Alice' | http POST localhost:8080/hello

HTTP/1.1 200 OK
Access-Control-Allow-Credentials: true
Access-Control-Allow-Origin: *
Access-Control-Expose-Headers: Location, ETag, Auth-Token, Auth-Token-Valid-Until, Auth-Token-Location, X-Powered-By
Connection: keep-alive
Content-Encoding: gzip
Content-Length: 34
Content-Type: text/plain
Date: Mon, 13 Apr 2020 14:28:18 GMT
X-Powered-By: restheart.org

Hello Alice

```

Using any other HTTP verb will give you a HTTP 400 error.