package org.coner.core.client_generator.java

import org.dom4j.Document
import org.dom4j.DocumentFactory
import org.dom4j.io.SAXReader
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PomProcessorTest {

    val documentAsString = """
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.coner</groupId>
  <artifactId>coner-core-client-java</artifactId>
  <packaging>jar</packaging>
  <name>coner-core-client-java</name>
</project>
""".trimMargin()
    lateinit var document: Document

    @Before
    fun before() {
        val factory = DocumentFactory()
        factory.xPathNamespaceURIs = mapOf("mvn" to "http://maven.apache.org/POM/4.0.0")
        val reader = SAXReader(factory)
        document = reader.read(documentAsString.byteInputStream())
    }

    @Test
    fun itShouldQueryRootNode() {
        val project = document.selectSingleNode("/project")
        assertNotNull(project)
    }

    @Test
    fun documentShouldSelectNodeWithDefaultNamespace() {
        val projectName = document.selectSingleNode("/project/mvn:name")

        assertNotNull(projectName)
        assertEquals("coner-core-client-java", projectName.text)
    }
}
