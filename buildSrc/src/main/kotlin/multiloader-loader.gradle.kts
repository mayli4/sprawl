import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    id("multiloader-common")
}

val mod_id: String by project

configurations {
    create("commonJava") {
        isCanBeResolved = true
    }
    create("commonResources") {
        isCanBeResolved = true
    }
}

dependencies {
    "compileOnly"(project(":common")) {
        capabilities {
            requireCapability("$group:$mod_id")
        }
    }
    add("commonJava", project(path = ":common", configuration = "commonJava"))
    add("commonResources", project(path = ":common", configuration = "commonResources"))
}

tasks.named<JavaCompile>("compileJava") {
    dependsOn(configurations["commonJava"])
    source(configurations["commonJava"])
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(configurations["commonResources"])
    from(configurations["commonResources"])
}

tasks.named<Javadoc>("javadoc") {
    dependsOn(configurations["commonJava"])
    source(configurations["commonJava"])
}

tasks.named<Jar>("sourcesJar") {
    dependsOn(configurations["commonJava"])
    from(configurations["commonJava"])
    dependsOn(configurations["commonResources"])
    from(configurations["commonResources"])
}
