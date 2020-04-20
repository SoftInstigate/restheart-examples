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

1) clone this repo and cd into it

```bash
git clone https://github.com/SoftInstigate/restheart-examples

cd restheart-examples
```

2) [Download RESTHeart v5](https://github.com/SoftInstigate/restheart/releases/tag/5.0.0-RC4) and uncompress it (`unzip restheart.zip` or `tar -xvf restheart.tar.gz`)

3) cd into each module for specific instructions.

## Modules

 - [Hello world example](bytes-array-service/README.md)
 - [MongoDB serverStatus service](mongo-status-service/README.md)
