plugins {
    kotlin("jvm") version "2.2.0"
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.harleylizard"
version = "1.0-SNAPSHOT"

gradlePlugin {
    plugins {
        create("deer") {
            id = "com.harleylizard.deer"
            implementationClass = "com.harleylizard.deer.Deer"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        mavenLocal()
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.harleylizard"
            artifactId = "deer"
            version = "1.0-SNAPSHOT"

            artifact(tasks.jar.get())
        }
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}