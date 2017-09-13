#!/bin/sh

# build the jar
mvn package -DskipTests -Dcheckstyle.check=false

# zip all artifacts
ZIP_OUT="coner-core-service-${TRAVIS_TAG}.zip"
ZIP_IN[0]="README.md"
ZIP_IN[1]="service/target/coner-core-service-*.jar"
ZIP_IN[2]="service/src/test/resources/config/*.yml"
zip -j $ZIP_OUT ${ZIP_IN[*]}

