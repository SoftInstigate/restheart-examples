#!/bin/bash
set -e

MONGO_VERSION=4.2
IMAGE=mongo

cleanup() {
    echo "### Cleaning up $IMAGE:$MONGO_VERSION container..."
    docker stop "$CONTAINER_ID"
    docker network rm "$MONGO_TMP_NETWORK"
    echo "### Done testing RESTHeart with $IMAGE:$MONGO_VERSION"
}
trap cleanup ERR INT TERM

#cd "$(dirname ${BASH_SOURCE[0]})"/.. || exit 1

MONGO_TMP_NETWORK="mongo-$(date | md5)"

mvn clean package
cp target/hello.jar restheart/plugins/

echo "### Running volatile $IMAGE:$MONGO_VERSION Docker container..."
docker network create "$MONGO_TMP_NETWORK"
CONTAINER_ID=$( docker run --rm -d -p 27017:27017 --net="$MONGO_TMP_NETWORK" --name mongo1 "$IMAGE:$MONGO_VERSION" )
echo "Waiting for mongodb complete startup..." && sleep 1

cd restheart
java -jar restheart.jar etc/restheart.yml -e etc/default.properties

cleanup