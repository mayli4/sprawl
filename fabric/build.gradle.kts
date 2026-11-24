import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("multiloader-loader")
    id("fabric-loom")
}

val minecraft_version: String by project
val parchment_minecraft: String by project
val parchment_version: String by project
val fabric_loader_version: String by project
val fabric_version: String by project
val cloth_config_version: String by project
val modmenu_version: String by project
val mod_id: String by project

repositories {
    maven {
        name = "Shedaniel"
        setUrl("https://maven.shedaniel.me/")
    }
    maven {
        name = "TerraformersMC"
        setUrl("https://maven.terraformersmc.com/releases/")
    }
}

val loomExt = extensions.getByType<LoomGradleExtensionAPI>()

dependencies {
    add("minecraft", "com.mojang:minecraft:$minecraft_version")
    add("mappings", loomExt.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-$parchment_minecraft:$parchment_version@zip")
    })
    add("modImplementation", "net.fabricmc:fabric-loader:$fabric_loader_version")
    add("modImplementation", "net.fabricmc.fabric-api:fabric-api:$fabric_version")
    add("modImplementation", "me.shedaniel.cloth:cloth-config-fabric:$cloth_config_version")
    add("modCompileOnly", "com.terraformersmc:modmenu:$modmenu_version")
}

loom {
    val aw = project(":common").file("src/main/resources/$mod_id.accesswidener")
    if (aw.exists()) {
        accessWidenerPath.set(aw)
    }
    mixin {
        defaultRefmapName.set("$mod_id.refmap.json")
    }
    runs {
        maybeCreate("client").apply {
            client()
            setConfigName("Fabric Client")
            setIdeConfigGenerated(true)
            runDir("runs/client")
        }
        maybeCreate("server").apply {
            server()
            setConfigName("Fabric Server")
            setIdeConfigGenerated(true)
            runDir("runs/server")
        }
    }
}
