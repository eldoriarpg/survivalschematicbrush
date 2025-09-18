import de.chojo.Repo
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.FALSE

plugins {
    id("org.cadixdev.licenser") version "0.6.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("de.chojo.publishdata") version "1.4.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    java
    `maven-publish`
}

group = "de.eldoria"
version = "1.1.0"

repositories {
    maven("https://eldonexus.de/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-proxies/")
}

dependencies {
    compileOnly("de.eldoria", "schematicbrushreborn-api", "2.6.0")
    compileOnly("org.spigotmc", "spigot-api", "1.13.2-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit", "worldedit-bukkit", "7.2.17")

    bukkitLibrary("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.3")
    bukkitLibrary("com.fasterxml.jackson.core:jackson-core:2.14.2")
    bukkitLibrary("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    bukkitLibrary("net.kyori:adventure-platform-bukkit:4.3.1")
    bukkitLibrary("net.kyori:adventure-text-minimessage:4.13.1")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
}

license {
    header(rootProject.file("HEADER.txt"))
    include("**/*.java")
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_17
}

publishData {
    addBuildData()
    addRepo(Repo.main("", "https://eldonexus.de/repository/maven-releases/", false))
    addRepo(Repo.dev("DEV", "https://eldonexus.de/repository/maven-dev/", true))
    addRepo(Repo.snapshot("SNAPSHOT", "https://eldonexus.de/repository/maven-snapshots/", true))
    publishComponent("java")
}

publishing {
    publications.create<MavenPublication>("maven") {
        publishData.configurePublication(this)
    }

    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    username = System.getenv("NEXUS_USERNAME")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }

            setUrl(publishData.getRepository())
            name = "EldoNexus"
        }
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    compileTestJava {
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    shadowJar {
        relocate("de.eldoria.eldoutilities", "de.eldoria.schematicbrush.libs.eldoutilities")
        relocate("de.eldoria.messageblocker", "de.eldoria.schematicbrush.libs.messageblocker")
        mergeServiceFiles()
    }

    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("plugin.yml") {
                expand(
                        "version" to publishData.getVersion(true)
                )
            }
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }

    register<Copy>("copyToServer") {
        val path = project.property("targetDir") ?: "";
        if (path.toString().isEmpty()) {
            println("targetDir is not set in gradle properties")
            return@register
        }
        println("Copying jar to $path")
        from(shadowJar)
        destinationDir = File(path.toString())
    }

    build {
        dependsOn(shadowJar)
    }
}

bukkit {
    name = "SurvivalSchematicBrush"
    main = "de.eldoria.survivalbrush.SurvivalBrush"
    apiVersion = "1.16"
    version = publishData.getVersion(true)
    authors = listOf("RainbowDashLabs")
    depend = listOf("SchematicBrushReborn")

    permissions {
        register("survivalschematicbrush.paste.bypass") {
            default = FALSE
            description = "Allow to bypass block pasting check when in survival"
        }

        register("survivalschematicbrush.limit.bypass") {
            default = FALSE
            description = "Allow to bypass max schematic size"
        }

        register("survivalschematicbrush.limit.<limit>") {
            default = FALSE
            description = "Set the max blocks a schematic can have"
        }
    }
}
