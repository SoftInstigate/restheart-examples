# User Signup

This example implements a user signup process:

unauthenticated clients can POST to /users to create new users. 

whan a new user document is created:
- the request interceptor verificationCodeGenerator adds the verification code to the user document and sets the roles=["UNVERIFIED"]
- the async response interceptor emailVerificationSender sends the verification email to the user with the verification code
- the service userVerifier allows to unlock (ie. set the roles=["USER"]) the user checking the verification code

## How to build and run

You need both **JDK 11+** and **Maven** to build and run this example.

-   Clone this repo `git clone git@github.com:SoftInstigate/restheart-examples.git`
-   `cd` into the `restheart-examples` folder
-   Download RESTHeart, either the [ZIP](https://github.com/SoftInstigate/restheart/releases/download/5.0.0/restheart.zip) or [TAR.GZ](https://github.com/SoftInstigate/restheart/releases/download/5.0.0/restheart.tar.gz) archive.
-   uncompress it: `unzip restheart.zip` or `tar -xvf restheart.tar.gz`.

### Run

1. `cd user-signup`
1. Build the plugin with `mvn package` (uses the maven-dependency-pluging to copy the jar of the external dependency to /target/lib)
1. Copy both the service JAR `target/user-signup.jar `and `target/lib/*` into `../restheart/plugins/` folder
1. Start MongoDB in your localhost.
1. cd into the restheart distribution you have previously downloaded and uncompressed.
1. Start the process: `java -jar restheart.jar etc/restheart.yml -e etc/default.properties`.
