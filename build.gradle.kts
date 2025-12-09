import de.chojo.Repo
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.FALSE

plugins {
    id("org.cadixdev.licenser") version "0.6.1"
    id("com.gradleup.shadow") version "9.3.0"
    id("de.chojo.publishdata") version "1.2.4"
    id("de.eldoria.plugin-yml.bukkit") version "0.8.0"
    java
    `maven-publish`
}

group = "de.eldoria"
version = "1.1.2"

repositories {
    maven("https://eldonexus.de/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-proxies/")
}

dependencies {
    compileOnly("de.eldoria", "schematicbrushreborn-api", "2.7.8")
    compileOnly("org.spigotmc", "spigot-api", "1.13.2-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit", "worldedit-bukkit", "7.2.14")

    bukkitLibrary("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")
    bukkitLibrary("com.fasterxml.jackson.core:jackson-core:2.14.2")
    bukkitLibrary("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    bukkitLibrary("net.kyori:adventure-platform-bukkit:4.3.0")
    bukkitLibrary("net.kyori:adventure-text-minimessage:4.13.0")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

license {
    header(rootProject.file("HEADER.txt"))
    include("**/*.java")
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
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
        val shadebase = "de.eldoria.schematicbrush.libs."
        relocate("de.eldoria.messageblocker", shadebase + "messageblocker")
        relocate("com.fasterxml", shadebase + "fasterxml")
        relocate("de.eldoria.jacksonbukkit", shadebase + "jacksonbukkit")
        relocate("de.eldoria.eldoutilities", shadebase + "utilities")
        mergeServiceFiles()
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
