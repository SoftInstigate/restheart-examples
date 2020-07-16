# User Signup

This example implements a user signup process.

To create an user, an unauthenticated client executes a POST request to `/users`; this  creates a new user document.

When the new document gets created:

- the request interceptor `verificationCodeGenerator` adds the verification code to the user document and sets `roles: ["UNVERIFIED"]` (the ACL defines no permissions for the role `UNVERIFIED`)
- the async response interceptor `emailVerificationSender` sends the verification email to the user with the verification code
- the service `userVerifier` allows to unlock (ie. set the roles=["USER"]) the user checking the verification code

## How to build and run

### Download RESTHeart and clone the this repo

You need both **JDK 11+** and **Maven** to build and run this example.

-   [Download RESTHeart](https://github.com/SoftInstigate/restheart/releases/)
-   uncompress it: `unzip restheart.zip` or `tar -xvf restheart.tar.gz`
-   Clone this repo `git clone git@github.com:SoftInstigate/restheart-examples.git`
-   `cd` into the `restheart-examples` folder

### Build and run it

1. `cd user-signup`
1. Build the plugin with `mvn package` (uses the maven-dependency-plugin to copy the jar of the external dependency to /target/lib)
1. Copy both the service JAR `target/user-signup.jar `and `target/lib/*` into `../../restheart/plugins/` folder
1. Start MongoDB in your localhost.
1. cd into the restheart distribution you have previously downloaded and uncompressed.
1. Edit the `etc/restheart.yml` as update the configuration as described in the following section **Configuration**
1. Start the process: `java -jar restheart.jar etc/restheart.yml -e etc/default.properties`.

### Configuration

You need to configure the `emailVerificationSender` interceptor with the connection parameters of an SMTP server (needed to send the verification emails). Add the following snippet to the `plugins-args` section of `etc/restheart.yml` (work with gmail but any SMTP server with SSL support should work):

```
plugins-args:
  emailVerificationSender:
    verifier-srv-url: http://127.0.0.1:8080/verify
    from: <your-email-address>
    from-name: <your-name>
    host: smtp.gmail.com
    port: 465
    smtp-username: <your-gmail-address>
    smtp-password: <your-gmail-password>
```

Set `mongoRealmAuthenticator` as the authenticator for the basic authentication mechanism:

```
auth-mechanisms:
  basicAuthMechanism:
    enabled: true
    authenticator: mongoRealmAuthenticator
```

## Validation

[JSON Schema Validation](https://restheart.org/docs/json-schema-validation/) is used to enforce the schema to the user document.

> The following request use [httpie](https://httpie.org) http cli client.


#### Create the schema store

```
$ http -a admin:secret PUT :8080/_schemas
```


#### Create the JSON Schema for user

```
$ echo '{"_id":"user","$schema":"http://json-schema.org/draft-04/schema#","type":"object","properties":{"_id":{"type":"string","pattern":"^\\\w+@[a-zA-Z_]+?.[a-zA-Z]{2,3}$"},"password":{"type":"string"},"roles":{"type":"array","items":{"type":"string"}}},"required": ["_id", "password"]}' | http -a admin:secret POST :8080/_schemas
```


#### Update the metadata of /users to apply the schema validation

```
$ echo '{"jsonSchema":{"schemaId":"user"}}' | http -a admin:secret PUT :8080/users
```

## ACL

To allow unauthenticated clients to create user documents add the following permission to `/acl`:

```
$ echo '{"_id":{"$oid":"5da0510e5cf10c7dbfc2ffb2"},"predicate":"path['/users'] and method[POST]","roles":["$unauthenticated"],"priority":1,"readFilter":null,"writeFilter":null}' | http POST :8080/acl
```

Give the role `USER` some permissions on the collection `/coll`

```
$ echo '{"_id":{"$oid":"5f0f0e1785a5d15404f953bb"},"predicate":"path-prefix['/coll'] and (method[PUT] or method[POST] or method[GET])","roles":["USER"],"priority":1,"readFilter":null,"writeFilter":null}' | http -a admin:secret POST :8080/acl
```

## Create user and verify the email address 

The following unauthenticated request creates the user with your email address:

```
$ http :8080/users _id=<your-email-address> password=<your-password>
```

The new user will automatically get the role `UNVERIFIED` and the verification email will be sent.

And email will be sent with a verification link. Clicking on the link will unlock the user giving her the role 'USER'.

For a complete guide on how to check the user credentials read [Suggested way to check credentials](https://restheart.org/docs/security/how-clients-authenticate/#suggested-way-to-check-credentials).

In short, the `/roles` service can be used to check the user credentials. It returns the user roles. If the user has not yet verified the email, calling the roles service with the correct credentials returns the role `UNVERIFIED` and the UX should display an error message saying that the email address has not been verified.

```
$ http -a <your-email-address>:<your-password> :8080/roles/<your-email-address>

{
    "authenticated": true, 
    "roles": [
        "UNVERIFIED"
    ]
}
```

Let try to create the collection `/coll` with the new user. The request fails with `403 Forbidden` because the user has role `UNVERIFIED` and no permissions are given to it.

```
http -a <your-email-address>:<your-password> PUT :8080/coll

HTTP/1.1 403 Forbidden
```

Check the email and verify your address by clicking in the verification link. 

Now the roles service will return the role `USER`. 

```
$ http -a <your-email-address>:<your-password> :8080/roles/<your-email-address>

{
    "authenticated": true, 
    "roles": [
        "USER"
    ]
}
```

Let try to create the collection `/coll` with the verified user. The request succeeds with `201 Created` because the user has now role `USER` and it has a permission to execute the request.

```
http -a <your-email-address>:<your-password> PUT :8080/coll

HTTP/1.1 201 Created
```
