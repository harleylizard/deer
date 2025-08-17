package com.harleylizard.deer

import com.google.gson.GsonBuilder
import com.harleylizard.deer.manifest.Dependency
import com.harleylizard.deer.manifest.Manifest
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.FileUpload
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.jvm.tasks.Jar
import java.io.FileInputStream
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

open class DistributeTask @Inject constructor(objects: ObjectFactory) : DefaultTask() {
    private val artifact: RegularFileProperty = objects.fileProperty()

    private val discord = Discord(objects)

    init {
        artifact.set(project.tasks.named("jar", Jar::class.java).flatMap { it.archiveFile })
    }

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

    fun discord(action: Action<Discord>) {
        discord.also(action::execute)
    }

    @TaskAction
    fun publish() {
        val path = project.layout.buildDirectory.asFile.get().toPath().resolve("libs").resolve("${artifact.asFile.get().nameWithoutExtension}.zip")

        path.parent.takeUnless(Files::isDirectory)?.let(Files::createDirectories)

        val gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Manifest::class.java, Manifest.jsonSerializer)
            .registerTypeAdapter(Dependency::class.java, Dependency.jsonSerializer)
            .create()

        ZipOutputStream(Files.newOutputStream(path)).use {
            var entry = ZipEntry("manifest.json")
            it.putNextEntry(entry)
            it.write(gson.toJson(Manifest.of(project)).encodeToByteArray())
            it.closeEntry()

            val jar = artifact.get().asFile
            FileInputStream(jar).use { file ->
                entry = ZipEntry("${jar.nameWithoutExtension}.zip")
                it.putNextEntry(entry)
                file.copyTo(it)
                it.closeEntry()
            }
        }

        val token = discord.token
        if (token.isPresent) {
            val jda = JDABuilder.createDefault(token.get(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT).build()
            jda.awaitReady()

            for (guild in jda.guilds) {
                val channel = discord.getChannel(guild)

                if (channel != null) {
                    val embedded = EmbedBuilder()
                    embedded.setTitle("Test")

                    val file = FileUpload.fromData(Files.newInputStream(path), "test.zip")
                    channel.sendMessageEmbeds(embedded.build()).addFiles(file).queue()
                }
            }
            jda.shutdown()
        }
    }
}