package com.harleylizard.deer.manifest

import com.google.gson.JsonObject
import com.google.gson.JsonSerializer
import org.gradle.api.artifacts.ResolvedArtifact

class Dependency private constructor(private val name: String, var url: String) {

    companion object {
        val jsonSerializer = JsonSerializer<Dependency> { dependency, typeOfSrc, context ->
            val jsonObject = JsonObject()
            jsonObject.addProperty("name", dependency.name)
            jsonObject.addProperty("url", dependency.url)
            jsonObject
        }

        fun of(artifact: ResolvedArtifact): Dependency {
            val file = artifact.file
            return Dependency(file.nameWithoutExtension, file.name)
        }

    }
}