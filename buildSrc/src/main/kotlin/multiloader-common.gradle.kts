import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    `java-library`
    `maven-publish`
}

val mod_id: String by project
val minecraft_version: String by project
val java_version: String by project
val minecraft_version_range: String by project
val fabric_version: String by project
val fabric_loader_version: String by project
val mod_name: String by project
val mod_author: String by project
val license: String by project
val credits: String by project
val neoforge_version: String by project
val neoforge_loader_version_range: String by project
val forge_version: String by project
val forge_loader_version_range: String by project
val cloth_config_version: String by project

base {
    archivesName.set("$mod_id-${project.name}-$minecraft_version")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(java_version))
    }
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    // https://docs.gradle.org/current/userguide/declaring_repositories.html#declaring_content_exclusively_found_in_one_repository
    exclusiveContent {
        forRepository {
            maven {
                name = "Sponge"
                setUrl("https://repo.spongepowered.org/repository/maven-public")
            }
        }
        filter {
            includeGroupAndSubgroups("org.spongepowered")
        }
    }
    exclusiveContent {
        forRepositories(
            maven {
                name = "ParchmentMC"
                setUrl("https://maven.parchmentmc.org/")
            },
            maven {
                name = "NeoForge"
                setUrl("https://maven.neoforged.net/releases")
            }
        )
        filter {
            includeGroup("org.parchmentmc.data")
        }
    }
    maven {
        name = "BlameJared"
        setUrl("https://maven.blamejared.com")
    }
    maven {
        name = "Shedaniel"
        setUrl("https://maven.shedaniel.me/")
    }
}

listOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements").forEach { variant ->
    configurations.named(variant) {
        outgoing {
            capability("$group:${the<BasePluginExtension>().archivesName.get()}:$version")
            capability("$group:$mod_id-${project.name}-$minecraft_version:$version")
            capability("$group:$mod_id:$version")
        }
    }
    publishing {
        publications.configureEach {
            if (this is MavenPublication) {
                suppressPomMetadataWarningsFor(variant)
            }
        }
    }
}

tasks.named<Jar>("sourcesJar") {
    from(rootProject.file("LICENSE")) {
        rename { "${it}_$mod_name" }
    }
}

tasks.named<Jar>("jar") {
    from(rootProject.file("LICENSE")) {
        rename { "${it}_$mod_name" }
    }

    manifest {
        attributes(
            mapOf(
                "Specification-Title" to mod_name,
                "Specification-Vendor" to mod_author,
                "Specification-Version" to project.version.toString(),
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version.toString(),
                "Implementation-Vendor" to mod_author,
                "Built-On-Minecraft" to minecraft_version
            )
        )
    }
}

tasks.named<ProcessResources>("processResources") {
    val expandProps = mapOf(
        "version" to version.toString(),
        "group" to project.group.toString(),
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to minecraft_version_range,
        "fabric_version" to fabric_version,
        "fabric_loader_version" to fabric_loader_version,
        "mod_name" to mod_name,
        "mod_author" to mod_author,
        "mod_id" to mod_id,
        "license" to license,
        "description" to (project.description ?: ""),
        "neoforge_version" to neoforge_version,
        "neoforge_loader_version_range" to neoforge_loader_version_range,
        "forge_version" to forge_version,
        "forge_loader_version_range" to forge_loader_version_range,
        "credits" to credits,
        "java_version" to java_version,
        "cloth_config_version" to cloth_config_version
    )

    filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/mods.toml", "META-INF/neoforge.mods.toml", "*.mixins.json")) {
        expand(expandProps)
    }
    inputs.properties(expandProps)
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifactId = the<BasePluginExtension>().archivesName.get()
            from(components["java"])
        }
    }
    repositories {
        maven {
            System.getenv("local_maven_url")?.let { setUrl(it) }
        }
    }
}
