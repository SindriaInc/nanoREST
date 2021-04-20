#!/usr/bin/env bash

CONTAINER_NAME=nanoREST
#APP_VERSION=0.1.0

docker exec -t ${CONTAINER_NAME} /bin/bash -c "mvn compile; mvn package"
#docker exec -t ${CONTAINER_NAME} /bin/bash -c "java -jar target/${CONTAINER_NAME}-${APP_VERSION}.jar"
