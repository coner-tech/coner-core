#!/usr/bin/env bash

cd client-generator/target/generated-sources/swagger-codegen/coner-core-client-java

PHP_SOURCE_PATH="../../../../src/main/php/coner-core-client-java"
./$PHP_SOURCE_PATH/process-pom-xml.php

# Remove any Gradle files from the template (to stop Travis preferring it over Maven)
rm -rf *gradle*

# Push generated client repos
git init
git add .
git commit -m "Generated coner-core-client-java $TRAVIS_TAG"
git tag "$TRAVIS_TAG"
git remote add origin "https://carltonwhitehead:${GITHUB_JAVA_CLIENT_API_KEY}@github.com/caeos/coner-core-client-java.git"
git push --tags --force origin master
