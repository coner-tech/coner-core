#!/bin/sh

# Manipulate coner-core-client-java/.bintray.json
CONER_CORE_CLIENT_JAVA_BINTRAY_JSON="client-generator/target/generated-sources/swagger-codegen/coner-core-client-java/.bintray.json"

jq ".version.name = \"${TRAVIS_TAG}\"" ${CONER_CORE_CLIENT_JAVA_BINTRAY_JSON} > ${CONER_CORE_CLIENT_JAVA_BINTRAY_JSON}
jq ".version.desc = \"${TRAVIS_TAG}\"" ${CONER_CORE_CLIENT_JAVA_BINTRAY_JSON} > ${CONER_CORE_CLIENT_JAVA_BINTRAY_JSON}
TODAY=`date +%Y-%m-%d`
jq ".version.released = \"${TODAY}\"" ${CONER_CORE_CLIENT_JAVA_BINTRAY_JSON} > ${CONER_CORE_CLIENT_JAVA_BINTRAY_JSON}

# Push generated client repos
