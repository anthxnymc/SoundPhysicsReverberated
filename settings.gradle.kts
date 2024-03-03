pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.deftu.xyz/releases")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://repo.polyfrost.org/releases")
        maven ("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    val egtVersion = "0.3.0"
    plugins {
        id("gg.essential.multi-version.root") version egtVersion
    }
    dependencyResolutionManagement {
        versionCatalogs {
            create("libs")
            create("egt") {
                plugin("multiversion", "gg.essential.multi-version").version(egtVersion)
                plugin("multiversionRoot", "gg.essential.multi-version.root").version(egtVersion)
                plugin("defaults", "gg.essential.defaults").version(egtVersion)
            }
        }
    }
}

val mod_name: String by settings

rootProject.name = mod_name
rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.20.1-forge",
    "1.20.1-fabric",
    //"1.19.2-forge",
//    "1.19.2-fabric",
//    "1.18.2-fabric",
    //"1.18.2-forge"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}