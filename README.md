# Deer
Gradle plugin for publishing lwjgl java applications

## Examples

Discord Integration
```kts
plugins {
    id("com.harleylizard.deer") version "version"
}

tasks.distribute {
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

tasks.distribute {
    artifact(proguardJar)
}
```
