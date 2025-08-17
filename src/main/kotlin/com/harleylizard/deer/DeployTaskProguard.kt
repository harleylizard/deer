@file:JvmName("DeployTaskKt")

package com.harleylizard.deer

import io.github.sgtsilvio.gradle.proguard.ProguardTask
import org.gradle.api.tasks.TaskProvider

fun DeployTask.artifact(task: TaskProvider<ProguardTask>) {
    dependsOn(task)
    artifact(task.flatMap { it.inputOutputGroups.first().outputs.first().archiveFile })
}