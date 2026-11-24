import org.gradle.api.plugins.JavaPluginExtension

plugins {
    id("multiloader-common")
    id("net.neoforged.moddev")
}

val neo_form_version: String by project
val parchment_minecraft: String by project
val parchment_version: String by project
val cloth_config_version: String by project

neoForge {
    neoFormVersion = neo_form_version
    val at = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }
    parchment {
        minecraftVersion = parchment_minecraft
        mappingsVersion = parchment_version
    }
}

dependencies {
    add("compileOnly", "org.spongepowered:mixin:0.8.5")

    add("compileOnly", "io.github.llamalad7:mixinextras-common:0.3.5")
    add("annotationProcessor", "io.github.llamalad7:mixinextras-common:0.3.5")
    add("compileOnly", "me.shedaniel.cloth:cloth-config-neoforge:$cloth_config_version") {
        isTransitive = false
    }
}

configurations {
    create("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

artifacts {
    val sourceSets = the<JavaPluginExtension>().sourceSets
    add("commonJava", sourceSets.getByName("main").java.sourceDirectories.singleFile)
    add("commonResources", sourceSets.getByName("main").resources.sourceDirectories.singleFile)
}
