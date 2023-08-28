import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val archivesBaseName: String by project
val modVersion: String by project
val mavenGroup: String by project
val minecraftVersion: String by project
val yarnMappings: String by project
val loaderVersion: String by project
val fabricVersion: String by project
val fabricKotlinVersion: String by project
val loomVersion: String by project
val kotlinVersion: String by project

plugins {
    id("fabric-loom") version "0.10-SNAPSHOT"
    `maven-publish`
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

loom {
    accessWidenerPath.set(file("src/main/resources/cardinal.accessWidener"))
}

version = modVersion
group = mavenGroup

repositories {
    maven(url = "https://dl.cloudsmith.io/public/arcane/archive/maven/")
    maven(url = "https://maven.fabricmc.net/")
    mavenCentral()
}

val lwjglNatives = Pair(
    System.getProperty("os.name")!!,
    System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        arrayOf("Linux", "FreeBSD", "SunOS", "Unit").any { name.startsWith(it) } ->
            if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
                "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
            else "natives-linux"
        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) } ->
            "natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"
        arrayOf("Windows").any { name.startsWith(it) } ->
            if (arch.contains("64"))
                "natives-windows${if (arch.startsWith("aarch64")) "-arm64" else ""}"
            else "natives-windows-x86"
        else -> throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}

dependencies {
    // fabric base
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")

    // Kotlin & extensions
    modImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    // sockets
    modImplementation("art.arcane:GSocks:22.3.5")
    include("art.arcane:GSocks:22.3.5")

    // database
    modImplementation("org.litote.kmongo:kmongo-serialization:4.6.0")
    include("org.litote.kmongo:kmongo-serialization:4.6.0")

    implementation("org.lwjgl:lwjgl-nanovg")
    runtimeOnly("org.lwjgl:lwjgl-nanovg:3.2.2:$lwjglNatives")

    modImplementation("com.github.Prism-Client:Aether-UI:production-SNAPSHOT") {
        exclude("commons-io")
    }
    include("com.github.Prism-Client:Aether-UI:production-SNAPSHOT") {
        exclude("commons-io")
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            if(requested.group == "org.lwjgl") {
                useVersion("3.2.2")
                because("compatibility")
            }
        }
    }

}

tasks.getByName<ProcessResources>("processResources") {
    filesMatching("fabric.mod.json") {
        expand(
            mutableMapOf(
                "version" to project.version,
            )
        )
    }
}

tasks.withType<Jar> {
    from ("LICENSE") {
        rename { "${it}_$archivesBaseName" }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}
