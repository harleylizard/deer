# Deer
Gradle plugin for publishing java applications

Creates and packages a manifest file of project dependencies to be downloaded when the application is ran.

**Uses the current platform to determine manifest dependencies. E.g. windows natives for lwjgl (requires lwjgl plugin)
## Examples

Discord Integration
```kts
plugins {
    id("com.harleylizard.deer") version "version"
}

tasks.deploy {
    artifact(tasks.jar)
    discord {
        token = "..."
        changes.set(listOf("example"))

        server(your_server) {
            channel(your_channel)

            notify(Role.everyone)
        }
    }
}
```

Proguard Integration

```kts
plugins {
    id("com.harleylizard.deer") version "version"
    id("io.github.sgtsilvio.gradle.proguard") version "version"
}

dependencies { 
    proguardClasspath("com.guardsquare:proguard-base:version")
}

val proguardJar by tasks.registering(proguard.taskClass) {
    // ...
}

tasks.deploy {
    artifact(proguardJar)
}
```
