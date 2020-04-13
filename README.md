# RESTHeart v5 examples

This repository provides examples on how to extend RESTHeart v5 with Java plugins.

It's a multi-module Maven project: the parent POM defines common properties while each module implements a single example.

However, The only required Maven dependency for building RESTHeart Java plugins is `restheart-commons`:

```xml
<dependency>
    <groupId>org.restheart</groupId>
    <artifactId>restheart-commons</artifactId>
    <version>Tag</version>
</dependency>
```

## Setup

1) clone this repo and cd into it

```bash
git clone https://github.com/SoftInstigate/restheart-examples

cd restheart-examples
```

2) [Download RESTHeart v5](https://github.com/SoftInstigate/restheart/releases/tag/5.0.0-RC3) and uncompress it (`unzip restheart.zip` or `tar -xvf restheart.tar.gz`)

3) cd into each module for specific instructions.

## Modules

 - [bytes-array-service](bytes-array-service/README.md)