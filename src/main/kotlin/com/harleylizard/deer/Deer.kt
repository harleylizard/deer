package com.harleylizard.deer

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class Deer : Plugin<Project> {

    override fun apply(target: Project) {
        val tasks = target.tasks

        tasks.register("distribute", DistributeTask::class.java, target.objects).configure {
            it.group = "build"
            it.dependsOn(tasks.named("build", DefaultTask::class.java))
        }

        val configurations = target.configurations
        configurations.create("resolved") {
            it.isCanBeResolved = true
            it.extendsFrom(configurations.getByName("runtimeElements"))
            it.extendsFrom(configurations.getByName("compileClasspath"))
        }
    }
}