package com.harleylizard.deer

import com.google.gson.GsonBuilder
import com.harleylizard.deer.manifest.Dependency
import com.harleylizard.deer.manifest.Manifest
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

open class DistributeTask @Inject constructor(objects: ObjectFactory) : DefaultTask() {
    private val artifact: RegularFileProperty = objects.fileProperty()

    fun artifact(file: RegularFile) {
        artifact.set(file)
    }

    fun artifact(file: Provider<RegularFile>) {
        artifact.set(file.get())
    }

    fun artifact(task: TaskProvider<out AbstractArchiveTask>) {
        dependsOn(task)
        artifact.set(task.flatMap { it.archiveFile })
    }

    @TaskAction
    fun publish() {
        val path = project.layout.buildDirectory.asFile.get().toPath().resolve("libs").resolve("${project.name}-${project.version}.zip")

        path.parent.takeUnless(Files::isDirectory)?.let(Files::createDirectories)

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Manifest::class.java, Manifest.jsonSerializer)
            .registerTypeAdapter(Dependency::class.java, Dependency.jsonSerializer)
            .create()

        ZipOutputStream(Files.newOutputStream(path)).use {
            val stack = ZipStack(it)

            stack.push("manifest.json")
            stack.write(gson.toJson(Manifest.of(project)).encodeToByteArray())
            stack.pop()

            val jar = artifact.get().asFile
            FileInputStream(jar).use { file ->
                stack.push(jar)
                stack.write(file)
                stack.pop()
            }
        }
    }

    private class ZipStack(private val zip: ZipOutputStream) {

        fun push(name: String) {
            val entry = ZipEntry(name)
            zip.putNextEntry(entry)
        }

        fun push(file: File) {
            push(file.name)
        }

        fun write(inputStream: InputStream) {
            inputStream.copyTo(zip)
        }

        fun write(bytes: ByteArray) {
            zip.write(bytes, 0, bytes.size)
        }

        fun pop() {
            zip.closeEntry()
        }
    }
}