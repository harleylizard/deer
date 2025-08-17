package com.harleylizard.deer

import org.gradle.api.Plugin
import org.gradle.api.Project

class Deer : Plugin<Project> {

    override fun apply(target: Project) {
        val tasks = target.tasks

        tasks.register("zip", ZipTask::class.java) {
            it.group = "deer"
        }

        val configurations = target.configurations
        configurations.create("resolved") {
            it.isCanBeResolved = true
            it.extendsFrom(configurations.getByName("runtimeElements"))
            it.extendsFrom(configurations.getByName("compileClasspath"))
        }
    }
}