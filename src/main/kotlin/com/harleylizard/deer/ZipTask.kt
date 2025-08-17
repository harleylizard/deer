package com.harleylizard.deer

import com.google.gson.GsonBuilder
import com.harleylizard.deer.manifest.Dependency
import com.harleylizard.deer.manifest.Manifest
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

open class ZipTask : DefaultTask() {

    @TaskAction
    fun publish() {
        val path = project.layout.buildDirectory.asFile.get().toPath().resolve("lib").resolve("${project.name}-${project.version}.zip")

        path.parent.takeUnless(Files::isDirectory)?.let(Files::createDirectories)

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Manifest::class.java, Manifest.jsonSerializer)
            .registerTypeAdapter(Dependency::class.java, Dependency.jsonSerializer)
            .create()
        ZipOutputStream(Files.newOutputStream(path)).use {
            val entry = ZipEntry("manifest.json")
            it.putNextEntry(entry)

            val manifest = gson.toJson(Manifest.of(project)).encodeToByteArray()
            it.write(manifest, 0, manifest.size)

            it.closeEntry()
        }
    }
}