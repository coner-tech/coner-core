#!/usr/bin/env php
<?php
$filename = ".bintray.json";
$input = file_get_contents($filename);
$json = json_decode($input);

$json->version->name = getenv("TRAVIS_TAG");
$json->version->desc = getenv("TRAVIS_TAG");
$now = date("c");
$json->version->released = $now;
$json->version->vcs_tag = getenv("TRAVIS_COMMIT");

$output = json_encode($json, JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES);
file_put_contents($filename, $output);
