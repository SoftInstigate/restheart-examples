# Credit Card Interceptor

A JavaScript Interceptor that hides credit card numbers

You need to run RESTHeart with the GraalVM to execute this interceptor.

## Deploy

```
$ cp -r credit-card-hider ../restheart/plugins && touch ../restheart/plugins/credit-card-hider
```

In the RESTHeart logs you'll see something like:

```
 11:24:31.009 [Thread-1] INFO  o.r.polyglot.PolyglotDeployer - Added interceptor ccHider, description: hides credit card numbers
```

## Create test data

We use httpie

```bash
$ http -a admin:secret PUT :8080/credicards
$ http -a admin:secret POST :8080/credicards cc=1234-0000-5555-0001
$ http -a admin:secret POST :8080/credicards cc=1234-0000-5555-0002
```

## See it in action

```bash
$ http -b -a admin:secret :8080/creditcards
[
    {
        "_etag": {
            "$oid": "60dae4b8a16b227e471d96f1"
        },
        "_id": {
            "$oid": "60dae4b8a16b227e471d96f2"
        },
        "cc": "****-****-****-0002"
    },
    {
        "_etag": {
            "$oid": "60dae4b6a16b227e471d96ef"
        },
        "_id": {
            "$oid": "60dae4b6a16b227e471d96f0"
        },
        "cc": "****-****-****-0001"
    }
]
```
