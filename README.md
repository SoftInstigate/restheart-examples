# RESTHeart examples

[![GitHub last commit](https://img.shields.io/github/last-commit/softinstigate/restheart-examples)](https://github.com/SoftInstigate/restheart-examples/commits/master)
[![Build](https://github.com/SoftInstigate/restheart-examples/workflows/Build/badge.svg)](https://github.com/SoftInstigate/restheart-examples/actions?query=workflow%3A%22Build%22)

RESTHeart is the REST API for MongoDB. With RESTHeart you can easily access MongoDB via a REST API, but it's also possible to create any kind of custom Web service.

This repository provides examples on how to extend [RESTHeart](https://github.com/SoftInstigate/restheart) with Java or Kotlin plugins.

The parent POM defines common properties while each module implements a single example.

The only required dependency for building Java plugins is `restheart-commons`:

```xml
<dependency>
    <groupId>org.restheart</groupId>
    <artifactId>restheart-commons</artifactId>
    <version>Tag</version>
</dependency>
```

You can use this repository as a foundation for implementing your own plugins.


## Setup

You need **JDK 17++** to build and run the examples.

-   Clone this repo `git clone git@github.com:SoftInstigate/restheart-examples.git`.
-   `cd` into the `restheart-examples` folder.
-   [Download RESTHeart](https://github.com/SoftInstigate/restheart/releases/).
-   Uncompress it: `unzip restheart.zip` or `tar -xvf restheart.tar.gz`.
-   `cd` into each module folder for specific instructions.

## Modules

 - [Hello world example](bytes-array-service/README.md) - A basic Java REST Web service.
 - [MongoDB serverStatus service](mongo-status-service/README.md) - Implements the serverStatus MongoDB system call.
 - [Random String service](random-string-service/README.md) - Shows how to deploy a service that uses external dependencies.
 - [CSV Interceptor](csv-interceptor/README.md) - Converts coordinates from [CSV](https://en.wikipedia.org/wiki/Comma-separated_values) to a [GeoJSON](https://geojson.org) object.
 - [User Signup](user-signup/README.md) - Implements a user signup process. This is an example of a complex process implemented with few interceptors, a service and uses json schema validation and security permissions.
 - [Kotlin Greeter Service](kotlin-greeter-service/README.md) - Simple service implemented in Kotlin programming language.
 - [Credit Card Hider](credit-card-hider/README.md) - Simple JavaScript interceptor to hide credit card numbers.
