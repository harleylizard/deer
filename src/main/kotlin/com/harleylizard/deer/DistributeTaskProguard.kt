@file:JvmName("DistributeTaskKt")

package com.harleylizard.deer

import io.github.sgtsilvio.gradle.proguard.ProguardTask
import org.gradle.api.tasks.TaskProvider

fun DistributeTask.artifact(task: TaskProvider<ProguardTask>) {
    dependsOn(task)
    artifact(task.flatMap { it.inputOutputGroups.first().outputs.first().archiveFile })
}