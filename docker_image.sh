#!/bin/bash
./mvnw clean install -DskipTests && docker build -t tinyurl .

# how to run docker image with a new container
# docker run -p 9001:8080 --name tinyurl --link mysql1:mysql --link redis:redis tinyurl