import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.FALSE
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.OP

plugins {
    java
    `maven-publish`
    alias(libs.plugins.spotless)
    alias(libs.plugins.publishdata)
    alias(libs.plugins.publishdata.manifest)
    alias(libs.plugins.shadow)
    alias(libs.plugins.pluginyml.bukkit)
    alias(libs.plugins.runserver)
}

val shadebase = "de.eldoria.schematicbrush.libs."

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-proxies/")
}

dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.fawe.core) {
        exclude("com.intellectualsites.paster")
        exclude("net.kyori")
    }
    compileOnly(libs.fawe.bukkit) {
        exclude("net.kyori")
    }
    compileOnly(libs.schematicbrushreborn.api)
    compileOnly(libs.bundles.utilities)
    compileOnly(platform(libs.jackson.bom))
    compileOnly(libs.bundles.jackson)

    testImplementation(libs.eldoutil.updater)
    testImplementation(libs.paper)
    testImplementation(libs.jetbrains.annotations)
    testImplementation(libs.mockbukkit)
    testImplementation(libs.mockito)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
    withSourcesJar()
    withJavadocJar()
}

spotless {
    java {
        target("src/**/*.java")
        licenseHeaderFile(rootProject.file("HEADER.txt"))
        trimTrailingWhitespace()
        endWithNewline()
        palantirJavaFormat(libs.versions.palantir.get()).formatJavadoc(false)
        removeUnusedImports()
        importOrder("", "java", "javax", "\\#")
        encoding("UTF-8")
    }
}

publishData {
    eldoNexus(dev = false)
    component = "shadow"
    buildData {
        put("lynaProduct", "7")
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
        dependsOn(spotlessCheck)
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    shadowJar {
        relocate("com.fasterxml", shadebase + "fasterxml")
        relocate("tools.jackson", shadebase + "jackson")
        relocate("dev.chojo.ocular", shadebase + "ocular")
        relocate("de.eldoria.jacksonbukkit", shadebase + "jacksonbukkit")
        relocate("de.eldoria.eldoutilities", shadebase + "utilities")
        mergeServiceFiles()
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        minecraftVersion("26.3")
        downloadPlugins {
            modrinth("fastasyncworldedit", "2.16.1")
            // TODO: download Schematic Brush Reborn 3 once a stable download URL exists
        }

        jvmArgs("-Dcom.mojang.eula.agree=true")
    }
}

pluginManifest {
    platforms("paper")
    requires("SchematicBrushReborn") {
        spigot(98499)
        link("https://lyna.eldoria.de/products/1")
    }
    requires("FastAsyncWorldEdit") {
        modrinth("fastasyncworldedit")
        hangar("IntellectualSites/FastAsyncWorldEdit")
    }
}

bukkit {
    name = "SurvivalSchematicBrush"
    main = "de.eldoria.survivalbrush.SurvivalBrush"
    version = project.version.toString()
    apiVersion = "1.21.11"
    authors = listOf("RainbowDashLabs")
    depend = listOf("SchematicBrushReborn", "FastAsyncWorldEdit")

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

        register("survivalschematicbrush.updates") {
            default = OP
            description = "Get notified about new versions on join and download them"
        }
    }
}
