#!/bin/sh
chmod +x Application.sh

mvn clean install

cd target/
java -jar web-rest-api-1.0-SNAPSHOT-jar-with-dependencies.jar
