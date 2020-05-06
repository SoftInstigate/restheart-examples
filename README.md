# RESTHeart v5 examples

RESTHeart is the REST API for MongoDB. With RESTHeart you can easily access MongoDB via a REST API, but it's also possibile to create any kind of custom Web service.

This repository provides examples on how to extend [RESTHeart v5](https://github.com/SoftInstigate/restheart) with Java plugins

The parent POM defines common properties while each module implements a single example.

The only required Maven dependency for building Java plugins is `restheart-commons`:

```xml
<dependency>
    <groupId>org.restheart</groupId>
    <artifactId>restheart-commons</artifactId>
    <version>Tag</version>
</dependency>
```

You can use this repository as a foundation for implementing your own plugins.


## Setup

You need both **JDK 11+** and **Maven** to build and run this example.

-   Clone this repo `git clone git@github.com:SoftInstigate/restheart-examples.git`
-   `cd` into the `restheart-examples` folder
-   Download RESTHeart, either the [ZIP](https://github.com/SoftInstigate/restheart/releases/download/5.0.0/restheart.zip) or [TAR.GZ](https://github.com/SoftInstigate/restheart/releases/download/5.0.0/restheart.tar.gz) archive.
-   uncompress it: `unzip restheart.zip` or `tar -xvf restheart.tar.gz`.
-   cd into each module for specific instructions.

## Modules

 - [Hello world example](bytes-array-service/README.md)
 - [MongoDB serverStatus service](mongo-status-service/README.md)
