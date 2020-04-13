#!/bin/bash
set -Eeuo pipefail

function run {
    echo "###### Running with parameter '$1'"
    MONGO_VERSION=4.2
    IMAGE=mongo
    MONGO_TMP_NETWORK="mongo-$(date | md5)"
    MYDIR=${PWD}
    JARFILE="$1"

    cleanup() {
        cd "$MYDIR"
        echo "### Removing ../restheart/plugins/$JARFILE"
        rm -rf "../restheart/plugins/$JARFILE"
        echo "### Cleaning up $IMAGE:$MONGO_VERSION container..."
        docker stop "$CONTAINER_ID"
        docker network rm "$MONGO_TMP_NETWORK"
        echo "### Done."
    }
    trap cleanup INT TERM EXIT ERR
    trap "exit 1" INT ERR

    mvn clean package
    cp "target/$1" ../restheart/plugins/

    echo "### Running volatile $IMAGE:$MONGO_VERSION Docker container..."
    docker network create "$MONGO_TMP_NETWORK"
    CONTAINER_ID=$( docker run --rm -d -p 27017:27017 --net="$MONGO_TMP_NETWORK" --name mongo1 "$IMAGE:$MONGO_VERSION" )
    echo "Waiting for mongodb complete startup..." && sleep 1

    cd ../restheart
    java -jar restheart.jar etc/restheart.yml -e etc/default.properties

    cleanup
}