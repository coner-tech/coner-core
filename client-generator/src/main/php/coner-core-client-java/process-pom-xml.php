#!/usr/bin/env php
<?php
$pom = simplexml_load_file("pom.xml");

add_maven_release_plugin($pom);
add_distribution_repository($pom);

$output = $pom->asXML();
$output = tidy_repair_string($output, array(
    'input-xml' => true,
    'output-xml' => true,
    'indent' => true,
    'wrap' => 0
));
file_put_contents("pom.xml", $output);

function add_maven_release_plugin(SimpleXMLElement $pom) {
    $plugin = $pom->build->plugins->addChild("plugin");
    $plugin->addChild("groupId", "org.apache.maven.plugins");
    $plugin->addChild("artifactId", "maven-release-plugin");
    $plugin->addChild("version", "2.3.5");
    $configuration = $plugin->addChild("configuration");
    $configuration->addChild("autoVersionSubmodules", "true");
    $configuration->addChild("tagNameFormat", "v@{project.version}");
    $configuration->addChild("arguments");
}

function add_distribution_repository(SimpleXMLElement $pom) {
    $distribution_management = $pom->addChild("distributionManagement");
    $repository = $distribution_management->addChild("repository");
    $username = "caeos";
    $repository_name = "coner-core";
    $package_name = "coner-core-client-java";
    $repository->addChild("id", "bintray-$username-$package_name");
    $repository->addChild("name", "$username-$package_name");
    $repository->addChild("url", "https://api.bintray.com/maven/$username/$repository_name/$package_name/;publish=1");
}
