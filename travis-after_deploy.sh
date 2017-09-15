#!/usr/bin/env bash

jq_mutate_file () {
    # argument order mimics jq itself
    local jq_filter=$1
    local json_file=$2
    local temp_file="jq_mutate_file.tmp"
    jq "$jq_filter" $json_file > $temp_file
    mv $temp_file $json_file
}
export -f jq_mutate_file

./client-generator/src/main/sh/coner-core-client-java/post-generate.sh
