#!/bin/bash

export MICROSERVICE_NAME=sonata-authorization-test

echo "Starting building image with name: $MICROSERVICE_NAME"

docker image build  -t $MICROSERVICE_NAME .


echo "Image $MICROSERVICE_NAME has been successfully built"