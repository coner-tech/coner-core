package org.coner.core.client_generator.java

import org.dom4j.DocumentFactory
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import java.io.File
import java.io.FileWriter


class PomProcessor(cwd: File) {

    val pomFile = File(cwd, "pom.xml")
    val reader = SAXReader(DocumentFactory().apply { xPathNamespaceURIs = mapOf("mvn" to "http://maven.apache.org/POM/4.0.0") })
    val document = reader.read(pomFile)

    fun run() {
        enforceArtifactVersionIsSnapshot()
        addMavenReleasePlugin()
        addDistributionRepository()
        save()
    }

    private fun enforceArtifactVersionIsSnapshot() {
        val snapshot = "-SNAPSHOT"
        val version = document.selectSingleNode("/project/mvn:version")
        if (!version.text.endsWith(snapshot)) {
            version.text = "${version.text}$snapshot"
        }
    }

    private fun addMavenReleasePlugin() {
        val plugins = document.selectSingleNode("/project/mvn:build/mvn:plugins") as Element
        val plugin = plugins.addElement("plugin")
        commentAddedElementRoot(plugin)
        with(plugin) {
            addElement("groupId").text = "org.apache.maven.plugins"
            addElement("artifactId").text = "maven-release-plugin"
            addElement("version").text = "2.5.3"
        }
        val configuration = plugin.addElement("configuration")
        with(configuration) {
            addElement("autoVersionSubmodules").text = "true"
            addElement("tagNameFormat").text = "v@{project.version}"
            addElement("arguments")
        }
    }

    private fun addDistributionRepository() {
        val username = "caeos"
        val repositoryName = "coner-core"
        val packageName = "coner-core-client-java"

        val distributionManagement = document.rootElement.addElement("distributionManagement")
        commentAddedElementRoot(distributionManagement)
        val repository = distributionManagement.addElement("repository")
        with(repository) {
            addElement("id").text = "bintray-$username-$packageName"
            addElement("name").text = "$username-$packageName"
            addElement("url").text = "https://api.bintray.com/maven/$username/$repositoryName/$packageName/;publish=1"
        }
    }

    private fun save() {
        val writer = XMLWriter(FileWriter(pomFile), OutputFormat.createPrettyPrint())
        writer.write(document)
        writer.close()
    }

    private fun commentAddedElementRoot(element: Element) {
        element.addComment("Added by ${this::class.qualifiedName}")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val cwd = File(args[0])
            val app = PomProcessor(cwd)
            app.run()
        }
    }
}
