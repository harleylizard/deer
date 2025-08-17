package com.harleylizard.deer.manifest

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSerializer
import org.gradle.api.Project
import java.util.*

class Manifest private constructor(private val dependencies: List<Dependency>) {

    companion object {
        val jsonSerializer = JsonSerializer<Manifest> { manifest, typeOfSrc, context ->
            val jsonObject = JsonObject()
            val jsonArray = JsonArray()
            for (dependency in manifest.dependencies) {
                jsonArray.add(context.serialize(dependency, Dependency::class.java))
            }
            jsonObject.add("dependencies", jsonArray)
            jsonObject
        }

        fun of(project: Project): Manifest {
            val dependencies = mutableListOf<Dependency>()

            val implementation = project.configurations.getByName("resolved")

            implementation.resolvedConfiguration.resolvedArtifacts.map(Dependency::of).forEach(dependencies::add)
            return Manifest(Collections.unmodifiableList(dependencies))
        }
    }
}