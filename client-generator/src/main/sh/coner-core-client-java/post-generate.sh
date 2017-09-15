#!/usr/bin/env bash

# Post-process the coner-core-client-java/.bintray.json
CONER_CORE_CLIENT_JAVA_BINTRAY_JSON="client-generator/target/generated-sources/swagger-codegen/coner-core-client-java/.bintray.json"
jq_mutate_file ".version.name = \"${TRAVIS_TAG}\"" $CONER_CORE_CLIENT_JAVA_BINTRAY_JSON
jq_mutate_file ".version.desc = \"${TRAVIS_TAG}\"" $CONER_CORE_CLIENT_JAVA_BINTRAY_JSON
TODAY=`date +%Y-%m-%d`
jq_mutate_file ".version.released = \"${TODAY}\"" $CONER_CORE_CLIENT_JAVA_BINTRAY_JSON
jq_mutate_file ".version.vcs_tag = \"$TRAVIS_COMMIT\"" $CONER_CORE_CLIENT_JAVA_BINTRAY_JSON

# Push generated client repos
cd client-generator/target/generated-sources/swagger-codegen/coner-core-client-java
git init
git add .
git commit -m "Generated coner-core-client-java $TRAVIS_TAG"
git tag "$TRAVIS_TAG"
git remote add origin git@github.com:caeos/coner-core-client-java.git
git push --tags --force origin master