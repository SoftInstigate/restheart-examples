# randomString service

This example shows how to add an external dependency to the classpath so that a plugin can use it.

> By external dependency we mean a dependency that is not included in restheart.jar, thus it must be added to the classpath for the service to work.

This service just returns a random string on GET requests and uses an external dependency, Apache Commons Lang.

## How to build and run

You need both **JDK 11+** and **Maven** to build and run this example.

-   Clone this repo `git clone git@github.com:SoftInstigate/restheart-examples.git`
-   `cd` into the `restheart-examples` folder
-   Download RESTHeart, either the [ZIP](https://github.com/SoftInstigate/restheart/releases/download/5.0.0/restheart.zip) or [TAR.GZ](https://github.com/SoftInstigate/restheart/releases/download/5.0.0/restheart.tar.gz) archive.
-   uncompress it: `unzip restheart.zip` or `tar -xvf restheart.tar.gz`.

### Run

1. `cd random-string-service`
1. Build the plugin with `mvn package -Pdeps` (the profile deps uses the maven-dependency-pluging to copy the jar of the external dependency to /target/lib)
1. Copy both the service JAR `target/random-string-service.jar `and `target/lib/commons-lang3-3.10.jar` into `../restheart/plugins/` folder
1. Start MongoDB in your localhost.
1. cd into the restheart distribution you have previously downloaded and uncompressed.
1. Start the process: `java -jar restheart.jar etc/restheart.yml -e etc/default.properties`.

## Test

We suggest using [httpie](https://httpie.org) for calling the API from command line, or just use your [browser](http://localhost:8080/status).

```http
$ http localhost:8080/rndSrv

HTTP/1.1 200 OK
Access-Control-Allow-Credentials: true
Access-Control-Allow-Origin: *
Access-Control-Expose-Headers: Location, ETag, Auth-Token, Auth-Token-Valid-Until, Auth-Token-Location, X-Powered-By
Connection: keep-alive
Content-Encoding: gzip
Content-Length: 30
Content-Type: application/txt
Date: Sun, 10 May 2020 15:18:55 GMT
X-Powered-By: restheart.org

xdGtToDjid
```