# User Signup

> **WORK IN PROGRESS!** This example is not complete. This document only describes how it will work when fully implemented. Feedbacks and suggestions are welcome!

This example implements a user signup process.

To create an user, an unauthenticated client executes a POST request to `/users`; this  creates a new user document.

When the new document gets created:

- the request interceptor `verificationCodeGenerator` adds the verification code to the user document and sets `roles: ["UNVERIFIED"]` (the ACL defines no permissions for the role `UNVERIFIED`)
- the async response interceptor `emailVerificationSender` sends the verification email to the user with the verification code
- the service `userVerifier` allows to unlock (ie. set the roles=["USER"]) the user checking the verification code
- the response interceptor `verificationCodeHider` removes the verification code from the response of `GET /users` so that the only way to know the verification code is from the verification email.

## Validation

[JSON Schema Validation](https://restheart.org/docs/json-schema-validation/) is used to enforce the schema to the user document.

The following request updates the metadata

```
# create the schema store
PUT /_schemas

# create the JSON Schema for user

POST /_schemas

{
    "_id": "user",
    "$schema": "http://json-schema.org/draft-07/schema#",
    "type": "object",
    "properties": {
        "_id": {
            "type": "email"
        },
        "password": {
            "type": "string"
        },
        "roles": {
          "type": "array",
          "items": {
            "type": "string"
          }
        }
    }
}

# updates the metadata of collectiobn /users to apply the schema validation

PUT /users

{
    "jsonSchema": {
        "schemaId": "user"
    }
}
```

## Check user credentials

For a complete guide read [Suggested way to check credentials](https://restheart.org/docs/security/how-clients-authenticate/#suggested-way-to-check-credentials)

The following request creates the user `user@domain.io`

```
POST /users

{
    "_id": "user@domain.io"
    "password": "secret"
}
```

It will automatically get the role `UNVERIFIED` and the verification email will be sent.

The `roles` service is used to check the user credentials and returns the user roles. If the user has not verified the email, calling the roles service with the correct credentials returns the role `UNVERIFIED` and the UX should display an error message saying that the email address has not been verified.

```
GET /roles/<userid>
Authorization: <basic authentication credentials>

{
    "authenticated": true, 
    "roles": [
        "UNVERIFIED"
    ]
}
```

If the email has been verified, the roles service will return the role `USER`. In this case the sign in 

```
GET /roles/<userid>
Authorization: <basic authentication credentials>

{
    "authenticated": true, 
    "roles": [
        "USER"
    ]
}
```

## How to build and run

You need both **JDK 11+** and **Maven** to build and run this example.

-   Clone this repo `git clone git@github.com:SoftInstigate/restheart-examples.git`
-   `cd` into the `restheart-examples` folder
-   [Download RESTHeart](https://github.com/SoftInstigate/restheart/releases/)
-   uncompress it: `unzip restheart.zip` or `tar -xvf restheart.tar.gz`.

### Run

1. `cd user-signup`
1. Build the plugin with `mvn package` (uses the maven-dependency-pluging to copy the jar of the external dependency to /target/lib)
1. Copy both the service JAR `target/user-signup.jar `and `target/lib/*` into `../restheart/plugins/` folder
1. Start MongoDB in your localhost.
1. cd into the restheart distribution you have previously downloaded and uncompressed.
1. Start the process: `java -jar restheart.jar etc/restheart.yml -e etc/default.properties`.
