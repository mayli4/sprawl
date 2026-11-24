plugins {
    id("multiloader-loader")
    id("net.neoforged.moddev")
}

val neoforge_version: String by project
val parchment_minecraft: String by project
val parchment_version: String by project
val mod_id: String by project
val cloth_config_version: String by project

neoForge {
    version = neoforge_version
    val at = project(":common").file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }
    parchment {
        minecraftVersion = parchment_minecraft
        mappingsVersion = parchment_version
    }
    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
            ideName = "NeoForge ${name.replaceFirstChar { it.uppercase() }} (${project.path})"
        }
        maybeCreate("client").apply {
            client()
        }
        maybeCreate("data").apply {
            data()

            // DataGen can be run by - "./gradlew :neoforge:runData" in Terminal.
            // Specify the modid for data generation, where to output the resulting resource, and where to look for existing resources.
            programArguments.addAll("--mod", project.property("mod_id").toString(), "--all", "--output", file("src/generated/resources/").absolutePath, "--existing", file("src/main/resources/").absolutePath)
        }
        maybeCreate("server").apply {
            server()
        }
    }
    mods {
        maybeCreate(mod_id).apply {
            sourceSet(sourceSets["main"])
        }
    }
}

sourceSets {
    named("main") {
        resources {
            srcDir("src/generated/resources")
        }
    }
}

repositories {
    maven {
        name = "Shedaniel"
        setUrl("https://maven.shedaniel.me/")
    }
}

dependencies {
    add("implementation", "me.shedaniel.cloth:cloth-config-neoforge:$cloth_config_version")
}
